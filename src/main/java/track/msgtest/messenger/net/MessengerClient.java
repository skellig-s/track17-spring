package track.msgtest.messenger.net;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.server.ExportException;
import java.util.Arrays;

/**
 * Created by alex on 19.04.17.
 */
public class MessengerClient {

    static Socket socket = null;
    static InputStream in = null;
    static OutputStream out = null;

    public void init() throws IOException {

        String server = "localhost";
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int servPort = 1025;

        Socket socket = new Socket(server, servPort);

        int totalBytesRcvd = 0;
        byte[] recieveBuf = new byte[32];

        in = socket.getInputStream();
        out = socket.getOutputStream();

        new ClientListenThread().start();

        while (totalBytesRcvd < 5){

                String line = bufferedReader.readLine();
                if (line.equals("")) {
                    line = line.concat("\n");
                }
                byte[] data = line.getBytes();
                int bytesRcvd;
                out.write(data);
                out.flush();

            if ((bytesRcvd = in.read(recieveBuf)) == -1) {
                throw new SocketException("Connection closed prematurely");
            } else {
                final byte[] slice = Arrays.copyOfRange(recieveBuf, 0, bytesRcvd);
                totalBytesRcvd += 1;
                System.out.println("Received:" + new String(slice));
            }
//                System.out.println("bytesRcvd" + bytesRcvd);
//                System.out.println(totalBytesRcvd);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        socket.close();
    }

    class ClientListenThread extends Thread {
        byte[] recieveBuf = new byte[32*1024];
        int bytesRcvd = 0;
        boolean isActive = true;

        @Override
        public void run() {
            while (isActive) {
                try {
                    bytesRcvd = in.read(recieveBuf);
                    final byte[] slice = Arrays.copyOfRange(recieveBuf, 0, bytesRcvd);
                    System.out.println("Received:" + new String(slice));
                } catch (Exception e) {
                    isActive = false;
                }
            }
        }
    }

}
