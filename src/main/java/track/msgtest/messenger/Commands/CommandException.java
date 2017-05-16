package track.msgtest.messenger.Commands;

/**
 * Created by alex on 12.05.17.
 */
public class CommandException extends Exception {
    public CommandException(String msg) {
        super(msg);
    }

    public CommandException(Throwable ex) {
        super(ex);
    }
}
