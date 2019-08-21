#!/usr/bin/env bash

set -e

DIR=$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd )

KOTLIN_DIR="$DIR"
IOS_DIR="$DIR/ios"

#Check CocoaPods version.
REQUIRED_POD_VERSION="1.6.1"
POD_VERSION=`pod --version`
EARLIER_VERSION=`echo "$POD_VERSION $REQUIRED_POD_VERSION" | tr " " "\n" | sort -V | head -1`

if [ "$EARLIER_VERSION" != "$REQUIRED_POD_VERSION" ]; then
    echo "ERROR: This version of CocoaPods is unsupported. Current version is $POD_VERSION. Minimal required version is $REQUIRED_POD_VERSION."
    echo "See update instructions at https://guides.cocoapods.org/using/getting-started.html#updating-cocoapods"
    exit 1
fi

# Generate dummy framework bo be consumed by Xcode.
"$KOTLIN_DIR/gradlew" -p "$KOTLIN_DIR" generateDummyFramework

# Run CocoaPods to configure the Xcode project.
pod --project-directory="$IOS_DIR" install

# Run test with Xcode
xcodebuild -workspace "$IOS_DIR/ios.xcworkspace" -scheme ios -sdk iphonesimulator -destination "platform=iOS Simulator,name=iPhone Xʀ,OS=12.4"
