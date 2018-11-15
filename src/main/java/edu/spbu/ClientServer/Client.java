package edu.spbu.ClientServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

    public static void main(String args[]) {
        String host = "www.ru";
        int port = 80;
        new Client(host, port);
    }

    public Client(String host, int port) {
        try {

            System.out.println("Соединение " + host + ":" + port + ".");

            Socket echoSocket;
            PrintWriter out;
            BufferedReader in;

            echoSocket = new Socket(host, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

                //System.out.print("Клиент: ");
                //String userInput = stdIn.readLine();
                String userInput = "GET / HTTP/1.1\n" +"Host: "+host+":"+"\n\n";
                out.println(userInput);
                String line;
                do {
                    line = in.readLine();
                    System.out.println(line);
                }
                while (line != null);


            out.close();
            in.close();
            stdIn.close();
            echoSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
