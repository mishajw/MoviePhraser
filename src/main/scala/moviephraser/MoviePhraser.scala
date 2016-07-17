package moviephraser

import java.nio.ByteBuffer

import io.humble.video.javaxsound.{AudioFrame, MediaAudioConverterFactory}
import io.humble.video._

import scala.collection.mutable.ArrayBuffer

object MoviePhraser {
  def main(args: Array[String]) {
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

    val sampleRate = converter.getJavaFormat.getSampleRate
    val frameSize = converter.getJavaFormat.getFrameSize
    val channelAmount = converter.getJavaFormat.getChannels

    val audioFrame = AudioFrame.make(converter.getJavaFormat)

    var rawAudio: ByteBuffer = null

    val packet = MediaPacket.make()

    var totalBytesRead = 0

    val bytes = new ArrayBuffer[Byte]()

    val begin = 300 * sampleRate
    val end   = 310 * sampleRate

    println(s"from $begin to $end")

    while (demuxer.read(packet) >= 0 && totalBytesRead < end) {
      if (packet.getStreamIndex == streamId) {
        var offset = 0
        var bytesRead = 0

        do {
          bytesRead += decoder.decode(samples, packet, offset)

          if (samples.isComplete) {
            rawAudio = converter.toJavaAudio(rawAudio, samples)

            if (totalBytesRead > begin) {
              bytes ++= rawAudio.array()
            }
          }

          offset += bytesRead
        } while (offset < packet.getSize)

        totalBytesRead += bytesRead
      }
    }

//    println(bytes.take(100).mkString(", "))
    println(s"${bytes.length / frameSize / 1000} seconds")

    val wrappedBytes: ByteBuffer = ByteBuffer.wrap(bytes.toArray)

    val start = System.currentTimeMillis()

    audioFrame.play(wrappedBytes)

    println(s"Play time: ${System.currentTimeMillis() - start}")
  }

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
