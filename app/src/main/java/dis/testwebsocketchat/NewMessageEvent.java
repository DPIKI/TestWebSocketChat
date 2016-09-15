package dis.testwebsocketchat;

/**
 * Created by Lenovo on 15.09.2016.
 */
public class NewMessageEvent {
    public String user;
    public String message;
    public String time;

    public NewMessageEvent(String user, String message, String time) {
        this.user = user;
        this.message = message;
        this.time = time;
    }
}
