import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.lang.String;

import org.apache.commons.csv.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class Download {
    public void Guardian(String query) {
        String apiKey = "5d63d9e8-4ca4-49e4-9d00-0fba1a197317"; // Inserisci qui la tua chiave API del Guardian
        int numberOfArticles = 200; // Numero di articoli da scaricare per pagine (200=max)

        String apiUrl = "https://content.guardianapis.com/search?q="+query+"&show-fields=bodyText&page-size=200&page=";
        try {
            // Crea la cartella "ArticoliGuardian" se non esiste
            String folderName = "ArticoliGuardian";
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
                    try {
                        JSONObject article = articles.getJSONObject(i);
                        String articleContent = article.getJSONObject("fields").getString("bodyText");

                        // Salva il contenuto dell'articolo in un file di testo separato
                        String fileName = "article" + (i + 1 + (k - 1) * 200) + ".txt";
                        Path filePath = folderPath.resolve(fileName);

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                            writer.write(articleContent);
                        }
                    }catch(Exception e){
                        k=6;
                        break;
                    }
                }
            }
            System.out.println("Articoli scaricati e salvati.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DownloadFromCSV() throws IOException {
        String csvFile = "C:\\Users\\marco\\eclipse\\Downloads\\nytimes_articles_v2.csv"; //cartella del csv
        String line;
        String txtFile = "ArticoliCSV"; //cartella dove salvare i file
        // Crea la cartella se non esiste
        File folder = new File(txtFile);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();
            int i = 1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String body;
                if (line.indexOf("\"") != -1) {
                    int start_body = line.indexOf("\"");
                    int end_body = line.indexOf("\"", start_body + 1);
                    body = line.substring(start_body, end_body - 1);
                } else {
                    int start_body = line.indexOf(",");
                    int end_body = line.indexOf(",", start_body + 1);
                    body = line.substring(start_body, end_body - 1);
                }


                String[] real_values = {values[2], body};

                i++;
                
                // Crea un nuovo file txt ad ogni iterazione
                File file = new File(txtFile + "/Articolo" + i + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                
                // Scrivi il contenuto nel file txt
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Titolo: " + real_values[0] + "\n");
                    writer.write(real_values[1]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
