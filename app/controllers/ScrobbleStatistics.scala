package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.Writes

import org.musicmatch.models
import org.musicmatch.services

object ScrobbleStatistics extends Controller {

  def show = Action { request =>
    val service = new services.ScrobbleStatistics(userId)
    val scrobbleStatistics = service.getStatistics
    Ok(Json.toJson(scrobbleStatistics))
  }

  private

  val userId = 1 // TODO: different users

  implicit val scrobbleStatisticsWrites = new Writes[models.ScrobbleStatistics] {
    def writes(statistics: models.ScrobbleStatistics) = Json.obj(
      "total_count" -> statistics.totalCount,
      "last_week_count" -> statistics.lastWeekCount,
      "last_month_count" -> statistics.lastMonthCount
    )
  }
}
