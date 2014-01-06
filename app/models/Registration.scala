package org.musicmatch.models

import org.musicmatch.repositories.UsersRepository

case class Registration(val email: String, val password: String, val passwordConfirmation: String) {
  def createUser: Option[User] = {
    val encryptedPassword = EncryptedPassword.encrypt(password)
    UsersRepository.create(email, encryptedPassword)
  }
}
