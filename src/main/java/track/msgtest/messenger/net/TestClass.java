package track.msgtest.messenger.net;

/**
 * Created by alex on 19.04.17.
 */
public class TestClass {
    public static void main(String[] args) {
        MessengerServer messengerServer = new MessengerServer();
        try {
            messengerServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
