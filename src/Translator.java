import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Translator {

    private final String target;
    private final String textToTranslate;
    private final String apiKey = System.getenv("API_KEY");
    private final String apiHost = System.getenv("API_HOST");

    public Translator(String target, String textToTranslate) {
        this.target = target;
        this.textToTranslate = textToTranslate;
    }

    public String translate() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://openl-translate.p.rapidapi.com/translate/bulk"))
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", apiHost)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"target_lang\": \"" + target + "\", \"text\": [\"" + textToTranslate + "\"]}"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return response.body().substring(21, response.body().length() - 3);
    }

}