import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientFP {
    // Create static fields for both methods to access
    public static String response = "";
    public static String GUINumber = "";
    public static BufferedReader reader = null;
    public static PrintWriter writer = null;
    
    // GUI method
    public static void constructGUI() {
        // Setup the frame
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create objects for the frame
        JTextField userInput = new JTextField();
        JLabel header = new JLabel("Enter a positive number to find the corresponding fibonacci number! Enter a negative number to shutdown the server.");
        JLabel enter = new JLabel("Enter a number:");
        JLabel answer = new JLabel("Fibonacci Number: ");
        JButton calc = new JButton("Calculate");
        JPanel userPanel = new JPanel();

        // Create the layouts for the GUI
        GridLayout mainLayout = new GridLayout(2, 1);
        GridLayout userLayout = new GridLayout(2, 2);

        // Set both layouts
        frame.setLayout(mainLayout);
        userPanel.setLayout(userLayout);

        // Add objects to the panel
        userPanel.add(enter);
        userPanel.add(userInput);
        userPanel.add(calc);
        userPanel.add(answer);

        // Creates an ActionListener for the calculate button
        calc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent x) {
                if (userInput.getText().length() > 0) {
                    // Writes the user input to the server
                    ClientFP.writer.println(userInput.getText().toString());
                    ClientFP.writer.flush();

                    // Tries to read the response
                    try {
                        ClientFP.response = ClientFP.reader.readLine();
                    }
                    catch (Exception y) {
                        System.out.println(y);
                    }

                    // Stores the response and number that the user entered for future use
                    ClientFP.GUINumber = userInput.getText().toString();
                    answer.setText(ClientFP.response);
                    userInput.setText("");
                }
            }
        });
        
        frame.add(header);
        frame.add(userPanel);
        frame.setTitle("Fibonacci Calculator");

        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {

        try {
            // Create and set fields
            boolean shutdown = false;
            int numRead = 0;
            Socket socket = new Socket("127.0.0.1", 80);
            ClientFP.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ClientFP.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Run the GUI
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    constructGUI();
                }
            });

            // While loop to prevent socket from closing until a negative number is entered
            while(!shutdown) {
                try {
                    numRead = Integer.parseInt(ClientFP.GUINumber);
                }
                catch (NumberFormatException x) {
                    ClientFP.GUINumber = "0";
                }
                if (numRead < 0) {
                    shutdown = true;
                }
            }
            socket.close();
        }
        catch (UnknownHostException x) {
            System.out.println("Failed to connect to server: " + x);
        }
        catch (IOException x) {
            System.out.println("Socket failed to open: " + x);
        }
    }
}
