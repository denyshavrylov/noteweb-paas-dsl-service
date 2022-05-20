import play.api.OptionalDevContext
import play.api.http.{DefaultHttpRequestHandler, HttpConfiguration, HttpErrorHandler, HttpFilters}
import play.api.mvc.request.RequestTarget
import play.api.mvc.{Handler, RequestHeader}
import play.api.routing.Router
import play.core.WebCommands

import javax.inject.Inject

class RequestHandler @Inject() (webCommands: WebCommands,
                                optDevContext: OptionalDevContext,
                                router: Router,
                                errorHandler: HttpErrorHandler,
                                configuration: HttpConfiguration,
                                filters: HttpFilters)
              extends DefaultHttpRequestHandler(webCommands, optDevContext, router, errorHandler, configuration, filters) {
  override def handlerForRequest(request: RequestHeader): (RequestHeader, Handler) = {
    super.handlerForRequest {
      if (isREST(request)) {
        addTrailingSlash(request)
      } else {
        request
      }
    }
  }

  private def isREST(request: RequestHeader) = {
    request.uri match {
      case uri: String if uri.contains("post") => true
      case _                                   => false
    }
  }

  private def addTrailingSlash(header: RequestHeader): RequestHeader = {
    if (!header.path.endsWith("/")) {
      val path = header.path + "/"
      if (header.rawQueryString.isEmpty) {
        header.withTarget(
          RequestTarget(path = path, uriString = path, queryString = Map())
        )
      } else {
        header.withTarget(
          RequestTarget(path = path, uriString = header.uri,
            queryString = header.queryString)
        )
      }
    } else {
      header
    }
  }
}
