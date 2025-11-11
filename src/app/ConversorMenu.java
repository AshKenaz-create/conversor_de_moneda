package app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

public class ConversorMenu {

    private static final List<String> FAVORITAS = List.of("USD", "ARS", "BOB", "BRL", "CLP", "COP");

    public static void main(String[] args) {
        String apiKey = System.getenv("EXCHANGE_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Falta EXCHANGE_API_KEY");
            return;
        }

        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US); // punto decimal

        while (true) {
            mostrarMenu();
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();

            switch (op) {
                case "1" -> convertirRapido(apiKey, "USD", "COP", sc);
                case "2" -> convertirRapido(apiKey, "COP", "USD", sc);
                case "3" -> convertirRapido(apiKey, "USD", "BRL", sc);
                case "4" -> convertirRapido(apiKey, "USD", "CLP", sc);
                case "5" -> verTasasFavoritas(apiKey, "USD");
                case "6" -> conversionPersonalizada(apiKey, sc);
                case "0" -> {
                    System.out.println("¡Gracias por usar el conversor!");
                    return;
                }
                default -> System.out.println("Elija una opción válida (0-6).");
            }

            System.out.print("\n¿Desea realizar otra operación? (S/N): ");
            String again = sc.nextLine().trim().toUpperCase();
            if (!again.equals("S")) {
                System.out.println("¡Hasta luego!");
                break;
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== Sea bienvenido/a al Conversor de Moneda ===");
        System.out.println("1) USD -> COP");
        System.out.println("2) COP -> USD");
        System.out.println("3) USD -> BRL");
        System.out.println("4) USD -> CLP");
        System.out.println("5) Ver tasas favoritas (USD base)");
        System.out.println("6) Conversión personalizada (FROM/TO/AMOUNT)");
        System.out.println("0) Salir");
    }

    private static void convertirRapido(String apiKey, String from, String to, Scanner sc) {
        try {
            System.out.print("Monto a convertir (" + from + "): ");
            double amount = Double.parseDouble(sc.nextLine().trim());
            double converted = convertirConPair(apiKey, from, to, amount);
            double rate = obtenerTasaConPair(apiKey, from, to);
            imprimirResultado(from, to, amount, rate, converted);
        } catch (NumberFormatException nfe) {
            System.out.println("Monto inválido. Usa números (ej: 150 o 150.50).");
        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e.getMessage());
        }
    }

    private static void conversionPersonalizada(String apiKey, Scanner sc) {
        try {
            System.out.print("Moneda origen (ej: USD): ");
            String from = sc.nextLine().trim().toUpperCase();
            System.out.print("Moneda destino (ej: COP): ");
            String to = sc.nextLine().trim().toUpperCase();

            if (!esCodigoValido(from) || !esCodigoValido(to)) {
                System.out.println("Usa códigos ISO de 3 letras (ej: USD, COP, EUR).");
                return;
            }

            System.out.print("Monto a convertir: ");
            double amount = Double.parseDouble(sc.nextLine().trim());

            double converted = convertirConPair(apiKey, from, to, amount);
            double rate = obtenerTasaConPair(apiKey, from, to);
            imprimirResultado(from, to, amount, rate, converted);
        } catch (NumberFormatException nfe) {
            System.out.println("Monto inválido. Usa números (ej: 150 o 150.50).");
        } catch (Exception e) {
            System.out.println("Error en la conversión: " + e.getMessage());
        }
    }

    private static boolean esCodigoValido(String code) {
        return code != null && code.length() == 3 && code.chars().allMatch(Character::isLetter);
    }

    private static double convertirConPair(String apiKey, String from, String to, double amount) throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey +
                "/pair/" + from + "/" + to + "/" + amount;
        String body = HttpUtil.get(url);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        validarRespuesta(json, body);
        return json.get("conversion_result").getAsDouble();
    }

    private static double obtenerTasaConPair(String apiKey, String from, String to) throws Exception {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey +
                "/pair/" + from + "/" + to;
        String body = HttpUtil.get(url);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        validarRespuesta(json, body);
        return json.get("conversion_rate").getAsDouble();
    }

    private static void validarRespuesta(JsonObject json, String body) {
        if (!"success".equalsIgnoreCase(json.get("result").getAsString())) {
            throw new RuntimeException("API error -> " + body);
        }
    }

    private static void imprimirResultado(String from, String to, double amount, double rate, double converted) {
        System.out.println("\n==== Resultado ====");
        System.out.println("De: " + from + "  A: " + to);
        System.out.printf("Tasa: %.4f%n", rate);
        System.out.printf("Monto: %.2f%n", amount);
        System.out.printf("Convertido: %.2f%n", converted);
    }

    private static void verTasasFavoritas(String apiKey, String base) {
        try {
            String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + base;
            String body = HttpUtil.get(url);
            JsonObject json = JsonParser.parseString(body).getAsJsonObject();
            validarRespuesta(json, body);

            JsonObject rates = json.getAsJsonObject("conversion_rates");
            System.out.println("\nTasas respecto a " + base + ":");
            for (String code : FAVORITAS) {
                if (rates.has(code)) {
                    System.out.printf("  %s: %.4f%n", code, rates.get(code).getAsDouble());
                }
            }
        } catch (Exception e) {
            System.out.println("No pude obtener las tasas favoritas: " + e.getMessage());
        }
    }
}
