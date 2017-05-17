package track.msgtest.messenger.Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.CreateChatMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;
import track.msgtest.messenger.net.SessionKeeper;
import track.msgtest.messenger.store.DBstore;

import java.io.IOException;
import java.util.List;

/**
 * Created by alex on 18.05.17.
 */
public class CreateChatCommand implements Command {
    static Logger log = LoggerFactory.getLogger(Session.class);
    static DBstore dbstore = new DBstore();

    public void execute(Session session, Message message) throws CommandException {

        CreateChatMessage chatMessage = (CreateChatMessage) message;
        List<Long> getIdsFromChat = chatMessage.getIdsFromChat();

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
