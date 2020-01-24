#!/usr/bin/env bash

set -e

DIR=$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd )

KOTLIN_DIR="$DIR"
IOS_DIR="$DIR/ios"

# Generate dummy framework bo be consumed by Xcode.
"$KOTLIN_DIR/gradlew" -p "$KOTLIN_DIR" packForXcode

# Run test with Xcode
xcodebuild -project "$IOS_DIR/ios.xcodeproj" -scheme ios -sdk iphonesimulator -destination "platform=iOS Simulator,name=iPhone 11,OS=13.3"
