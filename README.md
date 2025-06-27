# 🧾 Gestion des Factures - Micro-Entrepreneur

Application JavaFX pour la gestion des factures destinée aux micro-entrepreneurs.

## 📋 Description

Cette application permet aux auto-entrepreneurs de gérer leurs factures en prenant en compte différents types de prestations :
- **Formations** : facturation à l'heure
- **Consultations** : facturation à la journée (TJM)

## 🚀 Fonctionnalités

### ✅ Fonctionnalités principales
1. **Authentification** - Connexion sécurisée
2. **Menu principal** - Navigation intuitive
3. **Gestion des prestations** :
   - Formation : date, heures, module, classe, entreprise
   - Consultation : date, description, TJM, entreprise
4. **Génération de factures** - PDF mensuel par client
5. **Bilans de chiffre d'affaires** - Mensuel, annuel, période

### 🎯 Fonctionnalités bonus
- Gestion complète des clients (CRUD)
- Calcul automatique des montants
- Interface moderne et responsive
- Base de données SQLite intégrée

## 🛠️ Technologies utilisées

- **Java 11** - Langage principal
- **JavaFX** - Interface graphique
- **SQLite** - Base de données
- **Maven** - Gestion des dépendances
- **iText** - Génération de PDF

## 📁 Structure du projet

```
src/
├── main/
│   ├── java/com/facturation/
│   │   ├── MainApp.java              # Point d'entrée
│   │   ├── controller/               # Contrôleurs FXML
│   │   ├── model/                    # Classes modèles
│   │   ├── dao/                      # Accès aux données
│   │   ├── service/                  # Services métier
│   │   └── util/                     # Utilitaires
│   └── resources/
│       ├── fxml/                     # Interfaces FXML
│       └── css/                      # Styles CSS
└── test/                             # Tests unitaires
```

## 🚀 Installation et lancement

### Prérequis
- Java 11 ou supérieur
- Maven 3.6+

### Installation
1. **Cloner le projet**
   ```bash
   git clone [url-du-projet]
   cd gestion-factures
   ```

2. **Compiler le projet**
   ```bash
   mvn clean compile
   ```

3. **Lancer l'application**
   ```bash
   mvn javafx:run
   ```

### Compte de test
- **Email** : `admin@example.com`
- **Mot de passe** : `admin123`

## 📊 Base de données

La base de données SQLite est créée automatiquement au premier lancement avec :
- Table `utilisateur` - Authentification
- Table `client` - Gestion des clients
- Table `prestation` - Prestations (formations/consultations)
- Table `facture` - Factures générées

## 🎨 Interface utilisateur

### Écran de connexion
- Interface moderne avec dégradé bleu
- Champs email et mot de passe
- Informations de connexion de test

### Menu principal
- Navigation par boutons
- Affichage de l'utilisateur connecté
- Accès à toutes les fonctionnalités

## 💰 Calculs financiers

### Formations
```
Montant = (heure_fin - heure_debut) × 70€/heure
```

### Consultations
```
Montant = TJM × nombre_jours
```

## 📄 Génération de factures

Les factures sont générées en PDF avec :
- En-tête avec informations de l'entreprise
- Détail des prestations du mois
- Calcul automatique des totaux
- Numérotation unique des factures

## 🔧 Configuration

### Fichier `pom.xml`
- Dépendances JavaFX
- SQLite JDBC
- iText pour PDF
- Configuration Maven

### Base de données
- Fichier `facturation.db` créé automatiquement
- Script SQL dans `facturation.sql`

## 🧪 Tests

```bash
# Lancer les tests
mvn test

# Tests unitaires inclus
- Test des DAO
- Test des calculs financiers
- Test de l'authentification
```

## 📝 Utilisation

### 1. Connexion
- Utiliser les identifiants de test
- L'application se connecte automatiquement

### 2. Ajouter une prestation
- Choisir le type (formation/consultation)
- Remplir les informations spécifiques
- Le montant est calculé automatiquement

### 3. Générer une facture
- Sélectionner le client et la période
- La facture PDF est générée automatiquement

### 4. Consulter les bilans
- Bilans mensuels et annuels
- Filtres par période
- Export des données

## 🐛 Dépannage

### Problèmes courants
1. **JavaFX non trouvé** : Vérifier la version de Java
2. **Base de données** : Vérifier les permissions d'écriture
3. **PDF** : Vérifier l'espace disque

### Logs
Les erreurs sont affichées dans la console avec des messages explicites.

## 📈 Évolutions futures

- [ ] Export Excel
- [ ] Envoi automatique par email
- [ ] Sauvegarde cloud
- [ ] Multi-utilisateurs
- [ ] Graphiques de tendances
- [ ] Mode hors ligne

## 👨‍💻 Développement

### Architecture MVC
- **Modèle** : Classes métier (Utilisateur, Client, Prestation, Facture)
- **Vue** : Fichiers FXML
- **Contrôleur** : Classes JavaFX Controller

### Patterns utilisés
- **DAO** : Accès aux données
- **Singleton** : Connexion base de données
- **Factory** : Génération de factures

## 📄 Licence

Projet d'étude - Libre d'utilisation

## 👥 Auteur

Projet réalisé dans le cadre d'un cours de Java avancé.

---

**Note** : Ce projet est conçu pour l'apprentissage et peut être amélioré selon les besoins spécifiques. 