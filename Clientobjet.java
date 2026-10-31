package objets;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.*;
 
public class Clientobjet {
    private static final String HOST = "localhost";
    private static final int PORT = 1234;
    private static final Pattern SYNTAXE =
        Pattern.compile("^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");
    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             Scanner sc = new Scanner(System.in)) {
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

       
            System.out.println("Connecté au serveur. Tapez une opération (ex : 55 * 25) ou 'exit'.");
            while (true) {
                System.out.print("> ");
                String saisie = sc.nextLine().trim();
 
                if (saisie.equalsIgnoreCase("exit")) {
                    out.writeObject("FIN");
                    out.flush();
                    break;
                }
                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe incorrecte. Format attendu : <nombre> <+|-|*|/> <nombre>");
                    continue;
                }
                Operation op = new Operation(Double.parseDouble(m.group(1)),
                                             m.group(2).charAt(0),
                                             Double.parseDouble(m.group(3)));
                out.writeObject(op);
                out.flush();
                String resultat = (String) in.readObject();
                System.out.println("Résultat : " + resultat);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}
