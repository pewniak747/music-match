package org.musicmatch.services

import anorm._
import play.api.db.DB
import play.api.Play.current

import org.musicmatch.models._
import org.musicmatch.repositories.RecommendationsRepository
import org.musicmatch.repositories.ArtistsRepository

class Recommendations(val request: RecommendationRequest) {
  def apply = {
    val songIds = recommendedSongIds
    if (!songIds.isEmpty)
      RecommendationsRepository.insert(request.userId, songIds)
  }

  private

  def recommendedSongIds: Seq[Long] = (popularFromSimilarArtists ++ popularFromFavouriteArtists).distinct

  def popularFromFavouriteArtists: Seq[Long] = DB.withConnection { implicit c => popularFromFavouriteArtistsQuery().map { row => row[Long]("id") }.toList }

  def popularFromSimilarArtists: Seq[Long] = DB.withConnection { implicit c => popularFromSimilarArtistsQuery().map { row => row[Long]("songs.id") }.toList }

  lazy val favouriteArtistIds: Seq[Long] = 0 :: ArtistsRepository.findFavouritesByUserId(request.userId).map(_._1.id).toList
  lazy val similarArtistIds: Seq[Long] = DB.withConnection { implicit c => 0 :: similarArtistsQuery().map { row => row[Long]("artists.id") }.toList }
  lazy val alreadySeenSongIds: Seq[Long] = DB.withConnection { implicit c => 0 :: alreadySeenSongIdsQuery.on("userId" -> request.userId)().map { row => row[Long]("song_id") }.toList }
  lazy val favouriteTagIds: Seq[Long] = DB.withConnection { implicit c => 0 :: favouriteTagsQuery().take(5).map { row => row[Long]("tags.id") }.toList }

  lazy val popularFromFavouriteArtistsQuery = SQL("SELECT songs.id, count(scrobbles) FROM songs LEFT OUTER JOIN scrobbles ON scrobbles.song_id = songs.id WHERE artist_id IN (%s) AND songs.id NOT IN (%s) GROUP BY songs.id ORDER BY count(scrobbles) LIMIT 3".format(favouriteArtistIds.mkString(","), alreadySeenSongIds.mkString(",")))
  lazy val alreadySeenSongIdsQuery = SQL("SELECT scrobbles.song_id FROM scrobbles WHERE scrobbles.user_id = {userId} UNION SELECT recommendations.song_id FROM recommendations WHERE recommendations.user_id = {userId}")
  lazy val popularFromSimilarArtistsQuery = SQL("SELECT songs.id, count(scrobbles) FROM songs LEFT OUTER JOIN scrobbles ON scrobbles.song_id = songs.id WHERE artist_id IN (%s) AND songs.id NOT IN (%s) GROUP BY songs.id ORDER BY count(scrobbles) LIMIT 7".format(similarArtistIds.mkString(","), alreadySeenSongIds.mkString(",")))
  lazy val similarArtistsQuery = SQL("SELECT DISTINCT artists.id, count(taggings) FROM artists INNER JOIN taggings ON artists.id = taggings.artist_id WHERE taggings.tag_id IN (%s) AND artists.id NOT IN (%s) GROUP BY artists.id ORDER BY count DESC LIMIT 10".format(favouriteTagIds.mkString(","), favouriteArtistIds.mkString(",")))
  lazy val favouriteTagsQuery = SQL("SELECT tags.id, count(taggings) FROM tags INNER JOIN taggings ON taggings.tag_id = tags.id WHERE taggings.artist_id IN (%s) GROUP BY tags.id ORDER BY count(taggings) DESC".format(favouriteArtistIds.mkString(",")))
}
