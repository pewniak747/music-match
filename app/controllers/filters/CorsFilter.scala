import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits._

object Global extends GlobalSettings {
  override def doFilter(action: EssentialAction) = EssentialAction { request =>
    if (Play.isDev) {
      action(request).map(_.withHeaders(
        ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
        ACCESS_CONTROL_ALLOW_HEADERS -> "Content-Type"
      ))
    } else {
      action(request)
    }
  }
}
