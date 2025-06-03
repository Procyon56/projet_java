import java.util.List;
import java.util.Scanner;

public class JoueurHumain extends Joueur {

    private final Scanner in;

    public JoueurHumain(String nom, double soldeInitial, Scanner in) {
        super(nom, soldeInitial);
        this.in = in;
    }

    @Override
    protected int choisirAction(List<Carte> main, Carte banque) {
        System.out.printf("%n%s : %s  (1)Tirer (2)Rester (3)Doubler  -> ",
                nom, cartes);
        try { return Integer.parseInt(in.nextLine()); }
        catch (Exception e) { return 2; }          // par défaut : Stand
    }
}
