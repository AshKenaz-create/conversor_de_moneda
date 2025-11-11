package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

        // <<< aquí mezclamos la idea: usamos HttpUtil.get con la URL de /pair >>>
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey +
                "/pair/" + from + "/" + to + "/" + amount;

        String body = HttpUtil.get(url);

        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (!"success".equalsIgnoreCase(json.get("result").getAsString())) {
            System.out.println("La API respondió error:\n" + body);
            return;
        }

        double rate = json.get("conversion_rate").getAsDouble();
        double converted = json.get("conversion_result").getAsDouble();

        System.out.println("\n==== Resultado ====");
        System.out.println("De: " + from + "  A: " + to);
        System.out.printf("Tasa: %.4f%n", rate);
        System.out.printf("Monto: %.2f%n", amount);
        System.out.printf("Convertido: %.2f%n", converted);
    }
}
