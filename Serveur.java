package chaines;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.net.*;

public class Serveur {
    public static final int PORT = 1234;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur en attente de connexion sur le port " + PORT + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connecté : " + socket.getInetAddress());
                new Thread(() -> traiterClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }

    private static void traiterClient(Socket socket) {
        try (Socket s = socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true)) {
        	
            String op1;
            while ((op1 = in.readLine()) != null) {
                if (op1.equalsIgnoreCase("FIN")) break;   
                String operateur = in.readLine();
                String op2 = in.readLine();
                if (operateur == null || op2 == null) break;

                System.out.println("Reçu : " + op1 + " " + operateur + " " + op2);
                String resultat = calculer(op1, operateur, op2);
                System.out.println("Résultat envoyé : " + resultat);
                out.println(resultat);
            }
            System.out.println("Client déconnecté.");
        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }

    private static String calculer(String s1, String op, String s2) {
        try {
            double a = Double.parseDouble(s1);
            double b = Double.parseDouble(s2);
            double r;
            switch (op) {
                case "+": r = a + b; break;
                case "-": r = a - b; break;
                case "*": r = a * b; break;
                case "/":
                    if (b == 0) return "Erreur : division par zéro";
                    r = a / b;
                    break;
                default: return "Erreur : opérateur inconnu";
            }
            return (r == Math.rint(r) && !Double.isInfinite(r)) ? String.valueOf((long) r) : String.valueOf(r);
        } catch (NumberFormatException e) {
            return "Erreur : opérande invalide";
        }
    }
}
