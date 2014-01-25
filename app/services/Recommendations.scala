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

  def recommendedSongIds: Seq[Long] = (popularFromFavouriteArtists ++ popularFromSimilarArtists).distinct

  def popularFromFavouriteArtists: Seq[Long] = DB.withConnection { implicit c => popularFromFavouriteArtistsQuery().map { row => row[Long]("id") }.toList }

  def popularFromSimilarArtists: Seq[Long] = List()

  lazy val favouriteArtistIds: Seq[Long] = -1 :: ArtistsRepository.findFavouritesByUserId(request.userId).map(_._1.id).toList
  lazy val alreadySeenSongIds: Seq[Long] = DB.withConnection { implicit c => -1 :: alreadySeenSongIdsQuery.on("userId" -> request.userId)().map { row => row[Long]("song_id") }.toList }
  lazy val popularFromFavouriteArtistsQuery = SQL("SELECT songs.id, count(scrobbles) FROM songs LEFT OUTER JOIN scrobbles ON scrobbles.song_id = songs.id WHERE artist_id IN (%s) AND songs.id NOT IN (%s) GROUP BY songs.id ORDER BY count(scrobbles) LIMIT 3".format(favouriteArtistIds.mkString(","), alreadySeenSongIds.mkString(",")))
  lazy val alreadySeenSongIdsQuery = SQL("SELECT scrobbles.song_id FROM scrobbles WHERE scrobbles.user_id = {userId} UNION SELECT recommendations.song_id FROM recommendations WHERE recommendations.user_id = {userId}")
}
