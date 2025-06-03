import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);

        List<Joueur> joueurs = new ArrayList<>();
        joueurs.add(new JoueurHumain("Paul", 500, in));   // joueur réel
        joueurs.add(new JoueurRobot("Bot-42", 500));      // IA aléatoire

        Partie partie = new Partie(joueurs);              // la classe Partie
        partie.demarrer();

        in.close();
    }
}
