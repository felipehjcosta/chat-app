//
//  MessageTextField.swift
//  ChatWatch Extension
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

struct MessageTextField: View {
    @Binding var message: String
    
    var body: some View {
        TextField("Message...", text: $message)
    }
}

struct MessageTextField_Previews: PreviewProvider {
    @State static var message = "message"
    static var previews: some View {
        MessageTextField(message: $message)
    }
}
