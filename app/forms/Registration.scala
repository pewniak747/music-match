package org.musicmatch.forms

import play.api.data._
import play.api.data.Forms._

import org.musicmatch.models.Registration

object Forms {
  val registrationForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 5),
      "password_confirmation" -> text
    )(Registration.apply)(Registration.unapply).verifying("password_confirmation_match", fields => fields.password == fields.passwordConfirmation)
  )
}
