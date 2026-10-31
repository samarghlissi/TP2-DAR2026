package objets;

 
import java.io.*;
import java.net.*;
 
public class Serveurobjet {
    public static final int PORT = 1234;
 
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur (objets) en attente sur le port " + PORT + "...");
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
             // flux de sortie créé et vidé en premier pour éviter un blocage de l'en-tête
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
            out.flush();
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
 
            while (true) {
                Object recu = in.readObject();                    // c) réception de l'objet
                if (recu instanceof String && recu.equals("FIN")) break;
                if (!(recu instanceof Operation)) {
                    out.writeObject("Erreur : objet non reconnu");
                    out.flush();
                    continue;
                }
                Operation op = (Operation) recu;
                // d) récupération des données
                System.out.println("Reçu : " + op);
                // e) calcul
                String resultat = calculer(op);
                System.out.println("Résultat envoyé : " + resultat);
                // f) envoi du résultat
                out.writeObject(resultat);
                out.flush();
            }
            System.out.println("Client déconnecté.");
        } catch (EOFException e) {
            System.out.println("Client déconnecté.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
 
    private static String calculer(Operation op) {
        double a = op.getOperande1(), b = op.getOperande2(), r;
        switch (op.getOperateur()) {
            case '+': r = a + b; break;
            case '-': r = a - b; break;
            case '*': r = a * b; break;
            case '/':
                if (b == 0) return "Erreur : division par zéro";
                r = a / b;
                break;
            default: return "Erreur : opérateur inconnu";
        }
        return (r == Math.rint(r) && !Double.isInfinite(r)) ? String.valueOf((long) r) : String.valueOf(r);
    }
}
