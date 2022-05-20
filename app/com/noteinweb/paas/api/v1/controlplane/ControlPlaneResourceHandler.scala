package com.noteinweb.paas.api.v1.controlplane

import com.noteinweb.paas.cloud.Resource
import play.api.MarkerContext
import play.api.libs.json.{Format, Json}

import javax.inject.{Inject, Provider}
import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

object ControlPlaneResourceHandler {
  //implicit val mapFormat: Format[immutable.Map[String, String]] = Json.format
  implicit val format: Format[Resource] = Json.format
}

class ControlPlaneResourceHandler @Inject()(routerProvider: Provider[ControlPlaneResourceRouter],
                                            resourceRepository: ControlPlaneRepository)(implicit ec:ExecutionContext) {
  def find(implicit mc: MarkerContext) : Future[Iterable[Resource]] = {
    resourceRepository.list()
  }

}
