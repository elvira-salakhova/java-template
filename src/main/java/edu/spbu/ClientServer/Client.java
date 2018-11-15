package edu.spbu.ClientServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

    public static void main(String args[]) {
        String host = "127.0.0.1";
        int port = 8080;
        new Client(host, port);
    }

    public Client(String host, int port) {
        try {
            String serverHostname = new String("127.0.0.1");

            System.out.println("Соединение " + serverHostname + " по порту " + port + ".");

            Socket echoSocket;
            PrintWriter out;
            BufferedReader in;

            echoSocket = new Socket(serverHostname, 8080);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("Клиент: ");
                String userInput = stdIn.readLine();
                if ("q".equals(userInput)) {
                    break;
                }
                out.println(userInput);
                System.out.println("Сервер: Вы сказали '" + in.readLine()+ "'?");
            }

            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
