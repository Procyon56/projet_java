/* ===== Joueur.java (abstraite) ===== */
import java.util.*;

public abstract class Joueur {

    /* --- identité & argent --- */
    protected final String nom;
    protected double      solde;

    /* --- main courante (ex-MainJoueur) --- */
    protected final List<Carte> cartes = new ArrayList<>();
    protected double  mise  = 0;
    protected boolean stand = false;

    /* --- construction --- */
    public Joueur(String nom, double soldeInitial) {
        if (soldeInitial < 0) throw new IllegalArgumentException("Solde négatif");
        this.nom   = nom;
        this.solde = soldeInitial;
    }

    /* ---------- gestion de mise / cartes ---------- */
    public boolean peutMiser(double montant)        { return solde >= montant; }
    public boolean miser(double montant) {
        if (montant > 0 && peutMiser(montant)) {
            mise  = montant;
            solde -= montant;
            return true;
        }
        return false;
    }
    public void recevoirCarte(Carte c)              { cartes.add(c); }

    /* ---------- valeurs de la main ---------- */
    public int valeurTotale() {
        int v = 0, nbAs = 0;
        for (Carte c : cartes) {
            v += c.getValeur();
            if (c.estUnAs()) nbAs++;
        }
        while (v > 21 && nbAs-- > 0) v -= 10;
        return v;
    }
    public boolean estBuste()     { return valeurTotale() > 21; }
    public boolean aBlackjack()   { return cartes.size() == 2 && valeurTotale() == 21; }

    /* ---------- déroulement du tour ---------- */
    public void jouerTour(Paquet paquet) {
        while (!estBuste() && !stand) {
            int choix = choisirAction(this.cartes, this.cartes.get(0)/*mettre la carte de la banque à la place banque*/);       // 1-HIT, 2-STAND, 3-DOUBLE
            switch (choix) {
                case 1 -> recevoirCarte(paquet.tirerCarte());           // HIT
                case 2 -> stand = true;                                 // STAND
                case 3 -> {                                             // DOUBLE
                    if (cartes.size() == 2 && peutMiser(mise)) {
                        solde -= mise;
                        mise  *= 2;
                        recevoirCarte(paquet.tirerCarte());
                    }
                    stand = true;
                }
                case 4  -> effectuerSplit(paquet);
                default -> stand = true;        // toute saisie invalide → stand
            }
        }
    }

    private boolean peutSplitter() {
        return cartes.size()==2
                && cartes.get(0).getFace().equals(cartes.get(1).getFace())
                && peutMiser(mise);
    }

    private void effectuerSplit(Paquet paquet) {
        if (!peutSplitter()) return;

        // ↓ Préparation des deux mises
        solde -= mise;                      // même mise pour la deuxième main
        List<Carte> main2 = new ArrayList<>();
        main2.add(cartes.remove(1));        // extrait la 2ᵉ carte

        // ↓ On complète chacune des deux mains avec une carte du sabot
        this.recevoirCarte(paquet.tirerCarte());
        main2.add(paquet.tirerCarte());

        /* ---------- Main n°1 ---------- */
        stand = false;                      // relance la boucle jouerTour()
        jouerTour(paquet);                  // joue normalement la 1ʳᵉ main

        /* ---------- Main n°2 ---------- */
        List<Carte> sauvegarde = new ArrayList<>(cartes);   // garde la main 1 pour l’affichage final
        cartes.clear();
        cartes.addAll(main2);
        stand = false;
        jouerTour(paquet);                  // joue la 2ᵉ main

        /* ---------- Fin ---------- */
        cartes.clear();
        cartes.addAll(sauvegarde);          // ré-affiche la 1ʳᵉ main dans le résumé
    }

    /* ---------- à spécialiser ---------- */
    protected abstract int choisirAction(List<Carte> main, Carte banque);

    /* ---------- utilitaires ---------- */
    public void crediter(double facteur) { solde += mise * facteur; }
    public String getNom()               { return nom; }
    public double getSolde()             { return solde; }

    /*  à placer juste après getSolde() … */
    public List<Carte> getCartes() {      // <<< NOUVEAU
        return cartes;
    }


    public boolean isStand()             { return stand; }

    @Override public String toString() {
        return "%s  %s (valeur : %d)  mise : %.2f€  solde : %.2f€"
                .formatted(nom, cartes, valeurTotale(), mise, solde);
    }




    /* ---------- réinitialisation entre deux manches ---------- */
    public void nouvelleMain() {
        cartes.clear();
        mise  = 0;
        stand = false;
    }

}
