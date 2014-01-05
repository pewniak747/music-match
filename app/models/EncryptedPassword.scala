package org.musicmatch.models

import java.security.MessageDigest

object EncryptedPassword {
  def encrypt(plainPassword: String) = MessageDigest.getInstance("SHA").digest(plainPassword.getBytes).map("%02x".format(_)).mkString
}
