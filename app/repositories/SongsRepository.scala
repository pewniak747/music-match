package org.musicmatch.repositories

import org.musicmatch.models.Song
import org.musicmatch.models.Artist

import anorm._
import play.api.db.DB
import play.api.Play.current

object SongsRepository {
  lazy val findByTitleQuery = SQL("SELECT * FROM songs INNER JOIN artists ON artists.id = songs.artist_id WHERE title ILIKE {title} LIMIT 10")
  lazy val findFavouritesQuery = SQL("SELECT songs.*, artists.*, count(scrobbles) FROM songs INNER JOIN artists ON artists.id = songs.artist_id INNER JOIN scrobbles ON scrobbles.song_id = songs.id WHERE scrobbles.user_id = {userId} GROUP BY songs.id, artists.id ORDER BY count(scrobbles) DESC LIMIT 10")

  def findByTitle(title: String) = DB.withConnection { implicit c =>
    findByTitleQuery.on("title" -> toParameterValue("%" + title + "%"))().map { row =>
      new SongMapper(row).get
    }.toList
  }

  def findFavouritesByUserId(userId: Long): Seq[(Song, Long)] = DB.withConnection { implicit c =>
    findFavouritesQuery.on("userId" -> userId)().map { row =>
      val song = new SongMapper(row).get
      val scrobbleCount = row[Long]("count")
      (song, scrobbleCount)
    }.toList
  }
}
