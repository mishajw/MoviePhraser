package moviephraser

import moviephraser.media.AudioExtractor

object MoviePhraser {
  def main(args: Array[String]): Unit = {
    val movie = MovieHolder.addMovie("/home/misha/Downloads/primer/primer.mp4", "Primer")

    MovieHolder.getLoadedMovie(movie) match {
      case Some(loaded) =>
        val audio = AudioExtractor.extract(10, 11, loaded)
        AudioPlayer.play(audio, loaded)
      case None =>
    }






  }
}
