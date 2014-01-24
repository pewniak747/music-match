import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent.Akka

import akka.actor._
import scala.concurrent.duration._
import org.musicmatch.actors._

object Global extends GlobalSettings {
  override def doFilter(action: EssentialAction) = EssentialAction { request =>
    action(request).map(_.withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
      ACCESS_CONTROL_ALLOW_HEADERS -> "Content-Type,Authorization"
    ))
  }

  override def onStart(app: Application) {
    val recommendationDispatcher = Akka.system.actorOf(Props[RecommendationDispatcher], name = "dispatcher")
    Akka.system.scheduler.schedule(0.seconds, 5.seconds, recommendationDispatcher, DispatchRequests)
  }
}
