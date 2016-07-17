package moviephraser

import java.nio.ByteBuffer

import io.humble.video.javaxsound.AudioFrame
import moviephraser.media.LoadedMovie

object AudioPlayer {
  def play(bytes: Array[Byte], loadedMovie: LoadedMovie) = {
    val audioFrame = AudioFrame.make(loadedMovie.converter.getJavaFormat)

    audioFrame.play(ByteBuffer.wrap(bytes))
  }
}
