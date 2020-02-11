//
//  HostingController.swift
//  ChatWatch Extension
//
//  Created by Felipe Costa on 08/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import WatchKit
import Foundation
import SwiftUI
import Client

class HostingController: WKHostingController<WelcomeScreen> {
    override var body: WelcomeScreen {
//        let chatController = ChatController.createChatController()
//        .environmentObject(chatController)
        return WelcomeScreen()
    }
}
