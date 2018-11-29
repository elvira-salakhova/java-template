package edu.spbu.ClientServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {
    public static final int PORT_NUMBER = 8080;

    protected Socket socket;

    private Server(Socket socket) {
        this.socket = socket;
        System.out.println("Новый клиент:" + socket.getInetAddress().getHostAddress());
        start();
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String request;
            //String[] line = new String[5];
            while ((request = br.readLine()) != null) {
                System.out.println(request);
                request += '\n';
                out.write(request.getBytes());
            }
            //Scanner input = new Scanner(new File(line[1]));
            String s = "<html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>This is heading 1</h1>\n" +
                    "<h2>This is heading 2</h2>\n" +
                    "<h3>This is heading 3</h3>\n" +
                    "<h4>This is heading 4</h4>\n" +
                    "<h5>This is heading 5</h5>\n" +
                    "<h6>This is heading 6</h6>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: localhost\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            out.write(result.getBytes());
            out.flush();
        }
        catch (IOException ex) {

        }
        finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Сервер запущен");
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT_NUMBER);
            while (true) {
                new Server(server.accept());
            }
        } catch (IOException ex) {
            System.out.println("Сервер не запустился");
        } finally {
            try {
                if (server != null)
                    server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}