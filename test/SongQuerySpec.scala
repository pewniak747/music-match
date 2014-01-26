import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import org.musicmatch.models.SongQuery

@RunWith(classOf[JUnitRunner])
class SongQuerySpec extends Specification {

  "SongQuery" should {

    "have song title by default" in {
      new SongQuery("stairway to heaven").songTitle === Some("stairway to heaven")
    }

    "return artist name if present" in {
      new SongQuery("stairway to heaven by led zeppelin").artistName === Some("led zeppelin")
    }

    "return song title if both artist and song present" in {
      new SongQuery("stairway to heaven by led zeppelin").songTitle === Some("stairway to heaven")
    }

    "return no artist name if only title present" in {
      new SongQuery("stairway to heaven").artistName === None
    }

    "return no song title if only artist name present" in {
      new SongQuery("by led zeppelin").songTitle === None
    }

    "return artist name if only artist name present" in {
      new SongQuery("by led zeppelin").artistName === Some("led zeppelin")
    }

    "returns song title in lower case" in {
      new SongQuery("Stairway to Heaven").songTitle === Some("stairway to heaven")
    }

    "returns song title in lower case in case of full query" in {
      new SongQuery("Stairway to Heaven by Led Zeppelin").songTitle === Some("stairway to heaven")
    }

    "returns artist name in lower case" in {
      new SongQuery("Stairway to Heaven by Led Zeppelin").artistName === Some("led zeppelin")
    }

    "returns none if no artist is present after conjunction" in {
      new SongQuery("stairway to heaven by").artistName === None
    }

    "returns none for empty query" in {
      val query = new SongQuery("")
      (query.songTitle, query.artistName) === (None, None)
    }
  }

}
