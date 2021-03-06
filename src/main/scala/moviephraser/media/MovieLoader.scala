package moviephraser.media

import io.humble.video.javaxsound.MediaAudioConverterFactory
import io.humble.video.{Decoder, Demuxer, MediaAudio, MediaDescriptor}

/**
  * Responsible for loading in the movie from a file
  */
object MovieLoader {

  /**
    * Load a movie found at a path
    * @param path the path of the movie
    * @return the loaded movie
    */
  def load(path: String): LoadedMovie = {
    val demuxer = Demuxer.make()
    demuxer.open("/home/misha/Downloads/primer/primer.mp4", null, false, true, null, null)

    val (decoder, streamId) = getStreams(demuxer).head

    decoder.open(null, null)

    val samples = MediaAudio.make(
      decoder.getFrameSize,
      decoder.getSampleRate,
      decoder.getChannels,
      decoder.getChannelLayout,
      decoder.getSampleFormat
    )

    val converter = MediaAudioConverterFactory.createConverter(
      MediaAudioConverterFactory.DEFAULT_JAVA_AUDIO,
      samples
    )

    LoadedMovie(decoder, converter.getJavaFormat, converter, demuxer, streamId, samples)
  }

  /**
    * Get the streams of a movie
    * @param d the demuxer object
    * @return a list of streams and their indexes
    */
  private def getStreams(d: Demuxer): Seq[(Decoder, Int)] = {
    val numberOfStreams = d.getNumStreams

    val streams = for (i <- 0 until numberOfStreams) yield {
      val stream = d.getStream(i)
      val decoder = stream.getDecoder

      if (decoder != null && decoder.getCodecType == MediaDescriptor.Type.MEDIA_AUDIO) {
        Some((decoder, i))
      } else {
        None
      }
    }

    streams.flatten
  }
}
