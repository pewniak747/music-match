package org.musicmatch.repositories

import org.musicmatch.models.RecommendationRequest

import anorm._
import play.api.db.DB
import play.api.Play.current

object RecommendationRequestsRepository {
  lazy val createQuery = SQL("INSERT INTO recommendation_requests(user_id) VALUES({userId})")

  def create(userId: Long) = DB.withConnection { implicit c =>
    createQuery.on("userId" -> userId).executeInsert()
  }
}
