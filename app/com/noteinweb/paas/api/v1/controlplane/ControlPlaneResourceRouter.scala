package com.noteinweb.paas.api.v1.controlplane

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._
import javax.inject.Inject

class ControlPlaneResourceRouter @Inject()(controller: ControlPlaneController) extends SimpleRouter {
  val prefix = "/v1/controlplane"

  override def routes: Routes = {
    case GET(p"/") => controller.index
  }
}
