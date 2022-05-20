package com.noteinweb.paas.api.v1.controlplane

import net.logstash.logback.marker.LogstashMarker
import play.api.{Logger, MarkerContext}
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, ControllerComponents, DefaultActionBuilder, MessagesRequestHeader, PlayBodyParsers, PreferredMessagesProvider, Request, RequestHeader, Result, _}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

trait ControlPlaneRequestHeader extends MessagesRequestHeader with PreferredMessagesProvider

class ControlPlaneRequest[A](request: Request[A], val messagesApi: MessagesApi)
                extends WrappedRequest(request) with ControlPlaneRequestHeader

trait RequestMarkerContext {
  import net.logstash.logback.marker.Markers

  private def marker(tuple: (String, Any)) = Markers.append(tuple._1, tuple._2)

  private implicit class RichLogstashMarker(marker1: LogstashMarker) {
    def &&(marker2: LogstashMarker): LogstashMarker = marker1.and(marker2)
  }

  implicit def requestHeaderToMarkerContext(implicit request: RequestHeader): MarkerContext = {
    MarkerContext {
      marker("id" -> request.id) && marker("host" -> request.host) && marker(
        "remoteAddress" -> request.remoteAddress
      )
    }
  }
}

class ControlPlaneActionBuilder @Inject()(messagesApi: MessagesApi,
                                          playBodyParsers: PlayBodyParsers)(
                                      implicit val executionContext: ExecutionContext)
  extends ActionBuilder[ControlPlaneRequest, AnyContent] with RequestMarkerContext with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type PostRequestBlock[A] = ControlPlaneRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: PostRequestBlock[A]): Future[Result] = {
    implicit val markerContext: MarkerContext = requestHeaderToMarkerContext(request)
    logger.trace(s"invokeBlock: ")

    val future = block(new ControlPlaneRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Case-Control" -> s"max-age: 100")
        case other => result
      }
    }
  }

}

case class ControlPlaneControllerComponents @Inject()(
                                                       controlPlaneActionBuilder: ControlPlaneActionBuilder,
                                                       controlPlaneResourceHandler: ControlPlaneResourceHandler,
                                                       actionBuilder: DefaultActionBuilder,
                                                       parsers: PlayBodyParsers,
                                                       messagesApi: MessagesApi,
                                                       langs: Langs,
                                                       fileMimeTypes: FileMimeTypes,
                                                       executionContext: ExecutionContext) extends ControllerComponents {}

class ControlPlaneBaseController @Inject()(rpc: ControlPlaneControllerComponents) extends BaseController with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = rpc

  def ResourceAction: ControlPlaneActionBuilder = rpc.controlPlaneActionBuilder

  def controlPlaneResourceHandler: ControlPlaneResourceHandler = rpc.controlPlaneResourceHandler
}