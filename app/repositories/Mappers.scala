package org.musicmatch.repositories

import anorm._
import com.github.nscala_time.time.Imports._

import org.musicmatch.repositories.AnormExtensions._
import org.musicmatch.models._

abstract class Mapper[T](val row: SqlRow) {
  def get: T
}

class ArtistMapper(row: SqlRow) extends Mapper[Artist](row) {
  override def get = Artist(row[Long]("artists.id"), row[String]("artists.name"), Some(row[String]("artists.image_url")))
}

class SongMapper(row: SqlRow) extends Mapper[Song](row) {
  override def get = {
    val artist = new ArtistMapper(row).get
    Song(row[Long]("songs.id"), row[String]("songs.title"), artist)
  }
}

class ScrobbleMapper(row: SqlRow) extends Mapper[Scrobble](row) {
  override def get = {
    val song = new SongMapper(row).get
    Scrobble(row[Long]("scrobbles.id"), row[DateTime]("scrobbles.created_at"), song, row[Long]("scrobbles.user_id"))
  }
}

class RecommendationMapper(row: SqlRow) extends Mapper[Recommendation](row) {
  override def get = {
    val song = new SongMapper(row).get
    Recommendation(row[Long]("recommendations.id"), row[DateTime]("recommendations.created_at"), song, row[Long]("recommendations.user_id"))
  }
}
