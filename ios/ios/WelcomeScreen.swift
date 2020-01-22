//
//  ChatLogin.swift
//  ios
//
//  Created by Felipe Costa on 25/10/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import SwiftUI

struct WelcomeScreen: View {
    @State var composedUsername: String = ""
    
    var body: some View {
        NavigationView {
            VStack(spacing: CGFloat(8)) {
                Text("Enter your nickname")
                TextField("Nickname...", text: $composedUsername)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .frame(width: CGFloat(160)).padding()
                
                NavigationLink(destination: ChatScreen(username: composedUsername)) {
                    Text("Enter")
                }
                
            }.navigationBarTitle(Text("Welcome to Global Chat"))
        }
    }
    
    func goToChat() {
        
    }
}

struct ChatLogin_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeScreen()
    }
}
