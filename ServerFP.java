import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerFP {
    // Fibonacci method to calculate the number
    public static int fibonacci(int x) {
        int v1 = 0;
        int v2 = 1;
        int v3 = 0;
        int i = 0;

        if (x == 1) {
            return v1;
        }
        else if (x == 2) {
            return v2;
        }
        else {
            for (i = 2; i <= x; ++i) {
                v3 = v1 + v2;
                v1 = v2;
                v2 = v3;
            }
            return v3;
        }
    }
    public static void main(String[] args) {
        // Create required fields
        boolean shutdown = false;
        String input = "";
        String fibSend = "";
        int inputNum = 0;
        int fibNum = 0;
        
        try {
            // Sets up the server/socket connection and wraps the byte streams in the writer and reader objects
            ServerSocket server = new ServerSocket(80);
            Socket client = server.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));

            while(!shutdown) {

                // Reads input from client
                input = reader.readLine();
                System.out.println("Received: " + input);
                
                try  {
                    // Tries to parse the string to an integer
                    inputNum = Integer.parseInt(input);

                    // Shuts down the server if a negative input is received
                    if (inputNum < 0) {
                        shutdown = true;
                        writer.println("Negative number entered - Shutting Down...");
                        writer.flush();
                        System.out.println("Shutting down server...");
                    }
                    // Uses the fibonacci method with the client input to output the result to the writer
                    else {
                        fibNum = fibonacci(inputNum);
                        fibSend = "Fibonacci Number: " + fibNum;
                        writer.println(fibSend);
                        writer.flush();
                        System.out.println("Sent: " + fibSend);
                    }
                }
                // Catches non integer values
                catch (NumberFormatException x) {
                    writer.println("Please enter a valid number.");
                    writer.flush();
                    System.out.println("Non-integer input entered.");
                }
            }
            // Close client and server sockets
            client.close();
            server.close();
        }
        catch (IOException x) {
            System.out.println("Error creating server: " + x);
        }
    }
}
