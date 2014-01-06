package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.libs.json
import play.api.libs.json.Json
import play.api.libs.json.{Writes,Reads,JsValue}
import play.api.data.FormError
import play.api.data.Forms._

import org.musicmatch.forms.Forms.registrationForm

object Registrations extends Controller {

  def create = Action(parse.json) { request =>
    registrationForm.bind(request.body).fold(
      formWithErrors => UnprocessableEntity(Json.obj("errors" -> formWithErrors.errors)),
      _ => Created
    )
  }

  implicit val formErrorWrites = new Writes[FormError] {
    def writes(error: FormError) = Json.obj(
      "key" -> error.key,
      "message" -> error.message
    )
  }
}
