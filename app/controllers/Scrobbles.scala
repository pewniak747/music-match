package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.Writes

import org.musicmatch.models.Scrobble
import org.musicmatch.repositories.ScrobblesRepository

object Scrobbles extends Controller {

  def create = Action(parse.json) { request =>
    (request.body \ "song_id").asOpt[Long].map { songId =>
      ScrobblesRepository.create(userId, songId).map { scrobble =>
        Created(Json.toJson(scrobble))
      }.getOrElse {
        BadRequest(Json.obj("error" -> "invalid song id"))
      }
    }.getOrElse {
      BadRequest(Json.obj("error" -> "missing song_id parameter"))
    }
  }

  private

  val userId = 1 // TODO: different users

  implicit val scrobbleWrites = new Writes[Scrobble] {
    def writes(scrobble: Scrobble) = Json.obj(
      "id" -> scrobble.id,
      "song" -> Json.obj(
        "id" -> scrobble.song.id,
        "title" -> scrobble.song.title,
        "artist" -> Json.obj(
          "id" -> scrobble.song.artist.id,
          "name" -> scrobble.song.artist.name,
          "image_url" -> scrobble.song.artist.imageURL
        )
      )
    )
  }
}
