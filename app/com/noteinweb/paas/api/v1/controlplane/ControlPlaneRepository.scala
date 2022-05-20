package com.noteinweb.paas.api.v1.controlplane

import akka.actor.ActorSystem
import com.noteinweb.paas.cloud.Resource
import com.noteinweb.paas.cloud.aws.AWSCloudInspector
import play.api.{Logger, MarkerContext}
import play.api.libs.concurrent.CustomExecutionContext

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

case class ControlPlaneExecutionContext @Inject()(actorSystem: ActorSystem,
                                             awsCloudInspector: AWSCloudInspector)
  extends CustomExecutionContext(actorSystem, "repository.dispatcher"){}

trait ControlPlaneRepository {
  def list()(implicit mc: MarkerContext) : Future[Iterable[Resource]]
}

@Singleton
class ControlPlaneRepositoryImpl @Inject()(implicit ec: ControlPlaneExecutionContext) extends ControlPlaneRepository {
  private val logger = Logger(this.getClass)

  override def list() (implicit mc: MarkerContext) : Future[Iterable[Resource]] = {
    Future {
      logger.trace(s"list: ")
      ec.awsCloudInspector.getResources(null, null)
    }
  }
}
