package org.musicmatch.repositories

import org.musicmatch.models.Artist

import anorm._
import play.api.db.DB
import play.api.Play.current

object ArtistsRepository {
  lazy val findFavouritesQuery = SQL("SELECT artists.*, count(scrobbles) FROM artists INNER JOIN songs ON artists.id = songs.artist_id INNER JOIN scrobbles ON scrobbles.song_id = songs.id WHERE scrobbles.user_id = {userId} GROUP BY artists.id ORDER BY count(scrobbles) DESC LIMIT 10")

  def findFavouritesByUserId(userId: Long): Seq[(Artist, Long)] = DB.withConnection { implicit c =>
    findFavouritesQuery.on("userId" -> userId)().map { row =>
      val artist = new ArtistMapper(row).get
      val scrobbleCount = row[Long]("count")
      (artist, scrobbleCount)
    }.toList
  }
}
