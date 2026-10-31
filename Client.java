package chaines;


import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.*;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static final Pattern SYNTAXE =
        Pattern.compile("^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connecté au serveur. Tapez une opération (ex : 55 * 25) ou 'exit'.");
            while (true) {
                System.out.print("> ");
                String saisie = sc.nextLine().trim();

                if (saisie.equalsIgnoreCase("exit")) {
                    out.println("FIN");
                    break;
                }
                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe incorrecte. Format attendu : <nombre> <+|-|*|/> <nombre>");
                    continue;
                }
                out.println(m.group(1));   
                out.println(m.group(2));   
                out.println(m.group(3));  

                String resultat = in.readLine();
                System.out.println("Résultat : " + resultat);
            }
        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
