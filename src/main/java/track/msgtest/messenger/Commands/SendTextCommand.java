package track.msgtest.messenger.Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;
import track.msgtest.messenger.net.SessionKeeper;
import track.msgtest.messenger.store.DBstore;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 12.05.17.
 */
public class SendTextCommand implements Command {
    static Logger log = LoggerFactory.getLogger(Session.class);
    static DBstore dbstore = new DBstore();

    public void execute(Session session, Message message) throws CommandException {

        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        long chatId = textMessage.getChatId();

        List<Long> userIds = dbstore.getUsersIdByChatId(chatId);
        userIds.remove(session.getUser().getId());

        for (long id : userIds) {
            try {
                SessionKeeper.sessionMap.get(id).send(textMessage);
            } catch (IOException | ProtocolException ex) {
                log.error("cant send message " + ex);
            }
        }

        //TODO: finish it
    }
}
