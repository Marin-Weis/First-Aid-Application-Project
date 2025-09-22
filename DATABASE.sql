-- Supprimmer la base de donnée
DROP DATABASE secours2030;

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS secours2030;
USE secours2030;

-- Table des secouristes
CREATE TABLE IF NOT EXISTS Secouriste (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    email VARCHAR(255) UNIQUE NOT NULL,
    motDePasse VARCHAR(255) NOT NULL,
    telephone VARCHAR(20)
);

-- Table des admins
CREATE TABLE IF NOT EXISTS Admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    email VARCHAR(255) UNIQUE NOT NULL,
    motDePasse VARCHAR(255) NOT NULL
);

--Table possède (compétences) :

CREATE TABLE Possede (
     idSecouriste INT,
     intitule VARCHAR(255),
     PRIMARY KEY (idSecouriste, intitule),
     FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id)
);

-- Table DPS :

CREATE TABLE DPS (
                     id INT AUTO_INCREMENT PRIMARY KEY,
                     label VARCHAR(255) NOT NULL,
                     date DATE NOT NULL,
                     heure_debut TIME NOT NULL,
                     heure_fin TIME NOT NULL,
                     sportAssocie VARCHAR(100),
                     codeSite INT,
                     lieu VARCHAR(255),
                     description TEXT,
                     couleur VARCHAR(20),
                     FOREIGN KEY (sportAssocie) REFERENCES Sport(nom)
);

-- Insert DPS :

INSERT INTO DPS (label, date, heure_debut, heure_fin, couleur)
VALUES ('DPS test du 18 juin', '2025-06-18', '08:00:00', '09:00:00', 'BLUE');

-- Table Affectation

CREATE TABLE Affectation (
                             idSecouriste BIGINT,
                             idDPS BIGINT,
                             PRIMARY KEY (idSecouriste, idDPS),
                             FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id),
                             FOREIGN KEY (idDPS) REFERENCES DPS(id)
);

-- Insert Affactation

INSERT INTO Affectation (idSecouriste, idDPS)
VALUES (1, 1);

INSERT INTO DPS (label, date, heure_debut, heure_fin, couleur)
VALUES ('DPS test du 18 juin', '2025-06-18', '16:00:00', '22:00:00', 'RED');


-- Table Sport :

CREATE TABLE Sport (
                       nom VARCHAR(100) PRIMARY KEY,
                       niveau_de_risque VARCHAR(50),
                       competences_requises TEXT
);



-- Insert d’un admin de test
INSERT INTO Admin (nom, prenom, email, motDePasse) VALUES
('Gouelo', 'Matthieu', 'matthieu.gouelo@ambulympics.fr', 'rootroot');

INSERT INTO Admin (nom, prenom, email, motDePasse) VALUES ('a', 'a', 'a@ambulympics.fr', 'a');


CREATE TABLE Affectation (
                             idSecouriste INT,
                             intitule VARCHAR(50),
                             idDPS INT,
                             PRIMARY KEY (idSecouriste, intitule, idDPS),
                             FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id),
                             FOREIGN KEY (idDPS) REFERENCES DPS(id)
);


-- Insérer :
INSERT INTO Sport (nom) VALUES ("Marathon");

-- Table Besoin : compétences nécessaires pour un DPS
CREATE TABLE IF NOT EXISTS Besoin (
    intituleComp VARCHAR(255),
    idDPS INT,
    nombreSecouriste INT NOT NULL,
    PRIMARY KEY (intituleComp, idDPS),
    FOREIGN KEY (idDPS) REFERENCES DPS(id)
);

INSERT INTO Secouriste (id, nom, prenom, email, motDePasse, telephone) VALUES
(3, 'Bernard', 'Enzo', 'enzo.bernard@gmail.com', 'mdp123', '0611223344'),
(4, 'Petit', 'Emma', 'emma.petit@gmail.com', 'mdp123', '0611223345'),
(5, 'Robert', 'Hugo', 'hugo.robert@gmail.com', 'mdp123', '0611223346'),
(6, 'Richard', 'Jade', 'jade.richard@gmail.com', 'mdp123', '0611223347'),
(7, 'Dubois', 'Louis', 'louis.dubois@gmail.com', 'mdp123', '0611223348'),
(8, 'Moreau', 'Léa', 'lea.moreau@gmail.com', 'mdp123', '0611223349'),
(9, 'Laurent', 'Nathan', 'nathan.laurent@gmail.com', 'mdp123', '0611223350'),
(10, 'Garcia', 'Camille', 'camille.garcia@gmail.com', 'mdp123', '0611223351'),
(11, 'Durand', 'Lucas', 'lucas.durand@gmail.com', 'mdp123', '0611223352'),
(12, 'Martin', 'Chloé', 'chloe.martin@gmail.com', 'mdp123', '0611223353');



-- Enzo Bernard
INSERT INTO Possede (idSecouriste, intitule) VALUES
(3, 'CP'), (3, 'CE'), (3, 'PSE2'), (3, 'PSE1'), (3, 'VPSP');

-- Emma Petit
INSERT INTO Possede (idSecouriste, intitule) VALUES
(4, 'CO'), (4, 'PSE1'), (4, 'PBC'), (4, 'PSE2');

-- Hugo Robert
INSERT INTO Possede (idSecouriste, intitule) VALUES
(5, 'PSE2'), (5, 'VPSP'), (5, 'CP'), (5, 'SSA');

-- Jade Richard
INSERT INTO Possede (idSecouriste, intitule) VALUES
(6, 'CP'), (6, 'PSE1'), (6, 'CO');

-- Louis Dubois
INSERT INTO Possede (idSecouriste, intitule) VALUES
(7, 'CO'), (7, 'PBF'), (7, 'PSE1');

-- Léa Moreau
INSERT INTO Possede (idSecouriste, intitule) VALUES
(8, 'PSE1'), (8, 'VPSP'), (8, 'SSA'), (8, 'PBC'), (8, 'PSE2');

-- Nathan Laurent
INSERT INTO Possede (idSecouriste, intitule) VALUES
(9, 'CE'), (9, 'PBC'), (9, 'PSE1'), (9, 'CP');

-- Camille Garcia
INSERT INTO Possede (idSecouriste, intitule) VALUES
(10, 'PSE2'), (10, 'PBF'), (10, 'VPSP'), (10, 'SSA');

-- Lucas Durand
INSERT INTO Possede (idSecouriste, intitule) VALUES
(11, 'PSE2'), (11, 'VPSP'), (11, 'CO'), (11, 'PSE1');

-- Chloé Martin
INSERT INTO Possede (idSecouriste, intitule) VALUES
(12, 'PSE1'), (12, 'SSA'), (12, 'CE'), (12, 'PBC');


CREATE TABLE Disponibilite (
                               idSecouriste INT NOT NULL,
                               jour INT NOT NULL,
                               mois INT NOT NULL,
                               annee INT NOT NULL,
                               PRIMARY KEY (idSecouriste, jour, mois, annee),
                               FOREIGN KEY (idSecouriste) REFERENCES Secouriste(id) ON DELETE CASCADE
);

