package dis.testwebsocketchat;

class RecyclerAdapterMessage {
    public String senderName;
    public String message;
    public String time;

    RecyclerAdapterMessage(String senderName, String message, String time){
        this.senderName = senderName;
        this.message = message;
        this.time = time;
    }

}
