import xerial.sbt.Sonatype.GitHubHosting

Global / concurrentRestrictions := Seq(Tags.limit(Tags.Test, 1))

lazy val commonSettings: Seq[Setting[?]] = Seq(
  organization          := "org.jetbrains",
  licenses              += ("MIT", url("https://opensource.org/licenses/MIT")),
  scalacOptions        ++= Seq("-deprecation", "-feature", "-release", "8", "-Xfatal-warnings"),
  javacOptions         ++= Seq("--release", "8"),
  scalaVersion := "2.12.18",
  pluginCrossBuild / sbtVersion := "1.4.5",
  Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat,
  sonatypeProfileName := "org.jetbrains",
  homepage := Some(url("https://github.com/JetBrains/sbt-idea-plugin")),
  sonatypeProjectHosting := Some(GitHubHosting("JetBrains", "sbt-idea-plugin", "scala-developers@jetbrains.com")),
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.17" % Test
)

lazy val core = (project in file("core"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .settings(
    name := "sbt-declarative-core"
  )

lazy val visualizer = (project in file("visualizer"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "sbt-declarative-visualizer",
    libraryDependencies += "com.github.mutcianm" %% "ascii-graphs" % "0.0.6"
  )

lazy val packaging = (project in file("packaging"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .dependsOn(core)
  .settings(
    name := "sbt-declarative-packaging",
    libraryDependencies += "org.pantsbuild" % "jarjar" % "1.7.2"
  )

lazy val ideaSupport = (project in file("ideaSupport"))
  .enablePlugins(SbtPlugin)
  .settings(commonSettings)
  .dependsOn(core, packaging, visualizer)
  .settings(
    name := "sbt-idea-plugin",
    libraryDependencies ++= Seq(
      "org.scalaj" %% "scalaj-http" % "2.4.2",
      "org.jetbrains" % "marketplace-zip-signer" % "0.1.8",
      "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.5",
      "org.rauschig" % "jarchivelib" % "1.2.0"
    )
  )

lazy val sbtIdeaPlugin = (project in file("."))
  .settings(commonSettings)
  .settings(publish / skip := true)
  .aggregate(core, packaging, ideaSupport, visualizer)
