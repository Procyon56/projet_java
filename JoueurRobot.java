/* ===== JoueurRobot.java (version améliorée) ===== */
import java.util.*;

public class JoueurRobot extends Joueur {

    /**
     * Tableau « basic strategy » hard/soft/paires  – 11 colonnes (2‑A) × 38 lignes (en-tête + 37 clés).
     */
    private static final String[][] MATRICE = {
            /*2    3    4    5    6    7    8    9    T    A*/
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},             //5     0
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},             //6     1
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},             //7     2
            {"h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},             //8     3
            {"h", "d", "d", "d", "d", "h", "h", "h", "h", "h"},             //9     4
            {"d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},             //10    5
            {"d", "d", "d", "d", "d", "d", "d", "d", "d", "h"},             //11    6
            {"h", "h", "s", "s", "s", "h", "h", "h", "h", "h"},             //12    7
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},             //13    8
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},             //14    9
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},             //15    10
            {"s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},             //16    11
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //17    12
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //18    13
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //19    14
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //20    15
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //21    16
            {"h", "h", "h", "d", "d", "h", "h", "h", "h", "h"},             //A2    17
            {"h", "h", "h", "d", "d", "h", "h", "h", "h", "h"},             //A3    18
            {"h", "h", "d", "d", "d", "h", "h", "h", "h", "h"},             //A4    19
            {"h", "h", "d", "d", "d", "h", "h", "h", "h", "h"},             //A5    20
            {"h", "d", "d", "d", "d", "h", "h", "h", "h", "h"},             //A6    21
            {"s", "d", "d", "d", "d", "s", "s", "h", "h", "h"},             //A7    22
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //A8    23
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},             //A9    24
            {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},       //22    25
            {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},       //33    26
            {"h", "h", "h", "sp", "sp", "h", "h", "h", "h", "h"},          //44    27
            {"d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},            //55    28
            {"sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "h"},        //66    29
            {"sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},       //77    30
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"},   //88    31
            {"sp", "sp", "sp", "sp", "sp", "s", "sp", "sp", "s", "s"},     //99    32
            {"s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},            //TT    33
            {"sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"}    //AA    34
    };

    public JoueurRobot(String nom, double soldeInitial) {
        super(nom, soldeInitial);
    }

    /**
     * Retour : 1=Hit, 2=Stand, 3=Double, 4=Split, 0=Erreur.
     */
    @Override
    protected int choisirAction(List<Carte> main, Carte banque) {

        // On sécurise l'accès : si la main est incomplète, on reste tranquille.
        if (main == null || main.size() < 2) return 2;        // Stand par prudence

        int indiceBanque = getIndiceBanque(banque);
        int indiceMain = getIndiceMain(main);

        // Si les indices sortent du tableau, fallback Stand.
        if (indiceBanque <= 0 || indiceBanque >= MATRICE[0].length ||
                indiceMain <= 0 || indiceMain >= MATRICE.length)
            return 2;

        String choix = MATRICE[indiceMain][indiceBanque];

        return switch (choix) {
            case "h" -> 1;   // HIT
            case "s" -> 2;   // STAND
            case "d" -> 3;   // DOUBLE
            case "sp" -> 4;   // SPLIT (non géré dans Partie mais prévu)
            default -> 0;   // inconnu
        };
    }

    /* ----------------------- Helpers ----------------------- */

    /**
     * Colonne (1–10) selon la carte visible du croupier.
     */
    private int getIndiceBanque(Carte banque) {
        if (banque == null) return 0;
        return banque.getValeur() - 2;
    }

    /**
     * Ligne dans la MATRICE pour la main du robot.
     */
    private int getIndiceMain(List<Carte> main) {

        // ----- Paire -----
        if (main.get(0).getFace().equals(main.get(1).getFace())) {
            return main.get(0).getValeur() + 23;
        } else if (main.get(0).estUnAs() || main.get(1).estUnAs()) {
            int carteARegarder;
            if (main.get(0).estUnAs()) {
                carteARegarder = 1;
            } else {
                carteARegarder = 0;
            }
            return main.get(carteARegarder).getValeur() + 15;

        } else {
            int total = main.get(0).getValeur() + main.get(1).getValeur();
            return total-5;
        }
    }
}
