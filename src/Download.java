import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Download {
    public void Guardian(String query) {
        String apiKey = "5d63d9e8-4ca4-49e4-9d00-0fba1a197317"; // Inserisci qui la tua chiave API del Guardian
        int numberOfArticles = 200; // Numero di articoli da scaricare
        //String query="france";
        String apiUrl = "https://content.guardianapis.com/search?q="+query+"&show-fields=bodyText&page-size=200&page=";
        try {
            // Crea la cartella "gigi" se non esiste
            String folderName = "Articoli";
            Path folderPath = Paths.get(folderName);
            if (!Files.exists(folderPath)) {
                Files.createDirectory(folderPath);
            }
            for(int k=1; k<6; k++) {
                // Crea una connessione HTTP per l'URL dell'API del Guardian
                int x=apiUrl.indexOf("page=");

                apiUrl=apiUrl.substring(0,x+5);

                apiUrl = apiUrl + "" + k + "&api-key=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Ottieni la risposta JSON dall'API del Guardian
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                    responseBuilder.append(System.lineSeparator());
                }

                // Chiudi il BufferedReader e la connessione
                reader.close();
                connection.disconnect();

                // Parsa la risposta JSON e salva gli articoli come file di testo separati
                JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                JSONArray articles = jsonResponse.getJSONObject("response").getJSONArray("results");

                for (int i = 0; i < numberOfArticles; i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String articleContent = article.getJSONObject("fields").getString("bodyText");

                    // Salva il contenuto dell'articolo in un file di testo separato
                    String fileName = "article" + (i + 1 + (k-1)*200) + ".txt";
                    Path filePath = folderPath.resolve(fileName);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                        writer.write(articleContent);
                    }
                }
            }
            System.out.println("Articoli scaricati e salvati.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
