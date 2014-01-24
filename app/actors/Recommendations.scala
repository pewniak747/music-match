package org.musicmatch.actors

import akka.actor._
import play.Logger

import org.musicmatch.models.RecommendationRequest
import org.musicmatch.repositories._

import scala.collection.mutable

case object DispatchRequests
case object FetchRequests
case class  RequestsBatch(requestIds: Seq[RecommendationRequest])

class RecommendationDispatcher extends Actor {
  val fetcher = context.actorOf(Props[RecommendationRequestFetcher], name = "fetcher")
  var queue = mutable.Queue[RecommendationRequest]()

  def receive = {
    case DispatchRequests => {
      Logger.info("received dispatch request")
      if (queue.isEmpty)
        fetcher ! FetchRequests
      else {
        val request = queue.dequeue
        Logger.info("dispatching request: " + request.id)
        self ! DispatchRequests
      }
    }

    case RequestsBatch(batch) => {
      Logger.info("received request batch: " + batch.map(_.id).mkString(", "))
      queue ++= batch
      self ! DispatchRequests
    }
  }
}

class RecommendationRequestFetcher extends Actor {
  def receive = {
    case FetchRequests => {
      Logger.info("received fetch request")
      val batch = RecommendationRequestsRepository.newest()
      sender ! new RequestsBatch(batch)
    }
  }
}
