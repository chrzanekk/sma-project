# SMA Project — Scaffolding Management App

Aplikacja do zarządzania rusztowaniami oparta na architekturze modularnego monolitu.
Stack: **Spring Boot 3** (backend) + **React/Vite** (frontend) + **PostgreSQL** + **Nginx** + **Grafana/Prometheus/Loki**.

---

## Spis treści

1. [Wymagania](#wymagania)
2. [Struktura projektu](#struktura-projektu)
3. [Pierwsze uruchomienie](#pierwsze-uruchomienie)
4. [Konfiguracja pliku .env](#konfiguracja-pliku-env)
5. [Tryby uruchamiania](#tryby-uruchamiania)
6. [Dostęp do serwisów](#dostęp-do-serwisów)
7. [Przydatne komendy](#przydatne-komendy)
8. [Monitoring — Grafana](#monitoring--grafana)
9. [Praca lokalna (IntelliJ + kontener z bazą)](#praca-lokalna-intellij--kontener-z-bazą)
10. [Rozwiązywanie problemów](#rozwiązywanie-problemów)

---

## Wymagania

| Narzędzie | Minimalna wersja | Pobierz |
|-----------|-----------------|---------|
| Docker Desktop | 24.x | https://www.docker.com/products/docker-desktop |
| Docker Compose | v2.x (wbudowany w Docker Desktop) | — |
| Git | dowolna | https://git-scm.com |
| Java 21 (opcjonalne) | 21 | https://adoptium.net — tylko do lokalnego devu bez Dockera |
| Node.js (opcjonalne) | 20.x | https://nodejs.org — tylko do lokalnego devu bez Dockera |

---

## Struktura projektu

```
sma-project/
├── backend-api/              # Spring Boot 3 — kod źródłowy backendu
│   ├── Dockerfile
│   └── src/
├── frontend/react/           # React + Vite — kod źródłowy frontendu
│   ├── Dockerfile
│   ├── .env                  # VITE backend url dla localhost
│   └── src/
├── config/                   # Konfiguracje serwisów infrastrukturalnych
│   ├── nginx.conf
│   ├── loki-config.yml
│   ├── prometheus.yml
│   └── grafana/
│       └── provisioning/
│           └── datasources/
│               └── datasource.yml
├── docker-compose.yml        # Definicja wszystkich serwisów
├── .env                      # Zmienne środowiskowe — NIE commituj! (dodaj do .gitignore)
├── .env.example              # Szablon zmiennych — commituj bez wartości
└── README.md
```

---

## Pierwsze uruchomienie

### Krok 1 — Sklonuj repozytorium

```bash
git clone https://github.com/<organization>/sma-project.git
cd sma-project
```

### Krok 2 — Utwórz plik `.env`

Skopiuj szablon i uzupełnij wartości (szczegóły poniżej w sekcji [Konfiguracja pliku .env](#konfiguracja-pliku-env)):

```bash
# Windows (PowerShell)
Copy-Item .env.example .env

# Linux / macOS
cp .env.example .env
```

Następnie otwórz `.env` w edytorze i uzupełnij wszystkie wartości.

### Krok 3 — Zbuduj i uruchom kontenery

```bash
docker-compose up --build -d
```

Pierwsze uruchomienie może zająć kilka minut — Docker pobierze obrazy bazowe i zbuduje backend oraz frontend.

### Krok 4 — Sprawdź czy wszystko działa

```bash
docker-compose ps
```

Wszystkie serwisy powinny mieć status `running` lub `healthy`. Następnie otwórz przeglądarkę i przejdź do `http://localhost`.

---

## Konfiguracja pliku `.env` dla backendu

Plik `.env` zawiera wrażliwe dane i **nigdy nie powinien trafiać do repozytorium** (jest w `.gitignore`).

Skopiuj `.env.example` do `.env` i uzupełnij poniższe wartości:

```env
# ─── Baza danych PostgreSQL ──────────────────────────────────────────────────
# Nazwa użytkownika do bazy danych
SPRING_DATASOURCE_USERNAME=sma_user

# Hasło do bazy danych — ustaw własne, silne hasło
SPRING_DATASOURCE_PASSWORD=zmien_na_silne_haslo

# ─── JWT ─────────────────────────────────────────────────────────────────────
# Sekret do podpisywania tokenów JWT
# Wygeneruj losowy ciąg, np.: openssl rand -base64 64
JWT_SECRET=wygeneruj_losowy_sekret_min_64_znaki

# ─── Konfiguracja e-mail (SMTP) ───────────────────────────────────────────────
# Adres serwera SMTP (np. smtp.gmail.com)
MAIL_HOST=smtp.gmail.com

# Port SMTP (zazwyczaj 587 dla TLS lub 465 dla SSL)
MAIL_PORT=587

# Login do konta e-mail
MAIL_USERNAME=twoj-email@gmail.com

# Hasło do konta e-mail lub App Password (Google: https://myaccount.google.com/apppasswords)
MAIL_PASSWORD=twoje_haslo_smtp

# ─── Grafana ─────────────────────────────────────────────────────────────────
# Hasło administratora Grafany (domyślnie: admin)
GRAFANA_PASSWORD=zmien_na_silne_haslo

# ─── URL platformy ───────────────────────────────────────────────────────────
# Adres frontendu używany w linkach e-mail (lokalnie: http://localhost)
PLATFORM_URL=http://localhost
```

> **Wskazówka dla Gmail:** Jeśli używasz konta Google, włącz weryfikację dwuetapową i wygeneruj
> **App Password** zamiast używania głównego hasła do konta.

> **Wskazówka dla JWT_SECRET:** Wygeneruj bezpieczny sekret komendą:
> ```bash
> # Linux / macOS / Git Bash
> openssl rand -base64 64
> ```

## Konfiguracja pliku `.env` dla frontendu (W przypadku pracy lokalnej patrz [Praca lokalna](#praca-lokalna-intellij--kontener-z-bazą))

Pliki `.env` i `.env.local` umieszczamy w katalogu /frontend/react

Zawartość `.env` powinna wyglądać tak:
```
VITE_API_BASE_URL=http://localhost
```

Zawartość `.env.local` powinna wyglądać tak:
```
VITE_API_BASE_URL=http://localhost:8080 ## lub inny wybrany port dla backendu
```


---

## Tryby uruchamiania

### Tryb 1 — Pełny Docker (zalecany dla testów i nowych osób)

Uruchamia wszystkie serwisy w kontenerach — baza, backend, frontend, Nginx, monitoring.

```bash
# Uruchom wszystko
docker-compose up -d

# Uruchom z przebudowaniem obrazów (po zmianach w kodzie)
docker-compose up --build -d
```

### Tryb 2 — Lokalny development (backend/frontend w IDE, baza w Dockerze)

Uruchamia tylko infrastrukturę w Dockerze. Backend i frontend uruchamiasz z IDE lub terminala.
Zalecany gdy aktywnie rozwijasz kod i chcesz korzystać z hot-reload i debuggera.

```bash
# Uruchom tylko infrastrukturę
docker-compose up -d postgres loki prometheus grafana
```

Następnie uruchom backend z IntelliJ (profil `default`) i frontend przez `npm run dev`.

Szczegóły w sekcji [Praca lokalna](#praca-lokalna-intellij--kontener-z-bazą).

---

## Dostęp do serwisów

| Serwis | URL | Opis |
|--------|-----|------|
| **Aplikacja** | http://localhost | Frontend + backend przez Nginx |
| **Grafana** | http://localhost:3001 | Monitoring — login: `admin` / hasło z `.env` |
| **Prometheus** | http://localhost:9091 | Metryki — UI do eksploracji |
| **Loki** | http://localhost:3100 | Agregacja logów (API) |

---

## Przydatne komendy

### Zarządzanie kontenerami

```bash
# Sprawdź status kontenerów
docker-compose ps

# Zatrzymaj kontenery (dane zostają zachowane)
docker-compose stop

# Zatrzymaj i usuń kontenery oraz sieci (dane w volumes zostają)
docker-compose down

# ⚠️ Zatrzymaj i usuń WSZYSTKO łącznie z danymi bazy (używaj ostrożnie!)
docker-compose down -v
```

### Logi

```bash
# Logi wszystkich serwisów (live)
docker-compose logs -f

# Logi konkretnego serwisu
docker-compose logs -f backend
docker-compose logs -f postgres

# Ostatnie 100 linii logów backendu
docker logs sma-backend --tail 100
```

### Przebudowanie po zmianach w kodzie

```bash
# Przebuduj i uruchom konkretny serwis
docker-compose up --build -d backend

# Przebuduj wszystko
docker-compose up --build -d
```

### Dostęp do bazy danych

```bash
# Wejdź do kontenera PostgreSQL
docker exec -it sma-postgres psql -U $SPRING_DATASOURCE_USERNAME -d sma
```

### Aktualizacja po pobraniu zmian z Git

```bash
git pull
docker-compose up --build -d
```

---

## Monitoring — Grafana

### Dostępne dashboardy

Po zalogowaniu do Grafany (`http://localhost:3001`) dostępne są dashboardy w sekcji **Dashboards**.

### Logi aplikacji w Loki

1. Wejdź w **Explore** → wybierz data source **Loki**
2. Wpisz query:
   ```
   {app="sma-backend"}
   ```

### Metryki JVM w Prometheus

1. Wejdź w **Explore** → wybierz data source **Prometheus**
2. Przykładowe query:
   ```
   jvm_memory_used_bytes{application="sma-backend", area="heap"}
   ```

---

## Praca lokalna (IntelliJ + kontener z bazą)

Ten tryb pozwala na uruchamianie backendu bezpośrednio z IntelliJ (z debuggerem, hot-reload itp.)
przy jednoczesnym korzystaniu z bazy PostgreSQL działającej w kontenerze Docker.

### Krok 0 — Konfiguracja frontendu dla lokalnego devu

Stwórz plik `frontend/react/.env.local` (ignorowany przez Git):

```env
VITE_API_BASE_URL=http://localhost:8080
```

Plik ten nadpisuje domyślną wartość z `.env` i przekierowuje
requesty bezpośrednio do lokalnego backendu z pominięciem Nginx.

### Krok 1 — Uruchom infrastrukturę

```bash
docker-compose up -d postgres loki prometheus grafana
```

### Krok 2 — Konfiguracja IntelliJ

1. Otwórz **Run/Debug Configurations** dla modułu backendu
2. W zakładce **Environment variables** dodaj:
   ```
   SPRING_PROFILES_ACTIVE=default
   SPRING_DATASOURCE_USERNAME=<wartość z .env>
   SPRING_DATASOURCE_PASSWORD=<wartość z .env>
   JWT_SECRET=<wartość z .env>
   MAIL_HOST=<wartość z .env>
   MAIL_PORT=<wartość z .env>
   MAIL_USERNAME=<wartość z .env>
   MAIL_PASSWORD=<wartość z .env>
   ```

Profil `default` używa `application.properties` który łączy się z bazą na `localhost:5432`
(kontener Postgres z wystawionym portem).

### Krok 3 — Uruchom frontend lokalnie

```bash
cd frontend/react
npm install
npm run dev
```

Frontend będzie dostępny pod `http://localhost:5173`.

---

## Rozwiązywanie problemów

### Kontener backendu nie startuje

```bash
docker logs sma-backend --tail 50
```

Najczęstsze przyczyny:
- Brakujące zmienne w `.env` — sprawdź czy wszystkie pola są wypełnione
- Baza nie zdążyła wystartować — poczekaj chwilę i spróbuj: `docker-compose restart backend`
- Błąd migracji Flyway — sprawdź logi pod kątem `FlywayException`

### Błąd `Cannot connect to database`

Sprawdź czy kontener Postgres działa i jest `healthy`:
```bash
docker-compose ps postgres
```

### Port już zajęty

Jeśli port `80`, `5432`, `3001` lub `9091` jest zajęty przez inny proces:
```bash
# Windows — sprawdź co zajmuje port
netstat -ano | findstr :5432
```

Zmień mapowanie portów w `docker-compose.yml`, np. `"5433:5432"` dla Postgres,
i odpowiednio zaktualizuj `application.properties`.

### Resetowanie danych bazy

```bash
# ⚠️ Usuwa wszystkie dane — nieodwracalne!
docker-compose down -v
docker-compose up -d
```

---

## Kontrybutorzy

| Imię | Rola | Kontakt |
|------|------|---------|
| Konrad Chrzanowski | Lead Developer | — |

---

*Ostatnia aktualizacja: maj 2026*
