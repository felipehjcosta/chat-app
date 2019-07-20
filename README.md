# Chat App

[![CircleCI](https://circleci.com/gh/felipehjcosta/chat-app.svg?style=svg)](https://circleci.com/gh/felipehjcosta/chat-app)

A chat web app written in Kotlin multiplaform. This is a pet project which objective is to experiment the language in both frontend, mobile and backend. Stay tuned! :)

## Running

Explain how to run the application

### Backend
Use the command below.
```
./gradlew runBackend
```

### Frontend
Use the command below.
```
./gradlew runFrontend
```

### Android
Use the command below (You need a connected device or a virtual device).
```
./gradlew installDebug
```

You can use IntelliJ IDE to run

### iOS
Follow steps below in order to run iOS version:

1. Install `Xcode 10.2`
2. Install CocoaPods 1.6.1: 
```
gem install cocoapods -v 1.6.1
```
3. Run CocoaPods to configure the Xcode project:
```
pod --project-directory="ios" install
```
4. Open `ios/ios.xcworkspace` with Xcode

License
-------

  MIT License
  
  Copyright (c) 2016 Felipe Costa
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.
  
  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
