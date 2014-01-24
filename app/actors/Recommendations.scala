package org.musicmatch.actors

import akka.actor._
import play.Logger

case object DispatchRequests

class RecommendationDispatcher extends Actor {
  def receive = {
    case DispatchRequests => {
      Logger.info("received dispatch request")
    }
  }

}
