
-- Ajout des echecs de connexion de l'utilisateur
alter table UTILISATEUR add PREMIER_ECHEC TIMESTAMP;
alter table UTILISATEUR add SECOND_ECHEC TIMESTAMP;
alter table UTILISATEUR add TROISIEME_ECHEC TIMESTAMP;

-- Ajout du role d'un utilisateur
alter table UTILISATEUR add ROLE VARCHAR(20);
update UTILISATEUR set ROLE='ADMIN';