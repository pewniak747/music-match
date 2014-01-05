package org.musicmatch.services

import java.security.SecureRandom
import java.math.BigInteger

object Randomness {
  def hex = {
    new BigInteger(128, random).toString(16)
  }

  lazy val random = new SecureRandom
}
