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
import java.util.concurrent.*;

/**
 *
 */
public class MessengerServer {
    private static final int BUFSIZE = 32;

    public void start() throws IOException {

        int servPort = 1025;

        ServerSocket serverSocket = new ServerSocket(servPort);

        ExecutorService executor = Executors.newFixedThreadPool(5);


        while (true) {
            Socket clntSock = serverSocket.accept();

            SocketAddress clientAddress = clntSock.getRemoteSocketAddress();
            System.out.println("Handling client at " + clientAddress);

            executor.submit(new ServerThread(clntSock));
        }
    }

    public class ServerThread extends Thread {
        boolean isRunning;
        private Socket clntSock;

        ServerThread(Socket clntSock) {
            this.clntSock = clntSock;
        }

        public void run() {
            isRunning = true;
            int recvMsgSize;
            byte[] recieveBuf = new byte[BUFSIZE];

            try {
                InputStream in = clntSock.getInputStream();
                OutputStream out = clntSock.getOutputStream();
                PrintStream os = new PrintStream(out);
                System.out.println("Started");

//                while (isRunning) {
                    while ((recvMsgSize = in.read(recieveBuf)) != -1) {
                        System.out.println("recieved: " + new String(recieveBuf, 0, recvMsgSize));
                        os.println("new data");
                        out.write(recieveBuf, 0, recvMsgSize);
                    }
                    clntSock.close();
//                }
            } catch (IOException e) {
                isRunning = false;
                e.printStackTrace();
            } finally {
                //IoUtil.closeQuietly(serverSocket);
            }
        }
    }

    /*static Logger log = LoggerFactory.getLogger(MessengerServer.class);

    private int port = 1025;
    private int poolSize = 5;




    private ExecutorService executor;

    private volatile boolean isRunning;

    public MessengerServer() {
        System.out.println("POST_CONSTRUCT");
        System.out.println("Init server on port: " + port);
        System.out.printf("PoolSize: " + poolSize + "\n");
        executor = Executors.newFixedThreadPool(poolSize);
    }


    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }


    public void start() {

        isRunning = true;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Started, waiting for connection");
            while (isRunning) {
                Socket socket = serverSocket.accept();

                System.out.println("Accepted. " + socket.getInetAddress());
                executor.submit(new MessengerServer.Worker(socket));
            }
        } catch (IOException e) {
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
                iter++;
            }
        }
    */
}
