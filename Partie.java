import java.util.*;

/**
 * Boucle principale d’un Black-Jack simplifié :
 * – un sabot mélangé à chaque manche
 * – aucune gestion du split / assurance
 * – Blackjack payé 3:2
 *
 * Hypothèses :
 *   • Joueur possède nouvelleMain(), miser(), recevoirCarte(), jouerTour(), etc.
 *   • Banque expose nouvelleMain(), recevoirCarte(Carte) et getMain()
 *     où getMain() renvoie un objet ayant valeurTotale(), estBuste(), aBlackjack(), getCartes().
 */
public class Partie {

    /* --- État de la partie -------------------------------------------- */
    private final List<Joueur> joueurs;   // participants encore solvables
    private final Banque banque;          // croupier
    private Paquet paquet;                // sabot courant
    private final Scanner in;             // entrée unique pour tout le programme

    /* --- Construction -------------------------------------------------- */
    public Partie(List<Joueur> joueurs) {
        this.joueurs = joueurs;
        this.banque  = new Banque();
        this.in      = new Scanner(System.in);
        this.paquet  = new Paquet();
    }

    /* --- Boucle principale -------------------------------------------- */
    public void demarrer() {
        boolean continuer = true;

        while (continuer && !joueurs.isEmpty()) {

            preparerNouvelleManche();

            if (!demanderMises()) {                      // personne ne mise ?
                System.out.println("Aucune mise – fin de la partie.");
                break;
            }

            distribuerCartesInitiales();
            afficherMainsInitiales();

            boolean banqueBJ = verifierBlackjacksInitiaux();
            if (!banqueBJ) {                             // sinon la banque gagne d’emblée
                tourJoueurs();
                tourBanque();
            }

            evaluerResultats();
            afficherSoldes();

            System.out.print("\nNouvelle manche ? (o/n) ");
            continuer = in.nextLine().trim().equalsIgnoreCase("o");
        }

        System.out.println("\nMerci d’avoir joué !");
    }

    /* ---------- ÉTAPE 1 : réinitialisations --------------------------- */
    private void preparerNouvelleManche() {
        System.out.println("\n================= NOUVELLE MANCHE =================");
        paquet = new Paquet();
        paquet.melanger();
        banque.nouvelleMain();

        joueurs.removeIf(j -> j.getSolde() <= 0);     // élimine les joueurs fauchés
        joueurs.forEach(Joueur::nouvelleMain);        // vide les mains restantes
    }

    /* ---------- ÉTAPE 2 : mises --------------------------------------- */
    private boolean demanderMises() {
        boolean auMoinsUneMise = false;

        for (Joueur j : joueurs) {
            double mise = 0;

            if (j instanceof JoueurHumain) {          // saisie au clavier
                System.out.printf("%s (solde %.2f €) – mise (0 pour passer) : ",
                        j.getNom(), j.getSolde());
                try { mise = Double.parseDouble(in.nextLine()); }
                catch (NumberFormatException e) { mise = -1; }
            } else {                                  // robot : mise aléatoire ≤ 50 €
                double plafond = Math.min(50, j.getSolde());
                if (plafond > 0) {
                    mise = 1 + new Random().nextInt((int) plafond);
                    System.out.printf("%s mise %.2f €%n", j.getNom(), mise);
                }
            }

            if (mise > 0 && j.miser(mise)) auMoinsUneMise = true;
        }
        return auMoinsUneMise;
    }

    /* ---------- ÉTAPE 3 : distribution initiale ----------------------- */
    private void distribuerCartesInitiales() {
        for (int i = 0; i < 2; i++) {
            for (Joueur j : joueurs) j.recevoirCarte(paquet.tirerCarte());
            banque.recevoirCarte(paquet.tirerCarte());
        }
    }

    /* ---------- ÉTAPE 4 : affichage initial --------------------------- */
    private void afficherMainsInitiales() {
        System.out.printf("\nBanque : [%s, CARTE CACHÉE]%n",
                banque.getMain().getCartes().get(0));
        joueurs.forEach(j ->
                System.out.printf("%s : %s%n", j.getNom(), j));
    }

    /* ---------- ÉTAPE 5 : blackjacks initiaux ------------------------- */
    private boolean verifierBlackjacksInitiaux() {
        boolean banqueBJ = banque.getMain().aBlackjack();
        if (banqueBJ) System.out.println(">>> La banque a BLACKJACK !");

        for (Joueur j : joueurs)
            if (j.aBlackjack())
                System.out.printf(">>> %s a BLACKJACK !%n", j.getNom());

        return banqueBJ;
    }

    /* ---------- ÉTAPE 6 : tour des joueurs ---------------------------- */
    private void tourJoueurs() {
        for (Joueur j : joueurs) {
            System.out.printf("%n----- Tour de %s -----%n", j.getNom());
            j.jouerTour(paquet);
        }
    }

    /* ---------- ÉTAPE 7 : tour de la banque --------------------------- */
    private void tourBanque() {
        System.out.println("\n----- Tour de la banque -----");
        while (banque.getMain().valeurTotale() < 17)
            banque.recevoirCarte(paquet.tirerCarte());
        System.out.println(banque);
    }

    /* ---------- ÉTAPE 8 : évaluation ---------------------------------- */
    private void evaluerResultats() {
        int  vBanque   = banque.getMain().valeurTotale();
        boolean bust   = banque.getMain().estBuste();

        System.out.println("\n-------------- RÉSULTATS --------------");
        for (Joueur j : joueurs) {
            int v = j.valeurTotale();

            String verdict;
            double facteur;

            if (j.estBuste())      { verdict = "perdu";  facteur = 0; }
            else if (bust)         { verdict = "gagné";  facteur = 2; }
            else if (v > vBanque)  { verdict = "gagné";  facteur = 2; }
            else if (v < vBanque)  { verdict = "perdu";  facteur = 0; }
            else                   { verdict = "push";   facteur = 1; }

            // Blackjack naturel payé 3:2
            if (j.aBlackjack() && facteur == 2)
                facteur = 2.5;

            System.out.printf("%s : %s → %s%n", j.getNom(), j, verdict);

            if (facteur > 0) j.crediter(facteur);
        }
    }

    /* ---------- ÉTAPE 9 : soldes -------------------------------------- */
    private void afficherSoldes() {
        System.out.println("\nSoldes après la manche :");
        joueurs.forEach(j ->
                System.out.printf(" - %-10s : %.2f €%n", j.getNom(), j.getSolde()));
    }
}
