package th.mfu;

import java.io.*;
import java.net.*;

public class MockWebServer implements Runnable {

    private int port;

    public MockWebServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mock Web Server running on port " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.isEmpty()) break; // End of HTTP headers
                }

                String response = "HTTP/1.1 200 OK\r\n" +
                                  "Content-Type: text/html\r\n\r\n" +
                                  "<html><body>Hello, Web! on Port " + port + "</body></html>";
                out.println(response);

                clientSocket.close();
            }

        } catch (IOException e) {
            System.err.println("Error in MockWebServer on port " + port);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread server1 = new Thread(new MockWebServer(8080));
        server1.start();

        Thread server2 = new Thread(new MockWebServer(8081));
        server2.start();

        // Info message only — the threads will keep running until manually stopped
        System.out.println("Servers are running on ports 8080 and 8081.");
        System.out.println("Open browser to http://localhost:8080 or http://localhost:8081");

        // Optional: Keep main thread alive indefinitely
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
