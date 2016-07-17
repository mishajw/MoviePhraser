package moviephraser.media

import java.nio.ByteBuffer

import io.humble.video.MediaPacket

import scala.collection.mutable.ArrayBuffer

object AudioExtractor {
  def extract(startSeconds: Double, endSeconds: Double, loadedMovie: LoadedMovie): Array[Byte] = {
    val sampleRate = loadedMovie.format.getSampleRate
    var rawAudio: ByteBuffer = null
    val packet = MediaPacket.make()

    var totalBytesRead = 0
    val bytes = new ArrayBuffer[Byte]()

    val begin = startSeconds * sampleRate
    val end   = endSeconds   * sampleRate

    while (loadedMovie.demuxer.read(packet) >= 0 && totalBytesRead < end) {
      if (packet.getStreamIndex == loadedMovie.streamIndex) {
        var offset = 0
        var bytesRead = 0

        do {
          bytesRead += loadedMovie.decoder.decode(loadedMovie.samples, packet, offset)

          if (loadedMovie.samples.isComplete) {
            rawAudio = loadedMovie.converter.toJavaAudio(rawAudio, loadedMovie.samples)

            if (totalBytesRead > begin) {
              bytes ++= rawAudio.array()
            }
          }

          offset += bytesRead
        } while (offset < packet.getSize)

        totalBytesRead += bytesRead
      }
    }

    bytes.toArray
  }
}
