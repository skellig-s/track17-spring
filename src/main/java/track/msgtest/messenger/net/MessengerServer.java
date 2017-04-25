package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;
import track.msgtest.messenger.User;
import track.msgtest.messenger.messages.LoginMessage;
import track.msgtest.messenger.messages.Message;
import track.msgtest.messenger.messages.Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.*;

import static track.msgtest.messenger.messages.Type.MSG_LOGIN;

/**
 *
 */
public class MessengerServer {
    private static final int BUFSIZE = 32 * 1024;
    private static int POOLSIZE = 2;
    static Logger log = LoggerFactory.getLogger(MessengerServer.class);
    private volatile boolean isRunning;
    ExecutorService executor = null;
    Protocol protocol = new StringProtocol();

    private volatile ServerThread[] threads = new ServerThread[POOLSIZE];
    public volatile HashMap<Session, Chat> sessionMap = new HashMap<>(POOLSIZE);

    public MessengerServer() {
        this.executor = Executors.newFixedThreadPool(POOLSIZE);
    }

    public void start() throws IOException {

        int servPort = 1025;
        ServerSocket serverSocket = null;
        isRunning = true;

        try {
            serverSocket = new ServerSocket(servPort);
            System.out.println("Started, waiting for connection");
            while (isRunning) {
                Socket clntSock = serverSocket.accept();
                System.out.println("Accepted. " + clntSock.getRemoteSocketAddress()+ " starting new Thread...");
                ServerThread newThread = new ServerThread(clntSock);
                for (int i = 0; i < threads.length; i++) {
                    if (threads[i] == null) {
                        threads[i] = newThread;
                        System.out.println("new thread added to array");
                        break;
                    }
                }
                executor.submit(newThread);
            }
        } catch (IOException e) {
            isRunning = false;
            e.printStackTrace();
        } finally {
            //IoUtil.closeQuietly(serverSocket);
        }

    }
    public void destroy() throws Exception {
        isRunning = false;
        if (executor != null) {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
    }

    public class ServerThread extends Thread {
        boolean isRunning;
        private Socket clntSock;

        InputStream in = null;
        OutputStream out = null;

        ServerThread(Socket clntSock) throws IOException {
            this.clntSock = clntSock;
            in = clntSock.getInputStream();
            out = clntSock.getOutputStream();
        }

        @Override
        public void run() {
            isRunning = true;
            int recvMsgSize;
            byte[] recieveBuf = new byte[BUFSIZE];

            try {
                log.info("Started");
                while (!Thread.currentThread().isInterrupted()) {
                    recvMsgSize = in.read(recieveBuf);
                    if (recvMsgSize > 0) {
                        Message message = protocol.decode(Arrays.copyOf(recieveBuf, recvMsgSize));
                        if (message.getType() == MSG_LOGIN) {
                            LoginMessage loginMessage = (LoginMessage) message;
                            User newUser = new User(loginMessage.getName(), loginMessage.getPass());
                            Session session = new Session(newUser, clntSock, protocol);

                        }
                        System.out.println("recieved: " + message);
                        for (ServerThread t : threads) {
                            if ((t != null) && (t != this)) {
                                t.out.write(recieveBuf, 0, recvMsgSize);
                            }
                        }
                    }
                }
                clntSock.close();
            } catch (Exception e) {
                isRunning = false;
                log.error(String.format("Error on connection %s:%d", clntSock.getInetAddress(), clntSock.getPort()), e);
            } finally {
                for (int i = 0; i < threads.length; i++) {
                    if (this == threads[i]) {
                        threads[i] = null;
                        System.out.println("thread spot cleared");
                    }
                }
            }
        }
    }

}
