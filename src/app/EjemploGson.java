package app;

import com.google.gson.Gson;

public class EjemploGson {

    public static void main(String[] args) {
        // objeto Java
        Persona persona = new Persona("Juan", 30);

        // instancia de Gson
        Gson gson = new Gson();

        // serializar a JSON
        String json = gson.toJson(persona);
        System.out.println(json); // {"nombre":"Juan","edad":30}

        // deserializar a objeto
        Persona personaDeserializada = gson.fromJson(json, Persona.class);
        System.out.println(personaDeserializada.getNombre()); // Juan
    }
}
