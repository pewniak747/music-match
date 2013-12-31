package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.Writes

import org.musicmatch.models.Song
import org.musicmatch.repositories.SongsRepository

object Songs extends Controller {

  def index(filter: String) = Action { request =>
    val songs = SongsRepository.findByTitle(filter)
    Ok(Json.toJson(Map("items" -> songs)))
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
}
