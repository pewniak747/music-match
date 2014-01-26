package org.musicmatch.repositories

import org.musicmatch.models.Song
import org.musicmatch.models.SongQuery
import org.musicmatch.models.Artist

import anorm._
import play.api.db.DB
import play.api.Play.current

object SongsRepository {
  lazy val findByTitleQuery = SQL("SELECT * FROM songs INNER JOIN artists ON artists.id = songs.artist_id WHERE lower(title) LIKE {title} LIMIT 12")
  lazy val findByArtistQuery = SQL("SELECT * FROM artists INNER JOIN songs ON artists.id = songs.artist_id WHERE lower(artists.name) LIKE {artist} ORDER BY length(artists.name) LIMIT 12")
  lazy val findByTitleAndArtistQuery = SQL("SELECT * FROM songs INNER JOIN artists ON artists.id = songs.artist_id WHERE lower(title) LIKE {title} AND lower(artists.name) LIKE {artist} ORDER BY length(title) LIMIT 12")
  lazy val findFavouritesQuery = SQL("SELECT songs.*, artists.*, count(scrobbles) FROM songs INNER JOIN artists ON artists.id = songs.artist_id INNER JOIN scrobbles ON scrobbles.song_id = songs.id WHERE scrobbles.user_id = {userId} GROUP BY songs.id, artists.id ORDER BY count(scrobbles) DESC LIMIT 10")

  def findByQuery(query: SongQuery) = DB.withConnection { implicit c =>
    ((query.songTitle, query.artistName) match {
      case (Some(title), Some(artist)) => findByTitleAndArtistQuery.on("title" -> (title + "%"), "artist" -> (artist + "%"))()
      case (Some(title), None)         => findByTitleQuery.on("title" -> (title + "%"))()
      case (None, Some(artist))        => findByArtistQuery.on("artist" -> (artist + "%"))()
      case (None, None)                => List()
    }).map { row =>
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
