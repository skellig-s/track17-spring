package track.msgtest.messenger.net;

import java.io.*;

import java.net.Socket;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.Commands.CommandException;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;


/**
 * Сессия связывает бизнес-логику и сетевую часть.
 * Бизнес логика представлена объектом юзера - владельца сессии.
 * Сетевая часть привязывает нас к определнному соединению по сети (от клиента)
 */
public class Session implements Runnable {
    static Logger log = LoggerFactory.getLogger(Session.class);
    private static int BUFSIZE = 1024 * 16; // 16 kb

    private User user;
    private Socket socket;
    private Protocol protocol;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private volatile boolean isActive;
    private byte[] buffer = new byte[BUFSIZE]; // 16 kb

    public Session(User user, Socket socket, Protocol protocol) throws IOException {
        isActive = true;
        this.user = user;
        this.socket = socket;
        this.protocol = protocol;
        //Serialization
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void send(Message msg) throws ProtocolException, IOException {
        log.info(msg.toString());
        oos.writeObject(msg);
        oos.flush();
    }

    public void onMessage(Message msg) throws CommandException {
        Type msgType = msg.getType();
        SessionKeeper.commandMap.get(msgType).execute(this, msg);
    }

    public void close() {
        try{
            ois.close();
            oos.close();
            socket.close();
            SessionKeeper.sessionMap.remove(this.user.getId());
        } catch (IOException e) {
            log.error("cant close session" + e.getLocalizedMessage());
        }
        // TODO: complete
    }

    @Override
    public void run() {
//        SessionKeeper sessionKeeper = SessionKeeper.getInstance();

        while (isActive) {
            try {
                Message msg = (Message) ois.readObject();
                onMessage(msg);

            } catch (Exception e) {
                isActive = false;
                this.close();
                log.error("Session failed: ", e);
            }
        }
    }
}