import java.util.Scanner;
import java.io.IOException;
public class AppTest{
    public static void main(String[] args) {

        System.out.println("Inserisci se vuoi scaricare dal guardian (S), leggere una cartella (L), scaricare dal csv (C), sia guardian che leggere (E), o uscire (U):" +"\n");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while(!input.equals(("U"))){

            if(input.equals("S")){
                System.out.println("Inserire la query desiderata: ");
                String query=scanner.nextLine();
                Download d=new Download();
                d.Guardian(query);
            }

            if(input.equals("C")){
                try {
                    Download d=new Download();
                    d.DownloadFromCSV();
                }catch(Exception e) {
                    System.out.println("Errore nella lettura dei file \n");
                }
            }

            if(input.equals("L")) {
                try {
                    Analyse a=new Analyse();
                    System.out.println("Da dove si vuole leggere? Guardian (G) o CVS (C)?: ");
                    String read=scanner.nextLine();
                    if(read.equals(("G"))) {
                        a.Search(0);
                    }else {
                        if(read.equals("C")){
                            a.Search(1);
                        }else{
                            System.out.println("Input errato. ");
                        }
                    }
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
                    Analyse a=new Analyse();
                    a.Search(0);
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
