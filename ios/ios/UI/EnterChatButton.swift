//
//  EnterChatButton.swift
//  ios
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

struct EnterChatButton: View {    
    @Binding var username: String
    
    var body: some View {
        NavigationLink(destination: ChatScreenWithNavigation(username: self.username)) {
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
