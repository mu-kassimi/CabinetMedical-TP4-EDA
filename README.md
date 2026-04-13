# TP4 — Architecture Event-Driven avec Apache Kafka
### Cabinet Médical | Master IPS — Systèmes Distribués Basés sur les Microservices

**Encadrant :** Jaouad OUHSSAINE  
**Contact :** jaouad.ouhs@gmail.com · jaouad_ouhssaine@um5.ac.ma

---

## Contexte & Motivation

Le TP3 reposait sur une communication **REST synchrone** entre microservices, ce qui engendrait plusieurs problèmes :

- Couplage fort entre les services
- Dépendance à la disponibilité des autres services
- Risque de pannes en cascade
- Scalabilité limitée
- Temps de réponse tributaire des services tiers

Le TP4 fait évoluer cette architecture vers un modèle **asynchrone et découplé**, basé sur Apache Kafka.

---

## Objectif

Migrer vers une **Architecture Event-Driven (EDA)** en introduisant les principes suivants :

| Principe | Description |
|---|---|
| Communication asynchrone | Les services émettent et consomment des événements sans attente directe |
| Découplage fort | Les services sont indépendants les uns des autres |
| Publish / Subscribe | Modèle d'échange via des topics Kafka |
| Consistance éventuelle | La cohérence est garantie à terme, pas immédiatement |
| Saga par chorégraphie | Coordination distribuée sans orchestrateur central |

---

## Architecture Globale

Les services ne communiquent **plus via REST**. Toutes les interactions internes passent par le **bus d'événements Kafka**.

```
Client HTTP
    │
    ▼
API Gateway  (exposition REST externe uniquement)
    │
    ▼
Kafka Event Bus
    ├── Microservice A  →  Base de données A
    ├── Microservice B  →  Base de données B
    └── Microservice C  →  Base de données C
```

Chaque microservice est **autonome** et possède sa propre base de données.

---

## Déploiement Docker

### 1. Compiler le projet (sans exécuter les tests)
```bash
mvn -DskipTests package
```

### 2. Construire les images Docker (sans cache)
```bash
docker compose build --no-cache
```

### 3. Démarrer les conteneurs en arrière-plan
```bash
docker compose up -d --build
```

### 4. Arrêter et supprimer les conteneurs
```bash
docker compose down
```
