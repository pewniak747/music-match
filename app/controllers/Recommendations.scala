package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.Writes

import org.musicmatch.models.Recommendation
import org.musicmatch.repositories.RecommendationsRepository

object Recommendations extends Controller {

  def index = Action { request =>
    val recommendations = RecommendationsRepository.findByUserId(userId)
    Ok(Json.toJson(Map("items" -> recommendations)))
  }

  private

  val userId = 1 // TODO: different users

  implicit val recommendationWrites = new Writes[Recommendation] {
    def writes(recommendation: Recommendation) = Json.obj(
      "id" -> recommendation.id,
      "song" -> Json.obj(
        "id" -> recommendation.song.id,
        "title" -> recommendation.song.title,
        "artist" -> Json.obj(
          "id" -> recommendation.song.artist.id,
          "name" -> recommendation.song.artist.name,
          "image_url" -> recommendation.song.artist.imageURL
        )
      )
    )
  }
}
