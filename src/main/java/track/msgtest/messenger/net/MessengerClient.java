package track.msgtest.messenger.net;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

/**
 * Created by alex on 19.04.17.
 */
public class MessengerClient {

    public static void main(String[] args) throws IOException {

        String server = "localhost";

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));


        int servPort = 1025;

        Socket socket = new Socket(server, servPort);



        int totalBytesRcvd = 0;
        byte[] recieveBuf = new byte[32];

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        while (totalBytesRcvd < 5){
//            try (InputStream in = socket.getInputStream();
//                 OutputStream out = socket.getOutputStream()){

                String line = bufferedReader.readLine();

                byte[] data = line.getBytes();
                int bytesRcvd;
                out.write(data);
                out.flush();

                bytesRcvd = in.read(recieveBuf);
                final byte[] slice = Arrays.copyOfRange(recieveBuf, 0, bytesRcvd);

                totalBytesRcvd += 1;

                System.out.println("Received:" + new String(slice));
//                System.out.println("bytesRcvd" + bytesRcvd);
//                System.out.println(totalBytesRcvd);
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
        }

        socket.close();
    }
}
