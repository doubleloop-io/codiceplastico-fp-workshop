import sbt._

object Dependencies {
  lazy val catsCore   = "org.typelevel" %% "cats-core"     % "1.1.0"
  lazy val catsEffect = "org.typelevel" %% "cats-effect"   % "1.0.0-RC2"
  lazy val catsMtl    = "org.typelevel" %% "cats-mtl-core" % "0.3.0"
  lazy val miniTest   = "io.monix"      %% "minitest"      % "2.1.1"
}
