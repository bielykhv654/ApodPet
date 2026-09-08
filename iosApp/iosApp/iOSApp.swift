import SwiftUI
import Shared
@main
struct iOSApp: App {
    
    init(){
        InitIosKt.initialize( onKoinStart: { koinApp in koinApp})
        }
    @UIApplicationDelegateAdaptor(AppDelegate.self)
       var appDelegate: AppDelegate
    var body: some Scene {
        WindowGroup {
            ContentView(rootComponent: appDelegate.root, backDispatcher: appDelegate.backDispatcher)
        }
    }
}


class AppDelegate: NSObject, UIApplicationDelegate {
    var root: RootComponent!
    var backDispatcher: BackDispatcher = BackDispatcherKt.BackDispatcher()
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        
        root = KoinHelper().getRootComponentFactory().invoke(componentContext: DefaultComponentContext(lifecycle: ApplicationLifecycle() ,stateKeeper:nil, instanceKeeper: nil, backHandler:backDispatcher))
    
        
        
        return true
    }
}
