package com.noteinweb.paas.api.v1.dsl

import com.noteinweb.paas.cloud.act.CloudActor
import com.noteinweb.paas.cloud.transform.CloudTransformer
import com.noteinweb.paas.dsl.interpreter.{ASTInterpreter, ASTInterpreterImpl}
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc.{BaseController, ControllerComponents, DefaultActionBuilder, PlayBodyParsers}
import com.noteinweb.paas.dsl.InjectableAstInterpreterImpl
import javax.inject.Inject
import scala.concurrent.ExecutionContext


case class DslControllerComponents @Inject() (
                                                    actionBuilder: DefaultActionBuilder,
                                                    parsers: PlayBodyParsers,
                                                    messagesApi: MessagesApi,
                                                    langs: Langs,
                                                    fileMimeTypes: FileMimeTypes,
                                                    astInterpreter: InjectableAstInterpreterImpl,
                                                    executionContext: ExecutionContext) extends ControllerComponents {}

class DslBaseController @Inject()(dcc: DslControllerComponents) extends BaseController  {
  override protected def controllerComponents: DslControllerComponents = dcc
}
