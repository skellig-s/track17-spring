package track.msgtest.messenger.messages;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alex on 18.05.17.
 */
public class CreateChatMessage extends Message {
    List<Long> idsFromChat = new LinkedList<>();

    public CreateChatMessage() {
        this.type = Type.MSG_CHAT_CREATE;
    }

    public void setIdsFromChat(List<Long> idsFromChat) {
        this.idsFromChat = idsFromChat;
    }

    public List<Long> getIdsFromChat() {
        return idsFromChat;
    }
}
