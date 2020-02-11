//
//  WelcomeIOSScreen.swift
//  ios
//
//  Created by Felipe Costa on 09/02/20.
//  Copyright © 2020 Felipe Costa. All rights reserved.
//

import SwiftUI

struct WelcomeScreenWithNavigation: View {
    
    var body: some View {
        NavigationView {
            WelcomeScreen().navigationBarTitle(Text("Welcome to Global Chat"))
        }
    }
}

struct WelcomeScreenWithNavigation_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeScreenWithNavigation()
    }
}
