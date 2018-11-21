package edu.spbu.ClientServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class Server extends Thread {
    public static final int PORT_NUMBER = 8080;

    protected Socket socket;

    private Server(Socket socket) {
        this.socket = socket;
        System.out.println("Новый клиент >> " + socket.getInetAddress().getHostAddress());
        start();
    }

    public void run() {
        InputStream in;
        DataOutputStream out;
        try {
            in = socket.getInputStream();
            out = new DataOutputStream(socket.getOutputStream());

            String request = get_str(in);
            String file_name = get_file(request);
            is_file(file_name);
            out.writeUTF(goodrequest(file_name));
            print_file(file_name, out);
            out.flush();

        }
        catch (IOException ex) {

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

    /*Возвращает имя файла по запросу*/
    public static String get_file(String header) throws IOException {
        // ищем URI, указанный в HTTP запросе
        // URI ищется только для методов POST и GET, иначе возвращается null
        String URI = extract(header, "GET ", " "), path;
        if(URI == null) URI = extract(header, "POST ", " ");
        if(URI == null) return "-1";

        // если URI записан вместе с именем протокола
        // то удаляем протокол и имя хоста
        path = URI.toLowerCase();
        if(path.indexOf("http://", 0) == 0)
        {
            URI = URI.substring(7);
            URI = URI.substring(URI.indexOf("/", 0));
        }
        else if(path.indexOf("/", 0) == 0)
            URI = URI.substring(1); // если URI начинается с символа /, удаляем его

        // отсекаем из URI часть запроса, идущего после символов ? и #
        int i = URI.indexOf("?");
        if(i > 0) URI = URI.substring(0, i);
        i = URI.indexOf("#");
        if(i > 0) URI = URI.substring(0, i);

        // конвертируем URI в путь до документов
        // предполагается, что документы лежат там же, где и сервер
        // иначе ниже нужно переопределить path
        path = "." + File.separator;
        char a;
        for(i = 0; i < URI.length(); i++)
        {
            a = URI.charAt(i);
            if(a == '/')
                path = path + File.separator;
            else
                path = path + a;
        }

        if(path.equals(".\\"))
            return "index.html";
        return path;
    }

    // "вырезает" из строки str часть, находящуюся между строками start и end
    // если строки end нет, то берётся строка после start
    // если кусок не найден, возвращается null
    // для поиска берётся строка до "\n\n" или "\r\n\r\n", если таковые присутствуют
    protected static String extract(String str, String start, String end)
    {
        int s = str.indexOf("\n\n", 0), e;
        if(s < 0) s = str.indexOf("\r\n\r\n", 0);
        if(s > 0) str = str.substring(0, s);
        s = str.indexOf(start, 0)+start.length();
        if(s < start.length()) return null;
        e = str.indexOf(end, s);
        if(e < 0) e = str.length();
        return (str.substring(s, e)).trim();
    }

    /*Проверяем существует ли запрошенный файл*/
    public static boolean is_file(String filename)
    {
        // если файл существует и является директорией,
        // то ищем индексный файл index.html
        File f = new File(filename);
        boolean flag = !f.exists();
        if (!flag) if (f.isDirectory()) {
            if (filename.lastIndexOf("" + File.separator) == filename.length() - 1)
                filename = filename + "index.html";
            else
                filename = filename + File.separator + "index.html";
            f = new File(filename);
            flag = !f.exists();
        }
        return !flag;

    }

    /*Предоставление файла по имени*/

    public static void print_file(String filename, DataOutputStream out) throws IOException {
        byte buf[] = new byte[64*1024];
        FileInputStream fis = new FileInputStream(filename);
        int r = 1;
        while(r > 0)
        {
            r = fis.read(buf);
            if(r > 0) out.write(buf, 0, r);
        }
        fis.close();
    }


    public static String goodrequest(String file_name)
    {
        File f = new File(file_name);
        // первая строка ответа
        String response = "HTTP/1.1 200 OK\n";

        // дата создания в GMT
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // время последней модификации файла в GMT
        response = response + "Last-Modified: " + df.format(new Date(f.lastModified())) + "\n";

        // длина файла
        response = response + "Content-Length: " + f.length() + "\n";

        // строка с MIME кодировкой
        response = response + "Content-Type: text/html\n";

        // остальные заголовки
        response = response
                + "Connection: close\n"
                + "Server: SimpleWEBServer\n\n";
        return response;
    }

    /**
     * Возвращает строку из запроса     */
    public static String get_str(InputStream input) throws IOException {
        byte buf[] = new byte[64*1024];
        return new String(buf, 0, input.read(buf));
    }


}