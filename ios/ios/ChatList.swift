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

struct ChatList: View {
    
    @State var composedAuthor: String = ""
    @State var composedMessage: String = ""
    @EnvironmentObject var chatController: ChatController
    
    init() {
        UITableView.appearance().separatorStyle = .none
    }
    
    var body: some View {
        VStack {
                List(chatController.messages, id: \.self) {msg in
                        ChatRow(chatMessage: msg)
                }
            HStack {
                VStack {
                    TextField("Author...", text: $composedAuthor)
                    TextField("Message...", text: $composedMessage)
                }
                Button(action: sendMessage) {
                    Text("Send")
                }
            }.frame(minHeight: CGFloat(50)).padding()
        }
    }
    
    func sendMessage() {
        chatController.sendMessage(ChatMessage(author: composedAuthor, message: composedMessage))
        composedAuthor = ""
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
        ChatList()
    }
}
