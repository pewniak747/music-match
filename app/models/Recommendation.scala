package org.musicmatch.models

import com.github.nscala_time.time.Imports._

case class Recommendation(val id: Long, val createdAt: DateTime, val song: Song, val userId: Long)
