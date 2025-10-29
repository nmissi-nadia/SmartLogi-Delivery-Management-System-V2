//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void hanoi(int n,char src,char cible,char aux) {
        if (n == 1) {
            System.out.println("Déplacer le disque 1 de " + src + " vers " + cible);
        } else {
            hanoi(n - 1, src, aux, cible);
            System.out.println("Déplacer le disque " + n + " de " + src + " vers " + cible);
            hanoi(n - 1, aux, cible, src);
        }
    }
    public static void main(String[] args) {
        int nbres_disc=3;
        hanoi(nbres_disc,'A','B','C');


    }
}