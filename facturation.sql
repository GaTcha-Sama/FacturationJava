-- =====================================================
-- BASE DE DONNÉES : GESTION DES FACTURES MICRO-ENTREPRENEUR
-- =====================================================

-- Table des utilisateurs (authentification)
CREATE TABLE IF NOT EXISTS utilisateur (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    mot_de_passe TEXT NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Table des clients
CREATE TABLE IF NOT EXISTS client (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    entreprise TEXT,
    contact TEXT,
    email TEXT,
    telephone TEXT,
    adresse TEXT,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Table des prestations
CREATE TABLE IF NOT EXISTS prestation (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    type TEXT CHECK(type IN ('formation', 'consultation')) NOT NULL,
    date TEXT NOT NULL, -- Format ISO (YYYY-MM-DD)
    heure_debut TEXT,    -- Format HH:MM (pour les formations)
    heure_fin TEXT,      -- Format HH:MM (pour les formations)
    description TEXT,    -- Pour les consultations
    tjm REAL,           -- Taux Journalier Moyen (pour les consultations)
    module TEXT,         -- Module de formation
    classe TEXT,         -- Classe pour la formation
    entreprise TEXT,     -- Entreprise cliente
    client_id INTEGER NOT NULL,
    montant_calcule REAL, -- Montant calculé automatiquement
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Table des factures
CREATE TABLE IF NOT EXISTS facture (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_facture TEXT UNIQUE NOT NULL, -- Numéro de facture unique
    mois INTEGER NOT NULL,
    annee INTEGER NOT NULL,
    montant_total REAL NOT NULL,
    statut TEXT DEFAULT 'en_attente' CHECK(statut IN ('en_attente', 'payee', 'annulee')),
    fichier_pdf TEXT, -- chemin vers le fichier PDF généré
    client_id INTEGER NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    date_paiement DATETIME,
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE
);

-- Table de liaison facture-prestation (pour détailler les factures)
CREATE TABLE IF NOT EXISTS facture_prestation (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    facture_id INTEGER NOT NULL,
    prestation_id INTEGER NOT NULL,
    FOREIGN KEY (facture_id) REFERENCES facture(id) ON DELETE CASCADE,
    FOREIGN KEY (prestation_id) REFERENCES prestation(id) ON DELETE CASCADE
);

-- =====================================================
-- DONNÉES DE TEST
-- =====================================================

-- Insertion d'un utilisateur de test
INSERT OR IGNORE INTO utilisateur (nom, email, mot_de_passe) 
VALUES ('Admin', 'admin@example.com', 'password123');

-- Insertion de quelques clients de test
INSERT OR IGNORE INTO client (nom, entreprise, contact, email) VALUES
('Jean Dupont', 'Entreprise ABC', 'Jean Dupont', 'jean.dupont@abc.com'),
('Marie Martin', 'Société XYZ', 'Marie Martin', 'marie.martin@xyz.com'),
('Pierre Durand', 'Startup Innov', 'Pierre Durand', 'pierre.durand@innov.com');

-- Insertion de quelques prestations de test
INSERT OR IGNORE INTO prestation (type, date, heure_debut, heure_fin, description, tjm, module, classe, entreprise, client_id, montant_calcule) VALUES
('formation', '2024-01-15', '09:00', '17:00', NULL, NULL, 'Java Avancé', 'Développeurs', 'Entreprise ABC', 1, 560.0),
('consultation', '2024-01-20', NULL, NULL, 'Audit système informatique', 350.0, NULL, NULL, 'Société XYZ', 2, 350.0),
('formation', '2024-01-25', '14:00', '18:00', NULL, NULL, 'Spring Framework', 'Équipe Tech', 'Startup Innov', 3, 280.0);

-- =====================================================
-- INDEX POUR OPTIMISER LES PERFORMANCES
-- =====================================================

-- Index sur les clés étrangères
CREATE INDEX IF NOT EXISTS idx_prestation_client_id ON prestation(client_id);
CREATE INDEX IF NOT EXISTS idx_facture_client_id ON facture(client_id);
CREATE INDEX IF NOT EXISTS idx_facture_prestation_facture_id ON facture_prestation(facture_id);
CREATE INDEX IF NOT EXISTS idx_facture_prestation_prestation_id ON facture_prestation(prestation_id);

-- Index sur les dates pour les requêtes de bilan
CREATE INDEX IF NOT EXISTS idx_prestation_date ON prestation(date);
CREATE INDEX IF NOT EXISTS idx_facture_mois_annee ON facture(mois, annee);

-- Index sur les types de prestation
CREATE INDEX IF NOT EXISTS idx_prestation_type ON prestation(type);

-- =====================================================
-- VUES UTILES POUR LES RAPPORTS
-- =====================================================

-- Vue pour le bilan mensuel des prestations
CREATE VIEW IF NOT EXISTS vue_bilan_mensuel AS
SELECT 
    strftime('%Y-%m', date) as periode,
    type,
    COUNT(*) as nombre_prestations,
    SUM(montant_calcule) as chiffre_affaires
FROM prestation 
GROUP BY strftime('%Y-%m', date), type
ORDER BY periode DESC, type;

-- Vue pour le détail des factures
CREATE VIEW IF NOT EXISTS vue_detail_factures AS
SELECT 
    f.id,
    f.numero_facture,
    f.mois,
    f.annee,
    f.montant_total,
    f.statut,
    c.nom as client_nom,
    c.entreprise as client_entreprise,
    COUNT(fp.prestation_id) as nombre_prestations
FROM facture f
JOIN client c ON f.client_id = c.id
LEFT JOIN facture_prestation fp ON f.id = fp.facture_id
GROUP BY f.id, f.numero_facture, f.mois, f.annee, f.montant_total, f.statut, c.nom, c.entreprise; 