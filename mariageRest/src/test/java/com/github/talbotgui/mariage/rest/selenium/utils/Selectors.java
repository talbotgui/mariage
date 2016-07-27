package com.github.talbotgui.mariage.rest.selenium.utils;

import org.openqa.selenium.By;

public class Selectors {

	/**
	 *
	 */
	public static class Admin {
		public static class Button {
			public static final By AFFICHE_POPUP = By.id("button_afficher_popup_ajouter_utilisateur");
			public static final By AJOUT = By.id("button_ajouter_utilisateur");
			public static final By[] SUPPRIMER = { By.xpath("//div[@id='row0utilisateurs']/div[2]/a/span"),
					By.xpath("//div[@id='row1utilisateurs']/div[2]/a/span") };
		}

		public static class Input {
			public static final By LOGIN = By.id("login");
			public static final By MDP = By.id("mdp");
		}

		public static final By CASES[][] = { { By.xpath("//div[@id='row0utilisateurs']/div[1]/div") },
				{ By.xpath("//div[@id='row1utilisateurs']/div[1]/div") } };
	}

	/**
	 *
	 */
	public static class Commun {
		public static final By DIV_DATE_MARIAGE = By.cssSelector("#date > span");
		public static final By DIV_ERREUR = By.className("error");
		public static final By DIV_MARIES = By.id("maries");
		public static final By TABLEAU_MESSAGE_VIDE = By.cssSelector("div.jqx-grid-cell.jqx-grid-empty-cell > span");
	}

	/**
	 *
	 */
	public static class Index {
		public static class Button {
			public static final By MODIFIER = By.linkText("Modifier");
			public static final By NOUVEAU = By.linkText("Nouveau");
			public static final By SAUVEGARDER = By.linkText("Sauvegarder");
			public static final By SUPPRIMER = By.linkText("Supprimer");
		}

		public static class Cookie {
			public static final String ID_MARIAGE = "idMariage";
		}

		public static class Input {
			public static final By DATE_CELEBRATION = By.id("dateCelebration");
			public static final By MARIE1 = By.id("marie1");
			public static final By MARIE2 = By.id("marie2");
			public static final By SELECTION_MARIAGE = By.id("selectionMariage");
		}

		public static final String NOM_PAGE = "index.html";
	}

	/**
	 *
	 */
	public static class Invite {
		public static class Button {
			public static final By AFFICHE_POPUP_SAISIE = By.id("button_afficher_popup_ajouter");
			public static final By AJOUTER = By.id("button_ajouter");
		}

		public static class Input {
			public static final By FOYER = By.id("foyer");
			public static final By GROUPE = By.id("groupe");
			public static final By NOM = By.id("nom");
			public static final By PRENOM = By.id("prenom");
		}

		public static final By CASES[][] = { { By.xpath("//div[@id='row0invites']/div[1]/div"),
				By.xpath("//div[@id='row0invites']/div[2]/div"), By.xpath("//div[@id='row0invites']/div[3]/div"),
				By.xpath("//div[@id='row0invites']/div[4]/div") } };

		public static final By DIV_POPUP = By.xpath("//div[@id='popupAjoutInvite']");
	}

	/**
	 *
	 */
	public static class Menu {
		public static final By LIEN_ACCUEIL = By.linkText("Accueil");
		public static final By LIEN_ADMINISTRATION = By.linkText("Administration");
		public static final By LIEN_INVITES = By.linkText("Liste des invités");
		public static final By LIEN_PARAMETAPES = By.linkText("Paramètres - étapes");
	}

	public static class Monitoring {
		public static final By ENTETES[] = { By.xpath("//th[1]"), By.xpath("//th[2]") };
	}

	/**
	 *
	 */
	public static class ParametresEtape {
		public static class Button {
			public static final By AFFICHE_POPUP_CEREMONIE = By.id("button_afficher_popup_ajouter_ceremonie");
			public static final By AFFICHE_POPUP_REPAS = By.id("button_afficher_popup_ajouter_repas");
			public static final By AJOUT = By.id("button_ajouter_etapeRepas");
			public static final By CERE_AJOUT = By.id("button_ajouter_etapeCeremonie");
		}

		public static class Input {
			public static final By CERE_CELEBRANT = By.id("cere_celebrant");
			public static final By CERE_DATEHEURE = By.id("cere_dateHeure");
			public static final By CERE_LIEU = By.id("cere_lieu");
			public static final By CERE_NOM = By.id("cere_nom");
			public static final By REPA_DATEHEURE = By.id("repa_dateHeure");
			public static final By REPA_LIEU = By.id("repa_lieu");
			public static final By REPA_NOM = By.id("repa_nom");
		}

		public static final By CASES[][] = {
				{ By.xpath("//div[@id='row0etapes']/div[1]/div"), By.xpath("//div[@id='row0etapes']/div[2]/div"),
						By.xpath("//div[@id='row0etapes']/div[3]/div"), By.xpath("//div[@id='row0etapes']/div[4]/div"),
						By.xpath("//div[@id='row0etapes']/div[5]/div") },
				{ By.xpath("//div[@id='row1etapes']/div[1]/div"), By.xpath("//div[@id='row1etapes']/div[2]/div"),
						By.xpath("//div[@id='row1etapes']/div[3]/div"), By.xpath("//div[@id='row1etapes']/div[4]/div"),
						By.xpath("//div[@id='row1etapes']/div[5]/div") } };

		public static final By[] ENTETES = { By.xpath("//div[@id='columntableetapes']/div[1]/div/div"),
				By.xpath("//div[@id='columntableetapes']/div[2]/div/div"),
				By.xpath("//div[@id='columntableetapes']/div[3]/div/div"),
				By.xpath("//div[@id='columntableetapes']/div[4]/div/div"),
				By.xpath("//div[@id='columntableetapes']/div[5]/div/div"),
				By.xpath("//div[@id='columntableetapes']/div[6]/div/div") };
	}

	/** Classe utilitaire. */
	private Selectors() {
	}
}
