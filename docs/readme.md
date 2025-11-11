# Conversor de Moneda (Java + ExchangeRate-API)

Proyecto del desafío: un conversor de monedas por consola que consume la **ExchangeRate-API**, parsea JSON con **Gson** y muestra conversiones y tasas filtradas.

> Hecho con IntelliJ, JDK 25 (sirve 17+), `HttpClient/HttpRequest/HttpResponse` y **Gson**.

---

## Demo

### Menú principal
![Menú CLI](docs/img/menu-cli.png)

### Ejecución de ejemplo (USD → COP)
![Ejecución](docs/img/run-usd-cop.png)

---

## Funcionalidades

- Conversión **USD ⇄ COP** y cualquier par soportado por `/pair/{from}/{to}/{amount}`.  
- Modo **CLI** interactivo con menú en bucle (`Scanner`).  
- **Filtrado** de tasas favoritas desde `/latest/{BASE}` (p. ej. `USD`, `ARS`, `BOB`, `BRL`, `CLP`, `COP`).  
- Manejo básico de **errores** (códigos HTTP, `result != "success"`).  
- **Redondeo** amigable en la salida.

---

## Requisitos

- Java **17+** (usé **JDK 25**).  
- **IntelliJ IDEA** (Community está bien).  
- Clave gratuita de **ExchangeRate-API**: <https://www.exchangerate-api.com/>

---

## Configurar la API Key

EXCHANGE_API_KEY=TU_CLAVE

---

## Cómo correr

1. Clona/abre el proyecto en IntelliJ.  
2. Verifica que `libs/gson-2.13.2.jar` esté agregado como dependencia  
   (**File → Project Structure → Modules → Dependencies**).  
3. Ejecuta la clase `app.ConversorCLI`.  
4. Sigue el menú para convertir monedas o ver tasas favoritas.

> Alternativa (Terminal, desde el root del proyecto):
> ```bash
> javac -cp libs/gson-2.13.2.jar src/app/*.java -d out
> java  -cp "out:libs/gson-2.13.2.jar" app.ConversorCLI
> ```

---

## Endpoints usados

- **Ping/latest**:  
  `https://v6.exchangerate-api.com/v6/{API_KEY}/latest/USD`
- **Par con monto**:  
  `https://v6.exchangerate-api.com/v6/{API_KEY}/pair/{FROM}/{TO}/{AMOUNT}`

Ejemplo:

```text
.../pair/USD/COP/150

Tecnologías

Java 17+ / JDK 25

java.net.http.HttpClient (HttpRequest, HttpResponse)

Gson para parseo JSON

IntelliJ IDEA

Estructura (resumen)

.
├── libs/
│   └── gson-2.13.2.jar
├── docs/
│   └── img/
│       ├── menu-cli.png
│       └── run-usd-cop.png
├── src/
│   └── app/
│       ├── ConversorCLI.java
│       ├── HttpUtil.java         # utilitario HttpClient (opcional)
│       └── ...
└── README.md
