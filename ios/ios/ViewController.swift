//
//  ViewController.swift
//  ios
//
//  Created by Felipe Costa on 14/06/19.
//  Copyright © 2019 Felipe Costa. All rights reserved.
//

import UIKit
import client

class ViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {

    @IBOutlet weak var collectionView: UICollectionView!
    
    @IBOutlet weak var usernameTextField: UITextField!
    @IBOutlet weak var messageTextField: UITextField!
    
    var chatViewModel: ChatViewModel?
    var messages: [CoreMessage] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        chatViewModel = ChatInjector.init().createViewModel()
        chatViewModel?.start()

        chatViewModel?.onChat = { (message) -> Void in
            print(">>> messge: \(message)")
            self.messages.append(message)
            let indexPath = IndexPath(row: self.messages.count - 1, section: 0)
            self.collectionView.insertItems(at: [indexPath])
        }
        
        chatViewModel?.showFailureMessage = { (showFailureMessage) -> Void in
            print(">>> showFailureMessage: \(showFailureMessage)")
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return messages.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "collectionViewCell", for: indexPath) as! CollectionViewCell
        
        cell.messageLabel.text = "\(messages[indexPath.row].author): \(messages[indexPath.row].message)"
        
        return cell
    }
    @IBAction func sendAction(_ sender: Any) {
        
        let author = usernameTextField.text ?? ""
        let message = messageTextField.text ?? ""
        chatViewModel?.sendMessage(message: CoreMessage(author: author, message: message))
        usernameTextField.text = nil
        messageTextField.text = nil
    }
}
