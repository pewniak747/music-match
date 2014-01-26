package org.musicmatch.models

import scala.util.matching._

class SongQuery(rawQuery: String) {

  def songTitle: Option[String] = queryMatch match {
    case Some(m) => trimmedString(m.subgroups(0))
    case _ => trimmedString(query)
  }

  def artistName: Option[String] = queryMatch match {
    case Some(m) => trimmedString(m.subgroups(2))
    case _ => None
  }

  private

  def trimmedString(s: String) = s.trim match {
    case s if s.length > 0 => Some(s)
    case _ => None
  }

  val query = rawQuery.toLowerCase
  lazy val queryMatch = queryRegex.findFirstMatchIn(query)
  val queryRegex = new Regex("""^(.*)(\s*by\s+)(.*)""")
}
