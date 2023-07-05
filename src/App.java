import java.util.Scanner;
import java.io.IOException;
public class App{
    public static void main(String[] args) {

        System.out.println("Inserisci se vuoi scaricare (S), leggere (L), entrambi (E), o uscire (U):" +"\n");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while(!input.equals(("U"))){

            if(input.equals("S")){
                System.out.println("Inserire la query desiderata: ");
                String query=scanner.nextLine();
                Download d=new Download();
                d.Guardian(query);
            }

            if(input.equals("L")) {
                try {
                    Analyse.main(new String[0]);
                }catch(Exception e) {
                    System.out.println("Errore nella lettura dei file \n");
                }
            }

            if(input.equals("E")) {
                System.out.println("Inserire la query desiderata: ");
                String query=scanner.nextLine();
                Download d=new Download();
                d.Guardian(query);
                try {
                    Analyse.main(new String[0]);
                }catch(Exception e) {
                    System.out.println("Errore nella lettura dei file \n");
                }
            }
            System.out.println("Cos'altro si vuole fare?"+"\n");
            input = scanner.nextLine();
        }
        System.out.println("Fine del programma.");
    }
}
