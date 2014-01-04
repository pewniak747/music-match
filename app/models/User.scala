package org.musicmatch.models

case class User(val id: Long, val email: String, val encryptedPassword: String)
