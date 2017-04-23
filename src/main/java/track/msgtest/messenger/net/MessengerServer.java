package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 *
 */
public class MessengerServer {
    private static final int BUFSIZE = 32 * 1024;
    static Logger log = LoggerFactory.getLogger(MessengerServer.class);
    private static int POOLSIZE = 2;
    private volatile boolean isRunning;
    ExecutorService executor = null;

    volatile ServerThread[] threads = new ServerThread[POOLSIZE];

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

        ServerThread(Socket clntSock) {
            this.clntSock = clntSock;
        }

        @Override
        public void run() {
            isRunning = true;
            int recvMsgSize;
            byte[] recieveBuf = new byte[BUFSIZE];

            try {
                in = clntSock.getInputStream();
                out = clntSock.getOutputStream();
                log.info("Started");

                while (!Thread.currentThread().isInterrupted()) {
                    recvMsgSize = in.read(recieveBuf);
                    System.out.println("recieved: " + new String(recieveBuf, 0, recvMsgSize));
                    for (ServerThread t : threads) {
                        if ((t != null) && (t != this)) {
                            t.out.write(recieveBuf, 0, recvMsgSize);
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
