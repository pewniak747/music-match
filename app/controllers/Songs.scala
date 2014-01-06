package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json._

import org.musicmatch.models.Song
import org.musicmatch.repositories.SongsRepository
import org.musicmatch.services.ScrobbleStatistics

object Songs extends Controller {

  def index(filter: String) = AuthenticatedAction {
    val songs = SongsRepository.findByTitle(filter)
    Ok(Json.toJson(Map("items" -> songs)))
  }

  def userStatistics = AuthenticatedAction { request =>
    val statistics = new ScrobbleStatistics(request.user.id)
    val favourites = statistics.getFavouriteSongs
    Ok(Json.toJson(Map("items" -> favourites)))
  }

  private

  implicit val songWrites = new Writes[Song] {
    def writes(song: Song) = Json.obj(
      "id" -> song.id,
      "title" -> song.title,
      "artist" -> Json.obj(
        "id" -> song.artist.id,
        "name" -> song.artist.name,
        "image_url" -> song.artist.imageURL
      )
    )
  }

  implicit val songStatisticsWrites = new Writes[(Song, Long)] {
    def writes(pair: (Song, Long)): JsObject = pair match {
      case (song, totalCount) => {
        val statistics = Json.obj(
          "statistics" -> Json.obj(
            "total_count" -> totalCount
          )
        )
        songWrites.writes(song) ++ statistics
      }
    }
  }
}
