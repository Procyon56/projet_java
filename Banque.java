import java.util.List;

public class Banque {
    private MainJoueur main;
    // private List<Carte> main;

    public Banque() {
        this.main = new MainJoueur();
    }


    public void recevoirCarte(Carte carte) {
        if (carte != null) {
            this.main.ajouterCarte(carte);
        } else {
            System.err.println("Tentative d'ajout d'une carte null à la main de la banque.");
        }
    }


    public boolean doitTirer() {
        return this.main.valeurTotale() < 17;
    }


    public void nouvelleMain() {
        this.main = new MainJoueur();
    }

    public MainJoueur getMain() {
        return main;
    }


    public String toString() {

        return "Banque: " + main.toString();
    }

}