package org.musicmatch.repositories

import org.musicmatch.models.RecommendationRequest

import anorm._
import play.api.db.DB
import play.api.Play.current

object RecommendationRequestsRepository {
  lazy val createQuery = SQL("INSERT INTO recommendation_requests(user_id) VALUES({userId})")
  lazy val newestQuery = SQL("SELECT * FROM recommendation_requests WHERE enqueued_at IS NULL ORDER BY created_at ASC LIMIT {limit} FOR UPDATE")
  lazy val enqueueQuery = (ids: List[Long]) => SQL("UPDATE recommendation_requests SET enqueued_at = current_timestamp WHERE id IN (%s)".format(ids.mkString(",")))

  def create(userId: Long) = DB.withConnection { implicit c =>
    createQuery.on("userId" -> userId).executeInsert()
  }

  def newest(limit: Int = 5) = DB.withTransaction { implicit c =>
    val batch = newestQuery.on("limit" -> limit)().map { row =>
      new RecommendationRequestMapper(row).get
    }.toList
    if (!batch.isEmpty)
      enqueueQuery(batch.map(_.id)).executeUpdate()
    batch
  }
}
