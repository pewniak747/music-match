package org.musicmatch.repositories

import org.musicmatch.models.User

import anorm._
import play.api.db.DB
import play.api.Play.current
import org.musicmatch.repositories.AnormExtensions._

object UsersRepository {
  lazy val findByEmailQuery = SQL("SELECT * FROM users WHERE users.email = {email} LIMIT 1")
  lazy val findByIdQuery = SQL("SELECT * FROM users WHERE users.id = {id} LIMIT 1")

  def findByEmail(email: String) = DB.withConnection { implicit c =>
    findByEmailQuery.on("email" -> email)().map { row =>
      new UserMapper(row).get
    }.headOption
  }

  def findById(id: Long) = DB.withConnection { implicit c =>
    findByIdQuery.on("id" -> id)().map { row =>
      new UserMapper(row).get
    }.headOption
  }
}
