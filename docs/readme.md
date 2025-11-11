---
title: README
---

# Conversor de Moneda (Java + ExchangeRate-API)

Proyecto del desafío: un conversor de monedas por consola que consume la **ExchangeRate-API**, parsea JSON con **Gson** y muestra conversiones y tasas filtradas.

> Hecho con IntelliJ, JDK 25 (sirve 17+), `HttpClient/HttpRequest/HttpResponse` y **Gson**.

---

## Demo

### Menú principal
![Menú CLI](./img/menu-cli.png)

### Ejecución de ejemplo (USD → COP)
![Ejecución](./img/run-usd-cop.png)

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

```bash
EXCHANGE_API_KEY=TU_CLAVE
