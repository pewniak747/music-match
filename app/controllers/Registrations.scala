package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.{Writes,Reads,JsValue}
import play.api.data.FormError
import play.api.data.Forms._

import org.musicmatch.models.User
import org.musicmatch.forms.Forms.registrationForm

object Registrations extends Controller {

  def create = Action(parse.json) { request =>
    registrationForm.bind(request.body).fold(
      formWithErrors => UnprocessableEntity(Json.obj("errors" -> formWithErrors.errors)),
      registration => registration.createUser.map { user =>
        Created(Json.toJson(user))
      }.getOrElse(UnprocessableEntity)
    )
  }

  private

  implicit val formErrorWrites = new Writes[FormError] {
    def writes(error: FormError) = Json.obj(
      "key" -> error.key,
      "message" -> error.message
    )
  }

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "email" -> user.email
    )
  }
}
