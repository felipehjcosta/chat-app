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
        VStack(spacing: CGFloat(8)) {
            Text("Enter your nickname")
            UsernameTextField(username: $composedUsername)
            EnterChatButton(username: $composedUsername)
        }
    }
}

struct WelcomeScreen_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeScreen()
    }
}
