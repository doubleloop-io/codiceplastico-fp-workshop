import Dependencies._

lazy val global = project
  .in(file("."))
  .settings(settings)
  .aggregate(day1, day2, day3)

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

lazy val settings = Seq(
  organization := "io.doubleloop",
  scalaVersion := "2.12.6",
  version := "0.1.0-SNAPSHOT",
  scalacOptions ++= scalacSettings,
  resolvers ++= resolversSettings,
  libraryDependencies ++= libsSettings,
  scalafmtOnCompile := true,
  testFrameworks += new TestFramework("minitest.runner.Framework"),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4")
)

lazy val scalacSettings = Seq(
  "-encoding",
  "UTF-8", // source files are in UTF-8
  "-deprecation", // warn about use of deprecated APIs
  "-unchecked", // warn about unchecked type parameters
  "-feature", // warn about misused language features
  "-language:existentials", // ???
  "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
  "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
)

lazy val resolversSettings = Seq(
  Resolver.sonatypeRepo("public")
)

lazy val libsSettings = Seq(
  catsCore,
  catsEffect,
  catsMtl,
  miniTest % Test
)
