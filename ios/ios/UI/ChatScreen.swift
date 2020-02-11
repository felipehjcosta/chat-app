//
//  ChatList.swift
//  ios
//
//  Created by Felipe Costa on 21/10/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import SwiftUI

struct ChatScreen: View {
    
    var username: String
    
    @State var composedMessage: String = ""
    
    @EnvironmentObject var chatController: ChatController
    
    init(username: String) {
        self.username = username
        #if os(iOS)
            UITableView.appearance().separatorStyle = .none
        #endif
    }
    
    var body: some View {
        VStack {
                List(chatController.messages, id: \.self) {msg in
                        ChatRow(chatMessage: msg)
                }
            HStack {
                MessageTextField(message: $composedMessage)
                Button(action: sendMessage) {
                    Text("Send")
                }
            }.frame(minHeight: CGFloat(50)).padding()
        }
    }
    
    func sendMessage() {
        chatController.sendMessage(ChatMessage(author: username, message: composedMessage))
        composedMessage = ""
      }
}

struct ChatScreen_Previews: PreviewProvider {
    static var previews: some View {
        ChatScreen(username: "username")
    }
}
