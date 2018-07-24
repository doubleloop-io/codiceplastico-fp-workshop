lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(day1, day2, day3, demos)

lazy val day1 = project
  .settings(
    name := "day1",
    settings
  )

lazy val day2 = project
  .settings(
    name := "day2",
    settings
  )

lazy val day3 = project
  .settings(
    name := "day3",
    settings
  )

lazy val demos = project
  .settings(
    name := "demos",
    settings
  )

lazy val settings = Seq(
  organization := "io.doubleloop",
  scalaVersion := "2.12.6",
  version := "0.1.0-SNAPSHOT",
  scalacOptions ++= scalacSettings,
  resolvers ++= resolversSettings,
  libraryDependencies ++= libsSettings,
  scalafmtOnCompile := true,
  testFrameworks += new TestFramework("minitest.runner.Framework"),
  addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4"),
  addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.7"),
  addCompilerPlugin(scalafixSemanticdb)
)

lazy val scalacSettings = Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Ypartial-unification",
  "-Yrangepos",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)

lazy val resolversSettings = Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

lazy val monocleVersion = "1.5.0-cats"

lazy val libsSettings = Seq(
  "org.typelevel"              %% "cats-core"     % "1.1.0",
  "org.typelevel"              %% "cats-effect"   % "1.0.0-RC2",
  "org.typelevel"              %% "cats-mtl-core" % "0.3.0",
  "com.github.julien-truffaut" %% "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
  "net.debasishg"              %% "redisclient"   % "3.7",
  "io.monix"                   %% "minitest"      % "2.1.1" % Test
)
