//
//  ChatController.swift
//  ios
//
//  Created by Felipe Costa on 21/10/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import Combine
import SwiftUI
import client

class ChatController: ObservableObject {
    var chatViewModel: ChatViewModel
    
    var didChange = PassthroughSubject<Void, Never>()
    
    @Published var messages: [ChatMessage] = []
    
    init(_ chatViewModel: ChatViewModel) {
        self.chatViewModel = chatViewModel
        self.chatViewModel.start()
        
        self.chatViewModel.onChat = { (message) -> Void in
            print(">>> messge: \(message)")
            self.messages.append(ChatMessage(author: message.author, message: message.message))
            self.didChange.send(())
        }
        
        self.chatViewModel.showFailureMessage = { (showFailureMessage) -> Void in
            print(">>> showFailureMessage: \(showFailureMessage)")
        }
    }
    
    func sendMessage(_ chatMessage: ChatMessage) {
        self.chatViewModel.sendMessage(message: CoreMessage(author: chatMessage.author, message: chatMessage.message))
    }
}
