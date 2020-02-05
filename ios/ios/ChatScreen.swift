//
//  ChatList.swift
//  ios
//
//  Created by Felipe Costa on 21/10/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import SwiftUI

struct ChatMessage: Hashable{
    var author: String
    var message: String
}

struct ChatScreen: View {
    
    var username: String
    
    @State var composedMessage: String = ""
    
    @EnvironmentObject var chatController: ChatController
    
    init(username: String) {
        self.username = username
        UITableView.appearance().separatorStyle = .none
    }
    
    var body: some View {
        NavigationView {
            VStack {
                    List(chatController.messages, id: \.self) {msg in
                            ChatRow(chatMessage: msg)
                    }
                HStack {
                    TextField("Message...", text: $composedMessage)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    Button(action: sendMessage) {
                        Text("Send")
                    }
                }.frame(minHeight: CGFloat(50)).padding()
            }
        }.navigationBarTitle(Text("Chat Room"), displayMode: .inline)
            .onAppear {
                self.chatController.onViewAppear()
        }
    }
    
    func sendMessage() {
        chatController.sendMessage(ChatMessage(author: username, message: composedMessage))
        composedMessage = ""
      }
}

struct ChatRow: View {
    var chatMessage: ChatMessage
    
    var body: some View {
        Group {
            HStack {
                Text(chatMessage.author)
                Text(":")
                Text(chatMessage.message)
            }
        }
    }
}


struct ChatList_Previews: PreviewProvider {
    static var previews: some View {
        ChatScreen(username: "username")
    }
}
