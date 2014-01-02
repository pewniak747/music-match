package org.musicmatch.repositories

import org.musicmatch.models.Scrobble
import org.musicmatch.models.Song
import org.musicmatch.models.Artist

import org.musicmatch.repositories.AnormExtensions._

import anorm._
import play.api.db.DB
import play.api.Play.current
import com.github.nscala_time.time.Imports._

object ScrobblesRepository {
  lazy val createQuery = SQL("INSERT INTO scrobbles (user_id, song_id, created_at) VALUES({userId}, {songId}, {createdAt})")
  lazy val findByIdQuery = SQL("SELECT * FROM scrobbles INNER JOIN songs ON scrobbles.song_id = songs.id INNER JOIN artists ON songs.artist_id = artists.id WHERE scrobbles.id = {id}")

  def create(userId: Long, songId: Long) = DB.withConnection { implicit c =>
    createQuery.on("userId" -> userId, "songId" -> songId, "createdAt" -> toParameterValue(DateTime.now)).executeInsert().map { id =>
      findByIdQuery.on("id" -> id)().map { row =>
        val artist = Artist(row[Long]("artists.id"), row[String]("artists.name"), Some(row[String]("artists.image_url")))
        val song = Song(row[Long]("songs.id"), row[String]("songs.title"), artist)
        Scrobble(row[Long]("scrobbles.id"), row[DateTime]("scrobbles.created_at"), song, row[Long]("scrobbles.user_id"))
      }.headOption
    }.getOrElse {
      None
    }
  }
}
