plugins {
    id("org.openbakery.xcode-plugin")
}

xcodebuild {
    scheme = "ios"
    target = "ios"

    setDestination(listOf("iPhone 8"))
}