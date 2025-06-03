import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainJoueur {

    private List<Carte> cartes;
    private double mise;
    private boolean stand;


    public MainJoueur() {
        this.cartes = new ArrayList<>();
        this.mise = 0.0;
        this.stand = false;
    }


    public void ajouterCarte(Carte carte) {
        if (carte != null) {
            this.cartes.add(carte);
        } else {
            System.err.println("Tentative d'ajout d'une carte null à une main.");
        }

    }


    public List<Carte> getCartes() {
        return this.cartes;
    }


    public int valeurTotale() {
        int valeur = 0;
        int nbAs = 0;
        for (Carte carte : cartes) {
            valeur += carte.getValeur(); // Assumant que getValeur() retourne 11 pour un As initialement
            if (carte.estUnAs()) { // Assumant que Carte a une méthode estUnAs()
                nbAs++;
            }
        }

        while (valeur > 21 && nbAs > 0) {
            valeur -= 10; // Change la valeur d'un As de 11 à 1
            nbAs--;
        }
        return valeur;
    }

    public boolean estBuste() {
        return valeurTotale() > 21;
    }

    public boolean aBlackjack() {
        return this.cartes.size() == 2 && valeurTotale() == 21;
    }




    public String toString() {
        String cartesStr = cartes.stream()
                .map(Carte::toString) // Assumant que Carte.toString() donne qqch comme "8♠" ou "V♥"
                .collect(Collectors.joining(", "));
        return "[" + cartesStr + "] (Valeur: " + valeurTotale() + ")";
    }
}