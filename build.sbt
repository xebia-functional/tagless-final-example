ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file(".")).settings(
  name := "tagless-final-playground",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.8.0",
    "org.typelevel" %% "cats-effect" % "3.3.14",
    "org.scalameta" %% "munit" % "0.7.29" % Test,
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
  ),
  testFrameworks += new TestFramework("munit.Framework")
)
