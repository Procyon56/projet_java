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
                default -> stand = true;        // toute saisie invalide → stand
            }
        }
    }

    /* ---------- à spécialiser ---------- */
    protected abstract int choisirAction(List<Carte> main, Carte banque);

    /* ---------- utilitaires ---------- */
    public void crediter(double facteur) { solde += mise * facteur; }
    public String getNom()               { return nom; }
    public double getSolde()             { return solde; }
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
