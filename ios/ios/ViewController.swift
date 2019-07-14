//
//  ViewController.swift
//  ios
//
//  Created by Felipe Costa on 14/06/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import UIKit
import client

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        let chatViewModel = ChatInjector.init().createViewModel()
        chatViewModel.start()
        
        sleep(5)
        chatViewModel.sendMessage(message: CoreMessage(author: "test", message: "message"))

        chatViewModel.onChat = { (message) -> KotlinUnit in
            print(">>> messge: \(message)")
            return KotlinUnit()
        }
        
        chatViewModel.showFailureMessage = { (showFailureMessage) -> KotlinUnit in
            print(">>> showFailureMessage: \(showFailureMessage)")
            return KotlinUnit()
        }
    }
}

