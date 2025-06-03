import java.util.*;

public class Paquet {

    private final List<Carte> cartes = new ArrayList<>();
    private final Stack<Carte> pile  = new Stack<>();

    /* ---------- construction ---------- */
    public Paquet() {
        initialiserPaquet(6);   // 6 jeux par défaut
        melanger();             // mélange + création de la pile
    }

    /* ---------- initialisation ---------- */
    private void initialiserPaquet(int nbPaquets) {
        String[] couleurs = {"♠", "♥", "♦", "♣"};
        String[] faces    = {"2", "3", "4", "5", "6", "7", "8", "9",
                "10", "Valet", "Dame", "Roi", "As"};
        cartes.clear();
        for (int n = 0; n < nbPaquets; n++)
            for (String c : couleurs)
                for (String f : faces)
                    cartes.add(new Carte(c, f));
    }

    /* ---------- mélange ---------- */
    public void melanger() {
        Collections.shuffle(cartes);
        reconstruirePile();     // ⭢ la pile contient toutes les cartes mélangées
    }

    private void reconstruirePile() {
        pile.clear();
        for (Carte c : cartes) pile.push(c);
    }

    /* ---------- tirage ---------- */
    public Carte tirerCarte() {
        if (pile.isEmpty()) {          // sabot épuisé → on recommence
            System.out.println("Le sabot est vide : reconstitution automatique.");
            initialiserPaquet(6);
            melanger();                // reconstruit aussi la pile
        }
        return pile.pop();
    }
}
