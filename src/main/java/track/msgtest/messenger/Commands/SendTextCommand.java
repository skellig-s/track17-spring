package track.msgtest.messenger.Commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.Session;
import track.msgtest.messenger.net.SessionKeeper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by alex on 12.05.17.
 */
public class SendTextCommand implements Command {
    static Logger log = LoggerFactory.getLogger(Session.class);

    public void execute(Session session, Message message) throws CommandException {
        TextMessage textMessage = (TextMessage) message;
        String text = textMessage.getText();
        long chatId = textMessage.getChatId();

        try {
            SessionKeeper.sessionMap.get(chatId).send(textMessage);
        } catch (IOException | ProtocolException ex) {
            log.error("cant send message " + ex);
        }

        //TODO: finish it
    }
}
