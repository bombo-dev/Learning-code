pluginManagement {
    plugins {
        kotlin("jvm") version "2.0.21"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "webflux-code"

include("root-app:apps:internal-api")
include("root-app:libs:application")
include("root-app:libs:storage:db-core")
include("root-app:libs:client")

include("branch-app:apps:internal-api")
include("branch-app:libs:application")
include("branch-app:libs:storage:db-core")
include("branch-app:libs:client")
