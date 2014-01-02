package org.musicmatch.repositories

import org.musicmatch.models.Scrobble
import org.musicmatch.models.Song
import org.musicmatch.models.Artist

import anorm._
import play.api.db.DB
import play.api.Play.current
import com.github.nscala_time.time.Imports._

object ScrobblesRepository {
  def create(userId: Long, songId: Long) = DB.withConnection { implicit c =>
    val artist = Artist(1, "artist name", Some("http://"))
    val song = Song(songId, "song title", artist)
    Some(Scrobble(1, DateTime.now, song, userId))
  }
}
