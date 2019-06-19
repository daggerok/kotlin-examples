import org.gradle.api.JavaVersion

object Globals {
  object Project {
    const val version = "1.0.0-SNAPSHOT"
    const val groupId = "com.github.daggerok"
    const val artifactId = "kotlin-coroutines-examples"
  }

  val javaVersion = JavaVersion.VERSION_11
  const val kotlinVersion = "1.3.31"
  const val kotlinCoroutinesVersion = "1.2.1"

  const val junitVersion = "4.13-beta-3"
  const val junitJupiterVersion = "5.5.0-M1"
  const val assertjVersion = "3.12.2"

  object Gradle {
    const val wrapperVersion = "5.4.1"

    object Plugin {
      const val versionsVersion = "0.21.0"
    }
  }
}
