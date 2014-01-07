package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json._

import org.musicmatch.models.Artist
import org.musicmatch.services.ScrobbleStatistics

object Artists extends Controller {

  def userStatistics = AuthenticatedAction { request =>
    val statistics = new ScrobbleStatistics(request.user.id)
    val favourites = statistics.getFavouriteArtists
    Ok(Json.toJson(Map("items" -> favourites)))
  }

  private

  implicit val artistWrites = new Writes[Artist] {
    def writes(artist: Artist) = Json.obj(
      "id" -> artist.id,
      "name" -> artist.name,
      "image_url" -> artist.imageURL
    )
  }

  implicit val artistStatisticsWrites = new Writes[(Artist, Long)] {
    def writes(pair: (Artist, Long)) = pair match {
      case (artist, totalCount) => {
        val statistics = Json.obj(
          "statistics" -> Json.obj(
            "total_count" -> totalCount
          )
        )
        artistWrites.writes(artist) ++ statistics
      }
    }
  }
}
