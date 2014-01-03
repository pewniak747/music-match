package org.musicmatch.repositories

import org.musicmatch.models.Song
import org.musicmatch.models.Artist

import anorm._
import play.api.db.DB
import play.api.Play.current

object SongsRepository {
  lazy val findByTitleQuery = SQL("SELECT * FROM songs INNER JOIN artists ON artists.id = songs.artist_id WHERE title ILIKE {title}")

  def findByTitle(title: String) = DB.withConnection { implicit c =>
    findByTitleQuery.on("title" -> toParameterValue("%" + title + "%"))().map {
      row => { new SongMapper(row).get }
    }.toList
  }
}
