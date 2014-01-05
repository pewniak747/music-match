package org.musicmatch.controllers

import play.api._
import play.api.mvc._
import scalaoauth2.provider._

import org.musicmatch.services.OAuth2Service

object OAuth2 extends Controller with OAuth2Provider {
  def authorize = Action { implicit request =>
    issueAccessToken(new OAuth2Service)
  }

  def refresh = Action { implicit request =>
    NotImplemented("TODO")
  }

  def revoke = Action { implicit request =>
    NotImplemented("TODO")
  }
}
