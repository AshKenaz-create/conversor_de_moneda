package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PairPing {

    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("EXCHANGE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Falta EXCHANGE_API_KEY"); return;
        }

        String from = "USD";
        String to   = "COP";
        double amount = 100;

        // Con monto (devuelve conversion_result)
        URI uri = URI.create("https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + from + "/" + to + "/" + amount);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(uri).build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        System.out.println("HTTP: " + res.statusCode());
        if (res.statusCode() != 200) { System.out.println(res.body()); return; }

        JsonObject json = JsonParser.parseString(res.body()).getAsJsonObject();
        System.out.println("result: " + json.get("result").getAsString());
        System.out.println("base:   " + json.get("base_code").getAsString());
        System.out.println("target: " + json.get("target_code").getAsString());
        System.out.println("rate:   " + json.get("conversion_rate").getAsDouble());

        if (json.has("conversion_result")) {
            System.out.println("amount: " + amount);
            System.out.println("result: " + json.get("conversion_result").getAsDouble());
        }
    }
}
