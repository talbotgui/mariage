
-- Modifications pour COURRIER :
--   suppression d'une colonne inutilisée
--   renommage de la table de jointure
alter table COURRIER drop DATE_ENVOI_REALISE;
alter table COURRIER_ETAPES_INVITATION rename to COURRIER_ETAPES;
alter table COURRIER_ETAPES alter column ETAPES_INVITATION rename to ETAPES;

-- Modifications pour FOYER :
--   creation de la table FOYER
--   alimentation de la table FOYER et des FK dans INVITE
--   ajout des contraintes de FK
create table FOYER (ID bigint generated by default as identity (start with 1), ADRESSE varchar(255), EMAIL varchar(255), GROUPE varchar(255), NOM varchar(255), TELEPHONE varchar(255), MARIAGE_ID bigint, primary key (ID));
insert into FOYER (NOM, GROUPE, MARIAGE_ID) select distinct FOYER, GROUPE, MARIAGE_ID from INVITE where MARIAGE_ID is not null;
alter table INVITE add FOYER_ID bigint;
update INVITE i set FOYER_ID = (select f.ID from FOYER f where i.FOYER = f.nom);
update FOYER f set (ADRESSE, TELEPHONE, EMAIL) = (select i.ADRESSE, i.TELEPHONE, i.EMAIL from INVITE i where i.FOYER_ID = f.ID and ROWNUM() = 1);
alter table INVITE drop ADRESSE;
alter table INVITE drop FOYER;
alter table INVITE drop GROUPE;
alter table INVITE drop TELEPHONE;
alter table INVITE drop EMAIL;
alter table INVITE add constraint FK_pn772e0yd148arfvd9bdo1fxb foreign key (FOYER_ID) references FOYER;
alter table FOYER add constraint FK_k6ek1dt2ulk3gigcrsf252gb1 foreign key (MARIAGE_ID) references MARIAGE;

-- Modifications pour les invitations et les presences
create table FOYER_COURRIER_INVITATION (FOYER_ID bigint, COURRIER_ID bigint, primary key (COURRIER_ID, FOYER_ID));
create table INVITE_ETAPE_PRESENCE (INVITE_ID bigint, ETAPE_ID bigint, primary key (ETAPE_ID, INVITE_ID));
alter table INVITE_ETAPE_PRESENCE add constraint FK_8x89g3jard9vjyhvn0ikcnoq0 foreign key (INVITE_ID) references INVITE;
alter table FOYER_COURRIER_INVITATION add constraint FK_gmxmxvkp51h0yj528o8ytq2uy foreign key (FOYER_ID) references FOYER;
alter table FOYER_COURRIER_INVITATION add constraint FK_7wyohpk6e15tx4h4dn86yqean foreign key (COURRIER_ID) references COURRIER;
alter table INVITE_ETAPE_PRESENCE add constraint FK_snkb1hsrk0wu5if6rya2ennsx foreign key (ETAPE_ID) references ETAPE;
drop table PRESENCE_ETAPE;
