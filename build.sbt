name := "MoviePhraser"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "xuggle repo" at "http://xuggle.googlecode.com/svn/trunk/repo/share/java/"

libraryDependencies ++= Seq(
//  "xuggle" % "xuggle-xuggler" % "5.4"
  "io.humble" % "humble-video-all" % "0.2.1"
)
