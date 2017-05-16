package track.msgtest.messenger.Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.StatusMessage;
import track.msgtest.messenger.net.Session;
import track.msgtest.messenger.net.SessionKeeper;
import track.msgtest.messenger.store.DBstore;

import java.util.Objects;

/**
 * Created by alex on 12.05.17.
 */
public class LoginCommand implements Command {
    private Logger log = LoggerFactory.getLogger(Session.class);
    private long userNumber = 0;

    public void execute(Session session, Message message) throws CommandException {
        LoginMessage loginMessage = (LoginMessage) message;
        String name = loginMessage.getName();
        String pass = loginMessage.getPass();

        User newUser = new User(name, pass);

        //TODO: implement adder to userstore

        DBstore dbstore = new DBstore();

        User createduser = dbstore.getUser(name, pass);
        if (Objects.equals(createduser, null)) {
            dbstore.addUser(newUser);
            createduser = dbstore.getUser(name, pass);
        }

        session.setUser(createduser);
        System.out.println(String.format("setted: id: %d name: %s, pass: %s", createduser.getId(), createduser.getName(), createduser.getPass()));

        try {
            Session addedSession = SessionKeeper.sessionMap.put(createduser.getId(), session);
            session.send(new StatusMessage("Success"));
        } catch (Exception ex) {
//            session.send(new StatusMessage("error"));
            log.error("cant add session to map" + ex);
        }

    }
}
