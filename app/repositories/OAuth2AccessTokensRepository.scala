package org.musicmatch.repositories

import anorm._
import play.api.db.DB
import play.api.Play.current
import org.musicmatch.repositories.AnormExtensions._
import scalaoauth2.provider.AccessToken

object OAuth2AccessTokensRepository {
  lazy val findByTokenQuery = SQL("SELECT * FROM oauth2_access_tokens WHERE token = {token} LIMIT 1")
  lazy val findByUserIdQuery = SQL("SELECT * FROM oauth2_access_tokens WHERE user_id = {userId} ORDER BY created_at DESC LIMIT 1")
  lazy val createQuery = SQL("INSERT INTO oauth2_access_tokens(token, refresh_token, scope, expires_in, created_at, user_id) VALUES({token}, {refreshToken}, {scope}, {expiresIn}, {createdAt}, {userId})")

  def findByToken(token: String) = DB.withConnection { implicit c =>
    findByTokenQuery.on("token" -> token)().map { row =>
      new OAuth2AccessTokenMapper(row).getWithUserId
    }.headOption
  }

  def findByUserId(userId: Long) = DB.withConnection { implicit c =>
    findByUserIdQuery.on("userId" -> userId)().map { row =>
      new OAuth2AccessTokenMapper(row).getWithUserId
    }.headOption
  }

  def create(userId: Long, accessToken: AccessToken) = DB.withConnection { implicit c =>
    createQuery.on("token" -> accessToken.token, "refreshToken" -> accessToken.refreshToken, "scope" -> accessToken.scope, "expiresIn" -> accessToken.expiresIn, "createdAt" -> accessToken.createdAt, "userId" -> userId).execute()
  }
}
