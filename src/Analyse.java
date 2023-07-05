import edu.stanford.nlp.simple.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Analyse {

    private static final Set<String> EXCLUDED_WORDS = new HashSet<>(Arrays.asList(
            ",", ".", ":", ";", "!", "?", "(", ")", "[", "]", "{", "}",
            "and", "or", "but", "if", "then", "else", "while", "do", "for", "to",
            "a", "an", "the", "in", "on", "at", "by", "about", "with", "without",
            "of", "off", "from", "into", "onto", "through", "over", "under", "above", "below",
            "that", "’s", "is", "it", "as", "\"", "“", "”", "-", "have", "this", "was", "has",
            "be", "are", "not", "been", "which", "had", "more", "said", "their", "who", "they", "up",
            "out", "after", "he", "–"
    ));

    public static void main(String[] args) throws IOException {
        String folderPath = "C:\\Users\\marco\\IdeaProjects\\TheGuardian\\Articoli"; // Imposta il percorso della cartella contenente i file TXT
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
                    if (!isExcludedWord(cleanWord)) {
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

    private static boolean isExcludedWord(String word) {
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
