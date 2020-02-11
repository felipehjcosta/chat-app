//
//  ChatIOSScreen.swift
//  ios
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

struct ChatScreenWithNavigation: View {
    
    var username: String
    
    init(username: String) {
        self.username = username
    }
    
    var body: some View {
        NavigationView {
            ChatScreen(username: username)
        }.navigationBarTitle(Text("Chat Room"), displayMode: .inline)
    }
}

struct ChatScreenWithNavigation_Previews: PreviewProvider {
    static var previews: some View {
        ChatScreen(username: "username")
    }
}
