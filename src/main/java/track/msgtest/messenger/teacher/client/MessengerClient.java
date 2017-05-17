package track.msgtest.messenger.teacher.client;

import java.io.*;

import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.lf5.viewer.LogFactor5InputDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.TextMessage;
import track.msgtest.messenger.messages.Type;
import track.msgtest.messenger.net.Protocol;
import track.msgtest.messenger.net.ProtocolException;
import track.msgtest.messenger.net.StringProtocol;


/**
 *
 */
public class MessengerClient {


    /**
     * Механизм логирования позволяет более гибко управлять записью данных в лог (консоль, файл и тд)
     * */
    static Logger log = LoggerFactory.getLogger(MessengerClient.class);

    /**
     * Протокол, хост и порт инициализируются из конфига
     *
     * */
    private Protocol protocol;
    private int port;
    private String host;

    /**
     * С каждым сокетом связано 2 канала in/out
     */
    private InputStream in;
    private OutputStream out;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void initSocket() throws IOException {
        Socket socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        oos = new ObjectOutputStream(out);
        ois = new ObjectInputStream(in);

        /*
      Тред "слушает" сокет на наличие входящих сообщений от сервера
     */
        Thread socketListenerThread = new Thread(() -> {
            final byte[] buf = new byte[1024 * 64];
            log.info("Starting listener thread...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Здесь поток блокируется на ожидании данных
                    Message msg = (Message) ois.readObject();
//                    if (read > 0) {

                        // По сети передается поток байт, его нужно раскодировать с помощью протокола
//                        Message msg = protocol.decode(Arrays.copyOf(buf, read));
                        onMessage(msg);
//                    }
                } catch (Exception e) {
                    log.error("Failed to process connection: {}", e);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        socketListenerThread.start();
    }

    /**
     * Реагируем на входящее сообщение
     */
    public void onMessage(Message msg) {
        log.info("Message received: {}", msg);
    }

    /**
     * Обрабатывает входящую строку, полученную с консоли
     * Формат строки можно посмотреть в вики проекта
     */
    public void processInput(String line) throws IOException, ProtocolException {
        String[] tokens = line.split(" ");
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];
        switch (cmdType) {
            case "/login":
                LoginMessage loginMessage = new LoginMessage(tokens[1], tokens[2]);
                send(loginMessage);
                break;
            case "/help":
                printHelp();
                break;
            case "/text":
                StringBuilder textToSend = new StringBuilder();
                TextMessage sendMessage = new TextMessage();
//                sendMessage.setSenderId();
                sendMessage.setType(Type.MSG_TEXT);
                sendMessage.setChatId(Integer.parseInt(tokens[1]));
                for (int i = 2; i < tokens.length; i++) {
                    textToSend.append(tokens[i]).append(" ");
                }
                sendMessage.setText(textToSend.toString().trim());
                send(sendMessage);
                break;
            // TODO: implement another types from wiki

            default:
                log.error("Invalid input: " + line);
        }
    }

    /**
     * Отправка сообщения в сокет клиент -> сервер
     */
    public void send(Message msg) throws IOException, ProtocolException {
        log.info(msg.toString());
        oos.writeObject(msg);
        oos.flush(); // принудительно проталкиваем буфер с данными
    }

    public static void main(String[] args) throws Exception {

        MessengerClient client = new MessengerClient();
        client.setHost("localhost");
        client.setPort(1025);
        client.setProtocol(new StringProtocol());

        try {
            client.initSocket();

            // Цикл чтения с консоли
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("$");
                String input = scanner.nextLine();
                if ("q".equals(input)) {
                    return;
                }
                try {
                    client.processInput(input);
                } catch (ProtocolException | IOException e) {
                    log.error("Failed to process user input", e);
                }
            }
        } catch (Exception e) {
            log.error("Application failed.", e);
        } finally {
            if (client != null) {
                // TODO
//                client.close();
            }
        }
    }

    public void printHelp() {
        System.out.println("Enter /login to log in and start chatting");
    }
}