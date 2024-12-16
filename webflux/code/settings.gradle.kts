rootProject.name = "webflux-code"

include("root-app:apps:internal-api")
include("root-app:libs:application")
include("root-app:libs:storage:db-core")
include("root-app:libs:client")

include("branch-app:apps:internal-api")
include("branch-app:libs:application")
include("branch-app:libs:storage:db-core")
include("branch-app:libs:client")
