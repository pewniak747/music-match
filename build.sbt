name := "music-match"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "securesocial" %% "securesocial" % "2.1.2",
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "com.github.nscala-time" %% "nscala-time" % "0.6.0"
)     

resolvers += Resolver.url("SBT Plugin Releases", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)

play.Project.playScalaSettings
