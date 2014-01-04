package org.musicmatch.repositories

import org.musicmatch.models.Scrobble
import org.musicmatch.models.Song
import org.musicmatch.models.Artist

import anorm._
import play.api.db.DB
import play.api.Play.current
import com.github.nscala_time.time.Imports._
import org.musicmatch.repositories.AnormExtensions._

object ScrobblesRepository {
  lazy val createQuery = SQL("INSERT INTO scrobbles (user_id, song_id, created_at) VALUES({userId}, {songId}, {createdAt})")
  lazy val findByIdQuery = SQL("SELECT * FROM scrobbles INNER JOIN songs ON scrobbles.song_id = songs.id INNER JOIN artists ON songs.artist_id = artists.id WHERE scrobbles.id = {id}")
  lazy val countByUserIdQuery = SQL("SELECT count(*) FROM scrobbles WHERE scrobbles.user_id = {userId} AND scrobbles.created_at > {startTime}")

  def create(userId: Long, songId: Long) = DB.withConnection { implicit c =>
    createQuery.on("userId" -> userId, "songId" -> songId, "createdAt" -> toParameterValue(DateTime.now)).executeInsert().map { id =>
      findByIdQuery.on("id" -> id)().map { row =>
        new ScrobbleMapper(row).get
      }.headOption
    }
  }

  def count(userId: Long, startTime: DateTime) = DB.withConnection { implicit c =>
    countByUserIdQuery.on("userId" -> userId, "startTime" -> startTime)().head[Long]("count")
  }
}
