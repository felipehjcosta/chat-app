//
//  EnterChatButton.swift
//  ChatWatch Extension
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

struct EnterChatButton: View {
    @Binding var username: String
    
    private let chatController: ChatController
    
    init(username: Binding<String>) {
        self._username = username
        chatController = ChatController.createChatController()
    }
    
    var body: some View {
        NavigationLink(destination: ChatScreen(username: self.username).environmentObject(chatController)) {
            Text("Enter")
        }
    }
}

struct EnterChatButton_Previews: PreviewProvider {
    @State static var username = "username"
    static var previews: some View {
        EnterChatButton(username: $username)
    }
}
