package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.Writes

import org.musicmatch.models.User

object Users extends Controller {

  def show = AuthenticatedAction { request =>
    Ok(Json.toJson(request.user))
  }

  private

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "email" -> user.email
    )
  }
}
