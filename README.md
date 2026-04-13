# Cabinet Médical - TP4 Architecture Event-Driven (EDA) avec Kafka

## 📋 Table des matières

- [Description du projet](#-description-du-projet)
- [Architecture](#️-architecture)
- [Technologies utilisées](#️-technologies-utilisées)
- [Concepts clés](#-concepts-clés)
- [Prérequis](#-prérequis)
- [Structure du projet](#-structure-du-projet)
- [Installation et démarrage](#-installation-et-démarrage)
- [Tests](#-tests)
- [Flux d'événements](#-flux-dévénements)
- [Auteur](#-auteur)

---

## 📖 Description du projet

Ce projet implémente une architecture orientée événements **(Event-Driven Architecture - EDA)** pour la gestion d'un cabinet médical. Contrairement au TP2 (SOA avec ESB) où les microservices communiquaient de manière synchrone via HTTP, ce TP4 utilise **Apache Kafka** pour une communication asynchrone basée sur des événements.

### Objectifs pédagogiques

- ✅ Comprendre l'architecture Event-Driven (EDA)
- ✅ Mettre en place Apache Kafka comme Event Broker
- ✅ Implémenter des Producers et Consumers d'événements
- ✅ Éliminer les appels REST inter-services
- ✅ Mettre en œuvre une Saga distribuée par chorégraphie
- ✅ Gérer la consistance éventuelle (Eventual Consistency)
- ✅ Implémenter des projections locales (CQRS)

### Limites de l'architecture synchrone (TP2)

| ❌ Problème | Description |
|---|---|
| Couplage fort | Les services dépendent directement les uns des autres |
| Dépendance à la disponibilité | Si un service tombe, tout s'arrête |
| Erreurs en cascade | Une erreur se propage à tous les services |
| Scalabilité limitée | Difficile de scaler indépendamment |
| Temps de réponse | Dépend des autres services |

### Avantages de l'architecture asynchrone (TP4)

| ✅ Avantage | Description |
|---|---|
| Communication asynchrone | Non-bloquante |
| Découplage fort | Services indépendants |
| Publish/Subscribe | Pattern flexible |
| Consistance éventuelle | Données synchronisées progressivement |
| Résilience | Les services continuent même si d'autres sont down |
| Scalabilité | Excellente |

---

## 🏗️ Architecture

### Architecture globale

```
┌─────────────────────────────────────────────────┐
│              Clients (Postman)                   │
└───────────────────┬─────────────────────────────┘
                    │
                    ▼
┌───────────────────────────────────────────────────┐
│         API Gateway (Port 8080)                   │
│         Point d'entrée externe                    │
└───────────────────┬───────────────────────────────┘
                    │
                    ▼
┌───────────────────────────────────────────────────┐
│           KAFKA EVENT BUS                         │
│       (Topics = Canaux d'événements)              │
│                                                   │
│  Topics:                                          │
│  • patient.created                                │
│  • medecin.created                                │
│  • rendezvous.created                             │
│  • consultation.created                           │
│  • facture.created                                │
│  • facture.failed (compensation)                  │
└───┬──────┬──────┬──────┬──────┬─────────────────┘
    │      │      │      │      │
    ▼      ▼      ▼      ▼      ▼
┌────────┐┌────────┐┌────────┐┌────────┐┌────────┐
│Patient ││Medecin ││Rendez- ││Consult ││Billing │
│Service ││Service ││Vous    ││Service ││Service │
│:8082   ││:8083   ││Service ││:8085   ││:8086   │
│        ││        ││:8084   ││        ││        │
│Producer││Producer││Prod/   ││Prod/   ││Prod/   │
│        ││        ││Consumer││Consumer││Consumer│
└───┬────┘└───┬────┘└───┬────┘└───┬────┘└───┬────┘
    │         │         │         │         │
    ▼         ▼         ▼         ▼         ▼
┌────────┐┌────────┐┌────────┐┌────────┐┌────────┐
│   H2   ││   H2   ││   H2   ││   H2   ││   H2   │
│Patient ││Medecin ││Rendez- ││Consult ││Billing │
│   DB   ││   DB   ││Vous DB ││   DB   ││   DB   │
└────────┘└────────┘└────────┘└────────┘└────────┘
```

### Principes architecturaux

**Event-Driven Architecture (EDA) :**
- Chaque service publie des événements lors de changements d'état
- Les services intéressés consomment ces événements de manière asynchrone
- Pas d'appels REST directs entre microservices

**CQRS (Command Query Responsibility Segregation) :**
- Séparation des opérations d'écriture (commandes) et de lecture (queries)
- Projections locales pour éviter les appels inter-services

**Saga par Chorégraphie :**
- Transaction distribuée sans orchestrateur central
- Chaque service réagit aux événements et déclenche les suivants
- Compensation automatique en cas d'échec (`facture.failed` → annulation rendez-vous)

---

## 🛠️ Technologies utilisées

| Technologie | Version | Usage |
|---|---|---|
| Java | 21 | Langage de programmation |
| Spring Boot | 3.2.0 | Framework applicatif |
| Spring Kafka | Latest | Intégration Kafka |
| Apache Kafka | Latest | Event Broker |
| Apache Zookeeper | Latest | Coordination Kafka |
| Spring Data JPA | 3.2.0 | Persistance des données |
| H2 Database | 2.2.x | Base en mémoire (une par service) |
| Docker | Latest | Conteneurisation (Kafka + Zookeeper) |
| Maven | 3.8+ | Gestion des dépendances |
| Lombok | 1.18.30 | Réduction du code boilerplate |

---

## 🎯 Concepts clés

### 1. Event-Driven Architecture (EDA)

**Définition :** Architecture où les microservices communiquent via des événements asynchrones plutôt que des appels synchrones.

> **Analogie :**
> - Architecture synchrone (TP2) = Téléphone : tu appelles et tu attends la réponse
> - Architecture asynchrone (TP4) = Email : tu envoies et tu continues ton travail, la réponse viendra plus tard

### 2. Apache Kafka

**Rôle :** Event Broker — système de messagerie distribué

- **Topics** : Catégories d'événements (ex: `patient.created`)
- **Producers** : Services qui publient des événements
- **Consumers** : Services qui consomment des événements
- **Partitions** : Division des topics pour la scalabilité

### 3. Projections locales

**Problème :** Comment éviter les appels REST entre services ?

**Solution :** Chaque service maintient une copie locale (projection) des données dont il a besoin.

```
rendezvous-service maintient :
  - PatientProjection  (copie locale des patients)
  - MedecinProjection  (copie locale des médecins)

Lors de la création d'un rendez-vous :
  → Vérification locale (pas d'appel REST au patient-service !)
```

### 4. Saga distribuée par chorégraphie

```
1. Patient créé        → patient.created
2. Médecin créé        → medecin.created
3. Rendez-vous créé    → rendezvous.created
4. Consultation créée  → consultation.created
5. Facture générée     → facture.created ✅
   OU
   Facture échoue      → facture.failed ❌
6. Si facture.failed   → Compensation : annulation du rendez-vous
```

### 5. Consistance éventuelle

```
1. Patient créé dans patient-service        (t=0s)
2. Événement publié sur Kafka               (t=0.1s)
3. Projection créée dans rendezvous-service (t=0.5s)
   → Pendant 0.5s, les données ne sont pas synchronisées
   → Après 0.5s, cohérence atteinte ✅
```

---

## ✅ Prérequis

### Logiciels requis

- **Java JDK 21**
- **Maven 3.8+**
- **Docker Desktop**
- **Un IDE** — VS Code, IntelliJ IDEA ou Eclipse
- **Postman** (optionnel)

### Vérification de l'installation

```bash
java -version
# Doit afficher : java version "21.x.x"

mvn -version
# Doit afficher : Apache Maven 3.8.x ou supérieur

docker --version
# Doit afficher : Docker version 20.x.x ou supérieur

docker-compose --version
# Doit afficher : Docker Compose version 2.x.x ou supérieur
```

---

## 📁 Structure du projet

```
cabinetMedicalTp4EDA/
│
├── pom.xml                              # POM parent
├── docker-compose.yml                   # Configuration Kafka + Zookeeper
│
├── patient-service/                     # Service Patient (Producer)
│   ├── pom.xml
│   └── src/main/
│       ├── java/ma/fsr/eda/patientservice/
│       │   ├── PatientServiceApplication.java
│       │   ├── model/Patient.java
│       │   ├── repository/PatientRepository.java
│       │   ├── service/PatientService.java
│       │   ├── web/PatientController.java
│       │   └── event/
│       │       ├── dto/PatientCreatedEvent.java
│       │       └── producer/PatientEventProducer.java
│       └── resources/application.properties
│
├── medecin-service/                     # Service Médecin (Producer)
│   └── [Structure identique à patient-service]
│
├── rendezvous-service/                  # Service RDV (Producer + Consumer)
│   └── src/main/java/ma/fsr/eda/rendezvousservice/
│       ├── model/
│       │   ├── RendezVous.java
│       │   └── projection/
│       │       ├── PatientProjection.java    # Copie locale
│       │       └── MedecinProjection.java    # Copie locale
│       ├── event/
│       │   ├── consumer/
│       │   │   ├── PatientEventConsumer.java
│       │   │   ├── MedecinEventConsumer.java
│       │   │   └── FactureEventConsumer.java # Compensation
│       │   └── producer/RendezVousEventProducer.java
│       └── resources/application.properties
│
├── consultation-service/                # Service Consultation (Producer + Consumer)
│   └── [Structure similaire]
│
└── billing-service/                     # Service Facturation (Producer + Consumer)
    └── [Structure similaire avec logique de Saga]
```

---

## 🚀 Installation et démarrage

### 1. Cloner le projet

```bash
git clone https://github.com/mu-kassimi/CabinetMedical-TP4-EDA.git
cd cabinetMedicalTp4EDA
```

### 2. Démarrer l'infrastructure Kafka

```bash
docker-compose up -d
```

Vérifier que les conteneurs sont lancés :

```bash
docker ps
```

### 3. Compiler le projet

```bash
mvn clean install
```

### 4. Démarrer les microservices

Ouvrir **5 terminaux séparés** :

```bash
# Terminal 1 - Patient Service (Port 8082)
cd patient-service && mvn spring-boot:run

# Terminal 2 - Médecin Service (Port 8083)
cd medecin-service && mvn spring-boot:run

# Terminal 3 - RendezVous Service (Port 8084)
cd rendezvous-service && mvn spring-boot:run

# Terminal 4 - Consultation Service (Port 8085)
cd consultation-service && mvn spring-boot:run

# Terminal 5 - Billing Service (Port 8086)
cd billing-service && mvn spring-boot:run
```

### 5. Vérifier que tout est UP

| Service | Port | Rôle |
|---|---|---|
| Kafka | 9092 | Event Broker |
| Zookeeper | 2181 | Coordination Kafka |
| patient-service | 8082 | Producer |
| medecin-service | 8083 | Producer |
| rendezvous-service | 8084 | Producer + Consumer |
| consultation-service | 8085 | Producer + Consumer |
| billing-service | 8086 | Producer + Consumer |

---

## 🧪 Tests

### 📝 Test 1 : Création de Patient et Propagation d'Événement

**Objectif :** Vérifier que la création d'un patient déclenche un événement Kafka et met à jour les projections locales.

**Requête :**
```http
POST http://localhost:8080/api/patients
Content-Type: application/json

{
  "nom": "Alami",
  "email": "alami@email.com",
  "telephone": "0612345678"
}
```

**Réponse attendue (201 Created) :**


<img width="1338" height="829" alt="Screenshot 2026-04-13 011500" src="https://github.com/user-attachments/assets/27f97c35-5210-46e4-9cc5-6b5fd65f46d5" />



> 📝 **Noter l'ID du patient pour la suite !**

**Logs attendus :**

```
# patient-service
📤 Publishing event: patient.created for patientId: 2f1d36a0-...

# rendezvous-service
📥 Received event: patient.created for patientId: 2f1d36a0-...
✅ Patient projection saved: 2f1d36a0-...
```

---

### 📝 Test 2 : Création de Médecin et Propagation

**Requête :**
```http
POST http://localhost:8080/api/medecins
Content-Type: application/json

{
  "nom": "Aziz",
  "specialite": "Cardiologie",
  "email": "aziz@hopital.ma"
}
```

**Réponse attendue (201 Created) :**


<img width="1339" height="900" alt="Screenshot 2026-04-13 011354" src="https://github.com/user-attachments/assets/72dae9a6-c024-43c0-9174-af7bfae53e65" />



> 📝 **Noter l'ID du médecin !**

---

### 📝 Test 3 : Création de Rendez-vous et Cascade d'Événements

**Objectif :** Vérifier la cascade complète : rendez-vous → consultation → facture.

**Requête :**
POST http://localhost:8080/api/rendezvous
Content-Type: application/json

{
  "patientId": "xxxxxxxxxxxx",
  "medecinId": "yyyyyyyyyyy",
  "dateRendezVous": "2026-04-20T10:00:00"
}

<img width="1330" height="862" alt="Screenshot 2026-04-13 011622" src="https://github.com/user-attachments/assets/76f7d43b-fac8-47b8-aec8-2545326b3d9b" />



**Ce qui se passe automatiquement :**

```
1️⃣  Rendez-vous créé
    └─ 📤 Événement: rendezvous.created

2️⃣  Consultation-service écoute
    └─ 📥 Reçoit: rendezvous.created
    └─ ✅ Crée automatiquement une consultation
    └─ 📤 Événement: consultation.created

3️⃣  Billing-service écoute
    └─ 📥 Reçoit: consultation.created
    └─ 📤 Événement: facture.created ✅ OU facture.failed ❌
```

**Vérifier la consultation (créée automatiquement sans POST) :**

GET http://localhost:8080/api/consultations



<img width="1337" height="905" alt="Screenshot 2026-04-13 011816" src="https://github.com/user-attachments/assets/bf67576a-75d5-41d1-a31a-fbf924a253ff" />




**Vérifier la facture :**

GET http://localhost:8080/api/factures


<img width="1326" height="823" alt="Screenshot 2026-04-13 014016" src="https://github.com/user-attachments/assets/b67c0d94-e29a-48f2-9106-e3f960b29e18" />


---

### 📊 Résumé des Tests

| Test | Objectif | Résultat Attendu |
|---|---|---|
| Test 1 | Création Patient + Événement | ✅ Événement publié + Projection créée |
| Test 2 | Création Médecin + Événement | ✅ Événement publié + Projection créée |
| Test 3 | Cascade complète (RDV → Consult → Facture) | ✅ Tous les services réagissent automatiquement |

---

## 🔄 Flux d'événements

### Flux complet (Happy Path)


1. POST /patients
   └─ patient.created
      └─ rendezvous-service (projection)

2. POST /medecins
   └─ medecin.created
      └─ rendezvous-service (projection)

3. POST /rendezvous
   └─ rendezvous.created
      └─ consultation-service
         └─ consultation.created
            └─ billing-service
               └─ facture.created ✅


## 👤 Auteur

**Mustapha Kassimi**  
Master IPS — Systèmes Distribués Basés sur les Microservices  
Faculté des Sciences de Rabat — 2025/2026

---

## 🙏 Remerciements

Encadrant : **Pr. Jaouad OUHSSAINE**  
Contact : jaouad.ouhs@gmail.com | jaouad_ouhssaine@um5.ac.ma
