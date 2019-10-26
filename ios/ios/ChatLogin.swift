//
//  ChatLogin.swift
//  ios
//
//  Created by Felipe Costa on 25/10/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import SwiftUI

struct ChatLogin: View {
    @State var composedUsername: String = ""
    
    var body: some View {
        NavigationView {
            VStack(spacing: CGFloat(8)) {
                Text("Enter your username")
                TextField("Username...", text: $composedUsername)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .frame(width: CGFloat(160)).padding()
                
                NavigationLink(destination: ChatList(username: composedUsername)) {
                    Text("Go to chat")
                }
                
            }.navigationBarTitle(Text("Login Screen"))
        }
    }
    
    func goToChat() {
        
    }
}

struct ChatLogin_Previews: PreviewProvider {
    static var previews: some View {
        ChatLogin()
    }
}
