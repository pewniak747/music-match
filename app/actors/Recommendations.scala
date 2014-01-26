package org.musicmatch.actors

import akka.actor._
import play.Logger

import org.musicmatch.models.RecommendationRequest
import org.musicmatch.repositories._
import org.musicmatch.services.Recommendations

import scala.collection.mutable

case object DispatchRequests
case object FetchRequests
case class  RequestsBatch(requestIds: Seq[RecommendationRequest])
case object WorkAvailable
case object RequestWork
case class  RecommendationWork(request: RecommendationRequest)

class RecommendationDispatcher extends Actor {
  val fetcher = context.actorOf(Props[RecommendationRequestFetcher], name = "fetcher")
  val workers = Vector.fill(5) { context.actorOf(Props[RecommendationWorker]) }
  var queue = mutable.Queue[RecommendationRequest]()

  def receive = {
    case DispatchRequests => {
      Logger.info("received dispatch request")
      if (queue.isEmpty)
        fetcher ! FetchRequests
      else {
        workers.map(_ ! WorkAvailable)
      }
    }

    case RequestWork => {
      if (!queue.isEmpty) {
        val request = queue.dequeue
        Logger.info("dispatching request: " + request.id)
        sender ! RecommendationWork(request)
        self ! DispatchRequests
      }
    }

    case RequestsBatch(batch) => {
      Logger.info("received request batch: " + batch.map(_.id).mkString(", "))
      if (!batch.isEmpty) {
        queue ++= batch
        self ! DispatchRequests
      }
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

class RecommendationWorker extends Actor {
  def receive = {
    case RecommendationWork(request) => {
      Logger.info("processing request: " + request.id)
      new Recommendations(request).apply
      Logger.info("processed request: " + request.id)
      sender ! RequestWork
    }
    case WorkAvailable => {
      context.parent ! RequestWork
    }
  }
}
