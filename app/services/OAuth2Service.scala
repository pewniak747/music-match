package org.musicmatch.services

import scalaoauth2.provider._
import collection.mutable
import java.util.Date

import org.musicmatch.models.User
import org.musicmatch.models.EncryptedPassword
import org.musicmatch.repositories.UsersRepository

class OAuth2Service extends DataHandler[User] {

  def validateClient(clientId: String, clientSecret: String, grantType: String): Boolean = {
    OAuth2Client.findById(clientId) match {
      case Some(OAuth2Client(clientId, clientSecret)) => true
      case _ => false
    }
  }

  def findUser(username: String, password: String): Option[User] = {
    UsersRepository.findByEmail(username).map { user =>
      if (EncryptedPassword.matches(user.encryptedPassword, password))
        Some(user)
      else
        None
    }.getOrElse(None)
  }

  def createAccessToken(authInfo: AuthInfo[User]): AccessToken = {
    val timeout = 7*24*60*60
    val token = Randomness.hex
    val refreshToken = Randomness.hex
    val accessToken = new AccessToken(token, Some(refreshToken), authInfo.scope, Some(timeout), new Date)
    AccessToken.store(authInfo.user.id, accessToken)
    accessToken
  }

  def getStoredAccessToken(authInfo: AuthInfo[User]): Option[AccessToken] = AccessToken.findByUserId(authInfo.user.id) match {
    case Some((id, accessToken)) => Some(accessToken)
    case _ => None
  }

  def findAccessToken(token: String): Option[AccessToken] = AccessToken.findByToken(token) match {
    case Some((id, accessToken)) => Some(accessToken)
    case _ => None
  }

  def findAuthInfoByAccessToken(accessToken: AccessToken): Option[AuthInfo[User]] = {
    AccessToken.findByToken(accessToken.token) match {
      case Some((id, accessToken)) => {
        UsersRepository.findById(id) match {
          case Some(user) => Some(AuthInfo(user, "", accessToken.scope, None))
          case _ => None
        }
      }
      case _ => None
    }
  }

  def refreshAccessToken(authInfo: AuthInfo[User], refreshToken: String): AccessToken = ???

  def findAuthInfoByCode(code: String): Option[AuthInfo[User]] = ???

  def findAuthInfoByRefreshToken(refreshToken: String): Option[AuthInfo[User]] = ???

  def findClientUser(clientId: String, clientSecret: String, scope: Option[String]): Option[User] = ???

  case class OAuth2Client(id: String, secret: String)

  object OAuth2Client {
    def findById(id: String): Option[OAuth2Client] = all.find(_.id == id)

    lazy val all = Set(
      OAuth2Client("clientid", "clientsecret")
    )
  }

  object AccessToken {
    def findByToken(token: String) = all.find(_._2.token == token)

    def findByUserId(userId: Long) = all.find(_._1 == userId)

    def store(userId: Long, accessToken: AccessToken) = all += ((userId, accessToken))

    var all = mutable.Set[(Long, AccessToken)]()
  }

}
