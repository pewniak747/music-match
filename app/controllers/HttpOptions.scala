package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.http.HeaderNames._

object HttpOptions extends Controller {
  def show(path: String) = Action {
    Ok("OK")
  }
}
