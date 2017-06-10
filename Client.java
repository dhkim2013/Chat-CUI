/**
 * Created by Administrator on 2016-06-27.
 */

import javax.sound.midi.Receiver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

abstract class Help {
    abstract void showHelp();
}

interface Tutorial {
    public void showTutorial();
}

public class Client extends Help implements Tutorial{
    private String name;
    private Socket socket;

    void showHelp() {
        System.out.println("'help', 'exit'");
    }

    public void showTutorial() {
        System.out.println("Connect 127.0.0.1:8000\nYou can type 'help' to show command");
    }

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {

        try {
            socket = new Socket("127.0.0.1", 8000);

            System.out.print("Connected.\nInput name : ");

            name = new java.util.Scanner(System.in).next();

            showTutorial();
            DataReceiver receiver = new DataReceiver(socket);
            DataSender sender = new DataSender(socket);

            receiver.start();
            sender.start();
        }

        catch(IOException e) {
            e.printStackTrace();
        }

    }

    class DataReceiver extends Thread{
        Socket socket;
        DataInputStream in;

        public DataReceiver(Socket socket) {
            this.socket = socket;

            try {
                in = new DataInputStream(socket.getInputStream());
            }

            catch(IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {

            while(in != null) {

                try {
                    System.out.println(in.readUTF());
                }

                catch(IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    class DataSender extends Thread{
        Socket socket;
        DataOutputStream out;

        public DataSender(Socket socket) {
            this.socket = socket;

            try {
                out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF(name);
            }

            catch(IOException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            java.util.Scanner sc = new java.util.Scanner(System.in);
            String data = "";

            while (out != null) {
                try {
                    data = sc.nextLine();

                    if (data.equals("exit"))
                        System.exit(0);

                    else if (data.equals("help"))
                        showHelp();

                    else {
                        out.writeUTF("[" + name + "] : " + data);
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
