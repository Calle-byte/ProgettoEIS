import edu.stanford.nlp.simple.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 Classe che legge i file txt di una determinata directory, e contanta le parole più frequenti.
*/
public class Analyse {
    
    /**
    * Esegue una ricerca all'interno dei file TXT nella directory specificata e conteggia le parole più frequenti,
    * escludendo le parole presenti nella blacklist.
    *
    * @param i Indicatore per selezionare la directory dei file (0 per gli articoli dal Guardian, 1 per gli articoli CSV).
    * @throws IOException Se si verifica un errore durante la lettura dei file o della blacklist.
    */
    public void Search(int i) throws IOException {     

        // Set di stringhe contenente le parole vietate
        Set<String> EXCLUDED_WORDS = new HashSet<>();
        try {
            FileReader fr = new FileReader("C:\\Users\\marco\\IdeaProjects\\TheGuardian\\BanList.txt");    //Directory della black list di parole

            BufferedReader br = new BufferedReader(fr);    // Lettura della blacklist
            String line;

            // Aggiunta parole al set
            while ((line = br.readLine()) != null) {
                EXCLUDED_WORDS.add(line);
            }
        }catch(Exception e){}

        // Imposta il percorso della cartella contenente i file TXT
        String folderPath="";
        if(i==0) folderPath = "C:\\Users\\marco\\IdeaProjects\\TheGuardian\\ArticoliGuardian";  //Directory della cartella contenente gli articoli scaricati dal Guardian
        if(i==1) folderPath = "C:\\Users\\marco\\IdeaProjects\\TheGuardian\\ArticoliCSV";       //Directory della cartella contenente gli articoli scaricati dal CSV

        // Lista di file contenuti nella cartella
        List<Path> filePaths = getTxtFilePaths(folderPath);
        // Mappa contatore di parole
        Map<String, Integer> wordCounts = new HashMap<>();
        // Lista di parole già trovate
        Set<String> countedWords = new HashSet<>();
        
        for (Path filePath : filePaths) {
            Set<String> uniqueWords = new HashSet<>();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                List<String> words = getWords(line);
                for (String word : words) {
                    String cleanWord = word.toLowerCase();
                    
                    // Controlla se la parola unica è accettabile
                    if (!isExcludedWord(cleanWord, EXCLUDED_WORDS)) {
                        uniqueWords.add(cleanWord);
                    }
                }
            }
            for (String word : uniqueWords) {
                if (countedWords.contains(word)) {
                    Integer count = wordCounts.get(word);
                    
                    // Se la parola è gia presente, aumenta il contatore della parola di 1
                    if (count == null) {
                        count = 1;
                    }
                    wordCounts.put(word, count + 1);
                } else {
                    // Se la parola non è contenuta nella lista parole, la aggiunge
                    countedWords.add(word);
                }
            }
        }
        
        // Output delle 10 parole più frequenti, ossia le prime della mappa ordinata
        List<Map.Entry<String, Integer>> sortedEntries = sortByValue(wordCounts);
        System.out.println("Le parole più frequenti sono:");
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (count >= 10) {
                break;
            }
            System.out.println(entry.getKey() + " - " + entry.getValue());
            count++;
        }
    }

    /**
    * Restituisce una lista contenente i titoli dei file txt presenti nella directory specificata.
    *
    * @param directoryPath Il percorso della directory da cui estrarre i titoli dei file TXT.
    * @return Una lista di stringhe che rappresentano i titoli dei file TXT trovati nella directory.
    * @throws IOException Se il percorso specificato non corrisponde a una directory esistente.
    */
    private static List<Path> getTxtFilePaths(String folderPath) throws IOException {
        List<Path> filePaths = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                    filePaths.add(file.toPath());
                }
            }
        }
        return filePaths;
    }
    
    /**
    * Estrae le singole parole da un testo dato e le restituisce come una lista di stringhe.
    *
    * @param text Il testo da cui estrarre le parole.
    * @return Una lista contenente le singole parole estratte dal testo.
    */
    private static List<String> getWords(String text) {
        Sentence sentence = new Sentence(text);
        return sentence.words();
    }
    
    /**
    * Verifica se una parola è presente nell'insieme di parole escluse.
    *
    * @param word La parola da verificare.
    * @param EXCLUDED_WORDS L'insieme di parole escluse da confrontare.
    * @return true se la parola è presente nell'insieme di parole escluse, false altrimenti.
    */
    private static boolean isExcludedWord(String word, Set<String> EXCLUDED_WORDS) {
        return EXCLUDED_WORDS.contains(word);
    }
    
    /**
    * Ordina una mappa in base ai valori dei suoi elementi e restituisce una lista
    * di voci mappa (chiave-valore) ordinate in ordine decrescente dei valori.
    *
    * @param map La mappa da ordinare in base ai valori.
    * @return Una lista di voci mappa (chiave-valore) ordinate per valore in ordine decrescente.
    */
    private static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });
        return entries;
    }
}
