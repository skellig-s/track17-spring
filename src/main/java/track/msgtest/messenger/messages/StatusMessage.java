package track.msgtest.messenger.messages;

/**
 * Created by alex on 14.05.17.
 */
public class StatusMessage extends Message {
    private String content;

    public StatusMessage(String content) {
        setType(Type.MSG_STATUS);
        this.content = content;
    }

    @Override
    public String toString() {
        return "StatusMessage{" +
                "content='" + content + '\'' +
                '}';
    }
}
