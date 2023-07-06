import edu.stanford.nlp.simple.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Analyse {
    
    //Passa come variabile un numero, che sarà =0 se vogliamo guardare la cartella del Guardian, =1 per il CSV
    public void Search(int i) throws IOException {      
        Set<String> EXCLUDED_WORDS = new HashSet<>();
        try {
            FileReader fl = new FileReader("C:\\Users\\marco\\IdeaProjects\\TheGuardian\\BanList.txt"); //Directory della black list di parole

            BufferedReader br = new BufferedReader(fl);
            String line;
            while ((line = br.readLine()) != null) {
                EXCLUDED_WORDS.add(line);
            }
        }catch(Exception e){}

        // Imposta il percorso della cartella contenente i file TXT
        String folderPath="";
        if(i==0) folderPath = "C:\\Users\\marco\\IdeaProjects\\TheGuardian\\ArticoliGuardian";  //Directory della cartella contenente gli articoli scaricati dal Guardian
        if(i==1) folderPath = "C:\\Users\\marco\\IdeaProjects\\TheGuardian\\ArticoliCSV";       //Directory della cartella contenente gli articoli scaricati dal CSV

        List<Path> filePaths = getTxtFilePaths(folderPath);
        Map<String, Integer> wordCounts = new HashMap<>();
        Set<String> countedWords = new HashSet<>();
        for (Path filePath : filePaths) {
            Set<String> uniqueWords = new HashSet<>();
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                List<String> words = getWords(line);
                for (String word : words) {
                    String cleanWord = word.toLowerCase();
                    if (!isExcludedWord(cleanWord, EXCLUDED_WORDS)) {
                        uniqueWords.add(cleanWord);
                    }
                }
            }
            for (String word : uniqueWords) {
                if (countedWords.contains(word)) {
                    Integer count = wordCounts.get(word);
                    if (count == null) {
                        count = 1;
                    }
                    wordCounts.put(word, count + 1);
                } else {
                    countedWords.add(word);
                }
            }
        }
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

    private static List<String> getWords(String text) {
        Sentence sentence = new Sentence(text);
        return sentence.words();
    }

    private static boolean isExcludedWord(String word, Set<String> EXCLUDED_WORDS) {
        return EXCLUDED_WORDS.contains(word);
    }

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
