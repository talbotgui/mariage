# Titre de la fonctionnalité
Feature: Page d'acceuil 

# Préparation du test
#Background: 
	
# Scénario avec création simple	
Scenario: Création d'un mariage 
	Given utilisateur sur page index 
	When cree mariage "Femme" et "Homme" le "01/01/2017" 
	Then mariage "Femme" et "Homme" selectionne
	 

# Scénario avec création multiple
#Scenario Outline: Création de mariages  
#	Given aucun mariage sélectionné 
#	When Je modifie la Personne <id> avec <nom> 
#	Then J'obtiens la Personne d'identifiant <id> contenant les données <prenom>, <nom>, <naissance> 
#	
#	Examples: 
#		| id | prenom | nom                   | naissance |
#		| 3  | Leia   | Organa Solo Skywalker | 19 BBY    |