package com.noteinweb.paas.api.v1.controlplane

import com.noteinweb.paas.cloud.Resource
import play.api.Logger
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent}
//import v1.controlplane.ControlPlaneResourceHandler.mapFormat

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ControlPlaneController @Inject()(rcc: ControlPlaneControllerComponents)(
  implicit ec: ExecutionContext) extends ControlPlaneBaseController(rcc) {
  private val logger = Logger(getClass)
  logger.info("ControlPlaneController inited...")
  /*implicit val mapReads: Reads[immutable.Map[String, Object]] = (jv: JsValue) =>
    JsSuccess(jv.as[immutable.Map[String, Object]].map { case (k, v) =>
      k.toString -> v
    })

  implicit val mapWrites: Writes[immutable.Map[String, Object]] = (map: immutable.Map[String, Object]) =>
    Json.toJson(map.map { case (s, o) =>
      s.toString -> o
    })
  implicit val mapsFormat = Format(mapReads, mapWrites)*/

  def index: Action[AnyContent] = ResourceAction.async { implicit request =>

    //implicit val mapFormat = Json.format[immutable.Map[String, String]]
    implicit val resourceFormat = Json.format[Resource]

    logger.trace("index: ")
    controlPlaneResourceHandler.find.map { resources => Ok(Json.toJson(resources))
    }
  }
}
