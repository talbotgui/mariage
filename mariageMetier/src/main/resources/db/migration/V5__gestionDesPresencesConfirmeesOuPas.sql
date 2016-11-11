
-- Ajout de la colonne de confirmation de présence
alter table PRESENCE add CONFIRMEE BOOLEAN;
alter table PRESENCE add DATE_MAJ TIMESTAMP;
alter table PRESENCE add COMMENTAIRE VARCHAR(255);
alter table PRESENCE add PRESENT BOOLEAN;
