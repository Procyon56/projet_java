
public class Carte {
    private String couleur;
    private String face;
    private int valeur;


    public Carte(String couleur, String face) {
        this.couleur = couleur;
        this.face = face;
        this.valeur = calculerValeur(face); // Calcule et stocke la valeur initiale
    }

    private int calculerValeur(String face) {
        switch (face) {
            case "Valet":
            case "Dame":
            case "Roi":
                return 10;
            case "As":
                    return 11;
            case "2": case "3": case "4": case "5": case "6": case "7": case "8": case "9": case "10":
                return Integer.parseInt(face);
            default:

                throw new IllegalArgumentException("Face de carte invalide: " + face);
        }
    }


    public int getValeur() {
        return valeur;
    }

    public boolean estUnAs() {
        return this.face.equals("As");
    }

    public String getFace() { return face; }

    public String toString() {
        return face + couleur;
    }
}