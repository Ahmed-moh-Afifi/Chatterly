//import com.example.chatterly.data.local.TokenManager;
//import com.example.chatterly.utils.Config;
//import com.microsoft.signalr.HubConnection;
//import com.microsoft.signalr.HubConnectionBuilder;
//import com.microsoft.signalr.HubConnectionState;
//
//import io.reactivex.Single;
//
//import java.util.function.Consumer;
//
//public class MessagingHub {
//    private final HubConnection hubConnection;
//
//    private MessagingHub(TokenManager tokenManager) {
//        String url = Config.api + "/MessagingHub";
//        this.hubConnection = HubConnectionBuilder.create(url)
//                .withAccessTokenProvider(Single.fromCallable(() -> {
//                    try {
//                        return tokenManager.getValidTokens().join().getAccessToken();
//                    } catch (Exception e) {
//                        throw new RuntimeException("Failed to get access token", e);
//                    }
//                }))
//                .build();
//    }
//
//    public void start() {
//        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
//            hubConnection.start().blockingAwait();
//        }
//    }
//
//    public void stop() {
//        hubConnection.stop().blockingAwait();
//    }
//
//    public void on(String methodName, Consumer<Object[]> handler) {
//        hubConnection.on(methodName, handler, Object[].class);
//    }
//
//    public void off(String methodName) {
//        hubConnection.remove(methodName);
//    }
//
//    public void invoke(String methodName, Object... arguments) {
//        hubConnection.send(methodName, arguments);
//    }
//
//    public boolean isRunning() {
//        return hubConnection.getConnectionState() == HubConnectionState.CONNECTED;
//    }
//}