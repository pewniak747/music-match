package org.musicmatch.models

import com.github.nscala_time.time.Imports._

case class User(val id: Long, val email: String, val encryptedPassword: String, val createdAt: DateTime)
