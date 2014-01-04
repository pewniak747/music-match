package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import scalaoauth2.provider._

object OAuth2 extends Controller with OAuth2Provider {
  def authorize = Action { implicit request =>
    //issueAccessToken(new MyDataHandler())
    Ok("TODO")
  }

  def refresh = Action { implicit request =>
    Ok("TODO")
  }

  def revoke = Action { implicit request =>
    Ok("TODO")
  }
}
