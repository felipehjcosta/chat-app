pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
    
}
rootProject.name = "desktop"
include(":common:core")
project(":common:core").projectDir = file("../common/core")
include(":common:logger")
project(":common:logger").projectDir = file("../common/logger")
include(":common:client")
project(":common:client").projectDir = file("../common/client")
include(":common:client-websocket")
project(":common:client-websocket").projectDir = file("../common/client-websocket")
