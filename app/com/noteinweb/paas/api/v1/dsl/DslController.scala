package com.noteinweb.paas.api.v1.dsl

import com.noteinweb.paas.dsl.compiler.{Location, WorkflowCompilationError, WorkflowCompiler, WorkflowLexerError, WorkflowParserError}
import com.noteinweb.paas.dsl.interpreter.RunResult
import play.api.Logger
import play.api.libs.json.Json

import javax.inject.Inject
import scala.concurrent.ExecutionContext


class DslController @Inject()(dcc: DslControllerComponents) (
  implicit ec: ExecutionContext) extends DslBaseController(dcc) {
  private val logger = Logger(getClass)
  implicit val locWrites = Json.writes[Location]
  implicit val wcpWrites = Json.writes[WorkflowParserError]
  implicit val wclWrites = Json.writes[WorkflowLexerError]
  implicit val wceWrites = Json.writes[WorkflowCompilationError]
  logger.info("DslController inited...")

    def run = Action { implicit request =>
      /*logger.trace*/println("Processing post: ", request.body)
      val code = request.body.asText.getOrElse("No content in POST request")
      val compilationResult = WorkflowCompiler(code);
      logger.info(s"Running: $code \nCompiled as: $compilationResult")
      if (compilationResult.isLeft) {
        val compilationError = compilationResult.left.toOption.get
        Ok(Json.toJson(compilationError)(wceWrites))
      } else {
        val runResult = controllerComponents.astInterpreter.run(compilationResult.toOption.get)
        implicit val rrWrites = Json.writes[RunResult]
        Ok(Json.toJson(runResult)(rrWrites))
      }
    }
}
