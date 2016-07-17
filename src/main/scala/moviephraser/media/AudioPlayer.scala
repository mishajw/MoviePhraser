package moviephraser.media

import java.nio.ByteBuffer

import io.humble.video.javaxsound.AudioFrame

/**
  * Plays audio to speakers
  */
object AudioPlayer {

  /**
    * Play sound bytes
    * @param bytes the sound bytes
    * @param loadedMovie where they are loaded from, used to extract the audio format
    */
  def play(bytes: Array[Byte], loadedMovie: LoadedMovie) = {
    val audioFrame = AudioFrame.make(loadedMovie.converter.getJavaFormat)

    audioFrame.play(ByteBuffer.wrap(bytes))
  }
}
