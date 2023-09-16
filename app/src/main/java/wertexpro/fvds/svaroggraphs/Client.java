package wertexpro.fvds.svaroggraphs;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.io.*;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.util.Arrays;

import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;

public class Client implements Runnable{

    private static String serverIP;
    private static int serverPort;

    @Override
    public void run() {

        try {
            long millis = System.currentTimeMillis();
            byte[] aByte = new byte[1];
            int bytesRead;
            /*URL whatismyip = new URL("https://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();*/
            serverIP = MainActivity.sp.getString(ToolsFragment.KEY_IP_SERVER, "");
            serverPort = Integer.parseInt(MainActivity.sp.getString(ToolsFragment.KEY_IP_PORT2, "9897"));
            Socket clientSocket = new Socket(serverIP, serverPort);
            InputStream is = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

            dos.writeUTF(1+"-"+"8.8.8.8");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (is != null) {
                long lenghtfile = dis.readLong();
                System.out.println("hi");
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                System.out.println("hi");
                try {
                    fos = new FileOutputStream(MainActivity.readFile(true));
                    bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(aByte, 0, aByte.length);
                    System.out.println("hi");
                    int counter = 0;
                    System.out.println(lenghtfile);
                    while (counter < (int)lenghtfile){
                        counter++;
                        baos.write(aByte);
                        bytesRead = is.read(aByte);
                        String str = new String(aByte);
                        System.out.println(str);
                    }


                    System.out.println("aaa");
                    bos.write(baos.toByteArray());
                    bos.flush();
                    is.close();
                    dos.close();
                    baos.flush();
                    baos.close();
                    fos.flush();
                    fos.close();
                    bos.close();
                    System.out.println("aaa");
                    clientSocket.close();
                } catch (IOException ex) {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }

            }

            byte[] aByte2 = new byte[1];
            int bytesRead2;

            Socket clientSocket2 = new Socket(serverIP, serverPort);
            InputStream is2 = clientSocket2.getInputStream();
            DataInputStream dis2 = new DataInputStream(clientSocket2.getInputStream());
            DataOutputStream dos2 = new DataOutputStream(clientSocket2.getOutputStream());


            dos2.writeUTF(2+"-"+"8.8.8.8");
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();

            if (is2 != null) {
                long lenghtfile2 = dis2.readLong();
                System.out.println("hi");
                FileOutputStream fos2 = null;
                BufferedOutputStream bos2 = null;
                System.out.println("hi");
                try {
                    fos2 = new FileOutputStream(MainActivity.readFile(false));
                    bos2 = new BufferedOutputStream(fos2);
                    bytesRead2 = is2.read(aByte2, 0, aByte2.length);
                    System.out.println("hi");
                    int counter2 = 0;
                    System.out.println(lenghtfile2);
                    while (counter2 < (int)lenghtfile2){
                        counter2++;
                        baos2.write(aByte2);
                        bytesRead2 = is2.read(aByte2);
                        String str2 = new String(aByte2);
                        System.out.println(str2);
                    }


                    System.out.println("aaa");
                    bos2.write(baos2.toByteArray());
                    bos2.flush();
                    is2.close();
                    dos2.close();
                    baos2.flush();
                    baos2.close();
                    fos2.flush();
                    fos2.close();
                    bos2.close();
                    System.out.println("aaa");
                    clientSocket2.close();
                    System.out.println(Math.floor(System.currentTimeMillis() - millis));
                    MainActivity.readFile(false).mkdir();
                    MainActivity.readFile(true).mkdir();
                } catch (IOException ex) {
                    System.out.println(Arrays.toString(ex.getStackTrace()));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data.txt").delete();
            new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "data2.txt").delete();
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }
}