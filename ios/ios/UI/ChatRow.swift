//
//  ChatRow.swift
//  ios
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

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

struct ChatRow_Previews: PreviewProvider {
    static var previews: some View {
        ChatRow(chatMessage: ChatMessage(author: "Hello", message: "World"))
    }
}
