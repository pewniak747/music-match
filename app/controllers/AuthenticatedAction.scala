package org.musicmatch.controllers

import play.api.mvc._
import scalaoauth2.provider._
import scala.concurrent.Future

import org.musicmatch.models.User
import org.musicmatch.services.OAuth2Service

class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] with OAuth2Provider {
  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[SimpleResult]) = {
    authorize(request, new OAuth2Service) { authInfo =>
      block(new AuthenticatedRequest(authInfo.user, request))
    }
  }

  def authorize[A, U](request: Request[A], dataHandler: DataHandler[U])(block: AuthInfo[U] => Future[SimpleResult]) = {
    ProtectedResource.handleRequest(request, dataHandler) match {
      case Right(authInfo) => block(authInfo)
      case Left(e) if e.statusCode == 400 => Future.successful(responseOAuthError(BadRequest, e))
      case Left(e) if e.statusCode == 401 => Future.successful(responseOAuthError(Unauthorized, e))
    }
  }

  protected def responseOAuthError(result: SimpleResult, e: OAuthError) = result.withHeaders(
    "WWW-Authenticate" -> ("Bearer " + toOAuthErrorString(e))
  )
}
