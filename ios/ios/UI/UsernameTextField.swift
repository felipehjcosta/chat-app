//
//  UsernameIOSText.swift
//  ios
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

struct UsernameTextField: View {
    @Binding var username: String
    
    var body: some View {
        TextField("Nickname...", text: $username)
            .textFieldStyle(RoundedBorderTextFieldStyle())
            .frame(width: CGFloat(160))
            .padding()
    }
}

struct UsernameTextField_Previews: PreviewProvider {
    @State static var username = "username"
    static var previews: some View {
        UsernameTextField(username: $username)
    }
}
