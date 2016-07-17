package moviephraser

import java.util.concurrent.ConcurrentHashMap

import moviephraser.media.{LoadedMovie, MovieLoader}

/**
  * Responsible for storing movies once they have been loaded, so they don't need to be loaded again
  */
object MovieHolder {

  /**
    * What information to store about a movie
    * @param loadedMovie the loaded movie
    */
  private case class MovieDetails(loadedMovie: LoadedMovie)

  /**
    * Where to store the movies
    */
  private val movieStore = new ConcurrentHashMap[Movie, MovieDetails]()

  /**
    * Add a movie to the store
    * @param path the path of the video file
    * @param title the title of the film
    * @return the Movie object created
    */
  def addMovie(path: String, title: String): Movie = {
    val movie = Movie(title)

    movieStore.put(
      movie,
      MovieDetails(
        MovieLoader.load(path)
      ))

    movie
  }

  /**
    * Get an already loaded movie
    * @param movie the movie to get
    * @return the loaded movie
    */
  def getLoadedMovie(movie: Movie): Option[LoadedMovie] = {
    Option(movieStore.get(movie)) map (_.loadedMovie)
  }

}
