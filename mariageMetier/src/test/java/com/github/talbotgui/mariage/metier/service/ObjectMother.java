package com.github.talbotgui.mariage.metier.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Foyer;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public class ObjectMother {

	public static Mariage creeMariageAvecInvitations() throws ParseException {
		final Mariage mariage = creeMariageSimple();

		final Courrier c1 = ((List<Courrier>) mariage.getCourriers()).get(0);
		final Courrier c2 = ((List<Courrier>) mariage.getCourriers()).get(1);
		int i = 0;
		for (final Foyer f : mariage.getFoyers()) {
			f.addCourrierInvitation(c1);
			if (i % 2 == 0) {
				f.addCourrierInvitation(c2);
			}
			i++;
		}

		return mariage;
	}

	public static Mariage creeMariageSeul() throws ParseException {
		final SimpleDateFormat da = new SimpleDateFormat("dd/MM/yyyy");
		return new Mariage(da.parse("01/01/2016"), "M", "G");
	}

	public static Mariage creeMariageSimple() throws ParseException {
		final SimpleDateFormat sdhf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		final Mariage mariage = creeMariageSeul();

		final Etape a = new EtapeCeremonie(1, "Accueil", sdhf.parse("01/01/2016 13:00"), "Parking", "quelquUn");
		final Etape b = new EtapeCeremonie(2, "Eglise", sdhf.parse("01/01/2016 14:00"), "Eglise", "Curé");
		final Etape c = new EtapeCeremonie(3, "Mairie", sdhf.parse("01/01/2016 15:00"), "Mairie", "Maire");
		final Etape d = new EtapeCeremonie(4, "Photos", sdhf.parse("01/01/2016 16:00"), "Park");
		final Etape e = new EtapeRepas(5, "Vin d'honneur", sdhf.parse("01/01/2016 17:00"), "Salle");
		final Etape f = new EtapeRepas(6, "Repas", sdhf.parse("01/01/2016 21:00"), "Salle");
		mariage.addEtape(a);
		mariage.addEtape(b);
		mariage.addEtape(c);
		mariage.addEtape(d);
		mariage.addEtape(e);
		mariage.addEtape(f);

		final Courrier c1 = new Courrier("Courrier1", sdf.parse("15/10/2015"));
		c1.addEtape(a);
		c1.addEtape(b);
		final Courrier c2 = new Courrier("Courrier2", sdf.parse("16/10/2015"));
		c2.addEtape(c);
		c2.addEtape(d);
		c2.addEtape(e);
		c2.addEtape(f);
		mariage.addCourrier(c1);
		mariage.addCourrier(c2);

		final Foyer foyer1 = new Foyer("G1", "F1", "add1", null, null);
		final Foyer foyer2 = new Foyer("G1", "F2", "add1", null, null);
		final Foyer foyer3 = new Foyer("G2", "F3", "add1", null, null);
		mariage.addFoyer(foyer1);
		mariage.addFoyer(foyer2);
		mariage.addFoyer(foyer3);

		foyer1.addInvite(new Invite(null, "G F", "X", Age.adulte, foyer1));
		foyer1.addInvite(new Invite(null, "G A", "X", Age.adulte, foyer1));
		foyer1.addInvite(new Invite(null, "G C", "X", Age.adulte, foyer1));
		foyer2.addInvite(new Invite(null, "G I", "X", Age.adulte, foyer2));
		foyer2.addInvite(new Invite(null, "G J", "X", Age.adulte, foyer2));
		foyer2.addInvite(new Invite(null, "T P", "X", Age.adulte, foyer2));
		foyer3.addInvite(new Invite(null, "T A", "X", Age.adulte, foyer3));
		foyer3.addInvite(new Invite(null, "T J", "X", Age.adulte, foyer3));
		foyer3.addInvite(new Invite(null, "T A", "X", Age.adulte, foyer3));
		foyer3.addInvite(new Invite(null, "T J", "X", Age.adulte, foyer3));

		return mariage;
	}

}
