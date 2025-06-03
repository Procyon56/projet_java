/* ===== JoueurRobot.java ===== */
import java.util.*;

public class JoueurRobot extends Joueur {

    private static String matriceDeChoix[][] = {
            {"x", "2", "3", "4", "5", "6", "7", "8", "9", "T", "A"},
            {"3", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"4", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"5", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"6", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"7", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"8", "h", "h", "h", "h", "h", "h", "h", "h", "h", "h"},
            {"9", "h", "d", "d", "d", "d", "h", "h", "h", "h", "h"},
            {"10", "d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},
            {"11", "d", "d", "d", "d", "d", "d", "d", "d", "d", "h"},
            {"12", "h", "h", "s", "s", "s", "h", "h", "h", "h", "h"},
            {"13", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},
            {"14", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},
            {"15", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},
            {"16", "s", "s", "s", "s", "s", "h", "h", "h", "h", "h"},
            {"17", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"18", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"19", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"20", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"21", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"A2", "h", "h", "h", "d", "d", "h", "h", "h", "h", "h"},
            {"A3", "h", "h", "h", "d", "d", "h", "h", "h", "h", "h"},
            {"A4", "h", "h", "d", "d", "d", "h", "h", "h", "h", "h"},
            {"A5", "h", "h", "d", "d", "d", "h", "h", "h", "h", "h"},
            {"A6", "h", "d", "d", "d", "d", "h", "h", "h", "h", "h"},
            {"A7", "s", "d", "d", "d", "d", "s", "s", "h", "h", "h"},
            {"A8", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"A9", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"22", "sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},
            {"33", "sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},
            {"44", "h", "h", "h", "sp", "sp", "h", "h", "h", "h", "h"},
            {"55", "d", "d", "d", "d", "d", "d", "d", "d", "h", "h"},
            {"66", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h", "h"},
            {"77", "sp", "sp", "sp", "sp", "sp", "sp", "h", "h", "h", "h"},
            {"88", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"},
            {"99", "sp", "sp", "sp", "sp", "sp", "s", "sp", "sp", "s", "s"},
            {"TT", "s", "s", "s", "s", "s", "s", "s", "s", "s", "s"},
            {"AA", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp", "sp"}

    };

    public JoueurRobot(String nom, double soldeInitial) {
        super(nom, soldeInitial);
    }

    @Override
    protected int choisirAction(List<Carte> main, Carte banque) {

        int indiceBanque;
        int indiceMainRobot;
        String choix;

        indiceBanque = getIndiceBanque(banque);
        indiceMainRobot = getIndiceMain(main);

        choix = matriceDeChoix[indiceMainRobot][indiceBanque];

        switch(choix)   {
            case "h" :
                return 1;
            case "s" :
                return 2;
            case "d" :
                return 3;
            case "sp" :
                return 4;
            default :
                return 0;
        }
    }

    private int getIndiceBanque(Carte banque)   {

        switch(banque.getFace())  {
            case "2" :
                return 1;
            case "3" :
                return 2;
            case "4" :
                return 3;
            case "5" :
                return 4;
            case "6" :
                return 5;
            case "7" :
                return 6;
            case "8" :
                return 7;
            case "9" :
                return 8;
            case "Valet":
            case "Dame":
            case "Roi":
                return 9;
            case "As" :
                return 10;
            default :
                return 0;
        }
    }

    private int getIndiceMain(List<Carte> main) {
        if (main.get(0).getFace() == main.get(1).getFace()) {
            switch(main.get(0).getFace())   {
                case "2" :
                    return 28;
                case "3" :
                    return 29;
                case "4" :
                    return 30;
                case "5" :
                    return 31;
                case "6" :
                    return 32;
                case "7" :
                    return 33;
                case "8" :
                    return 34;
                case "9" :
                    return 35;
                case "Valet":
                case "Dame":
                case "Roi":
                    return 36;
                case "As" :
                    return 37;
                default :
                    return 0;
            }
        }
        else if (main.get(0).estUnAs() || main.get(1) .estUnAs())    {
            int carteARegarder;
            if (main.get(0) .estUnAs())  {
                carteARegarder = 1;
            }
            else {
                carteARegarder = 0;
            }

            switch(main.get(carteARegarder).getFace())   {
                case "2" :
                    return 20;
                case "3" :
                    return 21;
                case "4" :
                    return 22;
                case "5" :
                    return 23;
                case "6" :
                    return 24;
                case "7" :
                    return 25;
                case "8" :
                    return 26;
                case "9" :
                    return 27;
                case "Valet":
                case "Dame":
                case "Roi":
                    return 27;
                case "As" :
                    return 28;
                default :
                    return 0;
            }
        }
        else {
            int valeur;
            valeur = main.get(0) .getValeur() + main.get(1) .getValeur();
            switch(valeur)   {
                case 3 :
                    return 1;
                case 4 :
                    return 2;
                case 5 :
                    return 3;
                case 6 :
                    return 4;
                case 7 :
                    return 5;
                case 8 :
                    return 6;
                case 9 :
                    return 7;
                case 10 :
                    return 8;
                case 11 :
                    return 9;
                case 12:
                    return 10;
                case 13:
                    return 11;
                case 14 :
                    return 12;
                case 15 :
                    return 13;
                case 16 :
                    return 14;
                case 17 :
                    return 15;
                case 18 :
                    return 16;
                case 19 :
                    return 17;
                case 20 :
                    return 18;
                case 21 :
                    return 19;
                default :
                    return 0;
            }
        }
    }
}