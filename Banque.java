import java.util.List;

public final class Banque extends Joueur {

    public Banque() {
        super("Banque", 0);           // solde fictif
    }

    /* stratégie : tirer (<17) sinon rester */
    @Override
    protected int choisirAction(List<Carte> main, Carte carteVisibleBanque) {
        return valeurTotale() < 17 ? 1 : 2;   // 1 = HIT, 2 = STAND
    }

    /* (optionnel, pour compatibilité avec l’ancien code) */
    public Joueur getMain() {                 // remplace l’ancienne méthode
        return this;
    }

    @Override
    public String toString() {
        return "Banque : " + super.toString();
    }
}
