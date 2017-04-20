package track.msgtest.messenger.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 */
public class MessengerServer {
    private static final int BUFSIZE = 32 * 1024;
    static Logger log = LoggerFactory.getLogger(MessengerServer.class);
    private static int POOLSIZE = 5;
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
                System.out.println("Accepted. " + clntSock.getRemoteSocketAddress()+" starting new Thread...");
                executor.submit(new ServerThread(clntSock));
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
            for (ServerThread t : threads) {
                if (t == null) {
                    t = this;
                    break;
                }
            }
        }

        @Override
        public void run() {
            isRunning = true;
            int recvMsgSize;
            byte[] recieveBuf = new byte[BUFSIZE];

            try {
                in = clntSock.getInputStream();
                out = clntSock.getOutputStream();
                System.out.println("Started");

//                clientThread[] threads = this.threads;

                while (!Thread.currentThread().isInterrupted()) {
//                    while ((recvMsgSize = in.read(recieveBuf)) != -1) {
                        recvMsgSize = in.read(recieveBuf);
                        System.out.println("recieved: " + new String(recieveBuf));
//                        os.println("new data");
                    for (ServerThread t : threads) {
                        if ((t != null)&&(t != this)) {
                            t.out.write(recieveBuf, 0, recvMsgSize);
                        }
                    }
//                        out.write(recieveBuf, 0, recvMsgSize);
//                    }
                }
                clntSock.close();
            } catch (Exception e) {
                isRunning = false;
                log.error(String.format("Error on connection %s:%d", clntSock.getInetAddress(), clntSock.getPort()), e);
            } finally {

                //IoUtil.closeQuietly(serverSocket);
            }
        }
    }

    /*

    class Worker extends Thread {

        private Socket socket;

        public Worker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            int iter = 0;
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("iteration: " + iter);
                try (InputStream in = socket.getInputStream();
                     OutputStream out = socket.getOutputStream()) {
                    System.out.println("try started");
                    byte[] buf = new byte[32 * 1024];
                    int readBytes = in.read(buf);
                    String line = new String(buf, 0, readBytes);
                    System.out.printf("Client>%s\n", line);
                    out.write(line.getBytes());
                    out.flush();
                } catch (Exception e) {
                    log.error(String.format("Error on connection %s:%d", socket.getInetAddress(), socket.getPort()), e);
                    return;
                }
            }
        }
    */
}
