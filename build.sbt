import Dependencies._

lazy val day1 = project.in(file("./day1"))
lazy val day2 = project.in(file("./day2"))
lazy val day3 = project.in(file("./day3"))

lazy val root = (project in file("."))
  .aggregate(day1, day2, day3)
  .settings(
    inThisBuild(
      List(
        organization := "io.doubleloop",
        scalaVersion := "2.12.6",
        version := "0.1.0-SNAPSHOT"
      )
    ),
    name := "applied-fp-workshop",
    scalacOptions ++= Seq(
      "-encoding",
      "UTF-8", // source files are in UTF-8
      "-deprecation", // warn about use of deprecated APIs
      "-unchecked", // warn about unchecked type parameters
      "-feature", // warn about misused language features
      "-language:higherKinds", // allow higher kinded types without `import scala.language.higherKinds`
      "-Ypartial-unification" // allow the compiler to unify type constructors of different arities
    ),
    scalafmtOnCompile := true,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("public")
    ),
    libraryDependencies ++= Seq(
      catsCore,
      catsEffect,
      catsMtl,
      miniTest % Test
    ),
    testFrameworks += new TestFramework("minitest.runner.Framework"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4")
  )
