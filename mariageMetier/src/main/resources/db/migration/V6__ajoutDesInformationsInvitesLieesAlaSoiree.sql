
-- Ajout de colonnes dans la table des INVITES pour les informations liées à la soirée
alter table INVITE add COMMENTAIRE VARCHAR(255);
alter table INVITE add PARTICULARITE BOOLEAN;
alter table INVITE add PARTICIPANT_AUX_ANIMATIONS BOOLEAN;
