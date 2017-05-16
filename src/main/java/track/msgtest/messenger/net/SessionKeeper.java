package track.msgtest.messenger.net;

import java.util.HashMap;

import track.msgtest.messenger.Commands.*;
import track.msgtest.messenger.messages.Type;

/**
 * Created by alex on 12.05.17.
 */
public class SessionKeeper {
    private static SessionKeeper ourInstance = new SessionKeeper();

    public static volatile HashMap<Long, Session> sessionMap = new HashMap<>();
    public static HashMap<Type, Command> commandMap = new HashMap<>();

    public void addSessionToMap(Long number, Session session) {
//        sessionMap
    }

    static {
        commandMap.put(Type.MSG_LOGIN, new LoginCommand());
        commandMap.put(Type.MSG_TEXT, new SendTextCommand());

    }

    public static SessionKeeper getInstance() {
        return ourInstance;
    }

    private SessionKeeper() {
    }
}
