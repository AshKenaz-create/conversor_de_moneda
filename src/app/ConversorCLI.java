package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;

public class ConversorCLI {

    public static void main(String[] args) throws Exception {
        String apiKey = System.getenv("EXCHANGE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Falta EXCHANGE_API_KEY");
            return;
        }

        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US); // para punto decimal

        System.out.print("Moneda origen (ej: USD): ");
        String from = sc.nextLine().trim().toUpperCase();

        System.out.print("Moneda destino (ej: COP): ");
        String to = sc.nextLine().trim().toUpperCase();

        System.out.print("Monto a convertir: ");
        double amount = Double.parseDouble(sc.nextLine().trim());

        URI uri = URI.create("https://v6.exchangerate-api.com/v6/" + apiKey +
                "/pair/" + from + "/" + to + "/" + amount);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(uri).build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            System.out.println("Error HTTP: " + res.statusCode());
            System.out.println(res.body());
            return;
        }

        JsonObject json = JsonParser.parseString(res.body()).getAsJsonObject();
        if (!"success".equalsIgnoreCase(json.get("result").getAsString())) {
            System.out.println("La API respondió error:\n" + res.body());
            return;
        }

        double rate = json.get("conversion_rate").getAsDouble();
        double converted = json.get("conversion_result").getAsDouble();

        System.out.println("\n==== Resultado ====");
        System.out.println("De: " + from + "  A: " + to);
        System.out.println("Tasa: " + rate);
        System.out.println("Monto: " + amount);
        System.out.println("Convertido: " + converted);
    }
}
