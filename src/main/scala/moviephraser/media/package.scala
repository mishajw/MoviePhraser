package moviephraser

import javax.sound.sampled.AudioFormat

import io.humble.video.{Decoder, Demuxer, MediaAudio}
import io.humble.video.javaxsound.MediaAudioConverter

package object media {

  /**
    * Holds information about a loaded movie
    */
  case class LoadedMovie( decoder: Decoder,
                          format: AudioFormat,
                          converter: MediaAudioConverter,
                          demuxer: Demuxer,
                          streamIndex: Int,
                          samples: MediaAudio)
}
