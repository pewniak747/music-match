package org.musicmatch.services

import com.github.nscala_time.time.Imports._

import org.musicmatch.models
import org.musicmatch.repositories._

class ScrobbleStatistics(val userId: Long) {
  def getStatistics: models.ScrobbleStatistics = {
    val totalCount = ScrobblesRepository.count(userId, new DateTime(0))
    val lastWeekCount = ScrobblesRepository.count(userId, 1.week.ago)
    val lastMonthCount = ScrobblesRepository.count(userId, 1.month.ago)
    models.ScrobbleStatistics(totalCount, lastWeekCount, lastMonthCount)
  }

  def getFavouriteSongs: Seq[(models.Song, Long)] =
    SongsRepository.findFavouritesByUserId(userId)

  def getFavouriteArtists: Seq[(models.Artist, Long)] =
    ArtistsRepository.findFavouritesByUserId(userId)
}
