import UIKit
import SwiftUI
import Shared

struct ComposeView: UIViewControllerRepresentable {
    let rootComponent: RootComponent
    let backDispatcher: BackDispatcher

    func makeUIViewController(context: Self.Context) -> UIViewController {
        MainViewControllerKt.MainViewController(rootComponent: rootComponent, backDispatcher: backDispatcher)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    let rootComponent: RootComponent
    let backDispatcher: BackDispatcher


    var body: some View {
        ComposeView(rootComponent: rootComponent, backDispatcher: backDispatcher)
            .ignoresSafeArea()
    }
}
