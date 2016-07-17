package moviephraser

import java.util.concurrent.ConcurrentHashMap

import moviephraser.media.{LoadedMovie, MovieLoader}

object MovieHolder {

  private case class MovieDetails(loadedMovie: LoadedMovie)

  private val movieStore = new ConcurrentHashMap[Movie, MovieDetails]()

  def addMovie(path: String, title: String): Movie = {
    val movie = Movie(title)

    movieStore.put(
      movie,
      MovieDetails(
        MovieLoader.load(path)
      ))

    movie
  }

  def getLoadedMovie(movie: Movie): Option[LoadedMovie] = {
    Option(movieStore.get(movie)) map (_.loadedMovie)
  }

}
