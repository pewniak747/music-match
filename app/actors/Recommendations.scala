package org.musicmatch.actors

import akka.actor._
import play.Logger

import org.musicmatch.repositories._

case object DispatchRequests
case object FetchRequests
case class  RequestsBatch(requestIds: Seq[Long])

class RecommendationDispatcher extends Actor {
  val fetcher = context.actorOf(Props[RecommendationRequestFetcher], name = "fetcher")

  def receive = {
    case DispatchRequests => {
      Logger.info("received dispatch request")
      fetcher ! FetchRequests
    }

    case RequestsBatch(requestIds) => {
      Logger.info("received request batch: " + requestIds.mkString(", "))
    }
  }
}

class RecommendationRequestFetcher extends Actor {
  def receive = {
    case FetchRequests => {
      Logger.info("received fetch request")
      val batch = RecommendationRequestsRepository.newest()
      sender ! new RequestsBatch(batch.map(_.id))
    }
  }
}
