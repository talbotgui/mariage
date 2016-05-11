package com.github.talbotgui.mariage.metier.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Age;
import com.github.talbotgui.mariage.metier.entities.Courrier;
import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;

public class ObjectMother {

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
		c1.addEtapeInvitation(a);
		c1.addEtapeInvitation(b);
		final Courrier c2 = new Courrier("Courrier2", sdf.parse("16/10/2015"));
		c2.addEtapeInvitation(c);
		c2.addEtapeInvitation(d);
		c2.addEtapeInvitation(e);
		c2.addEtapeInvitation(f);
		mariage.addCourrier(c1);
		mariage.addCourrier(c2);

		final Invite i01 = new Invite("G", "F1", "G F", "X", Age.adulte);
		final Invite i02 = new Invite("G", "F1", "G A", "X", Age.adulte);
		final Invite i03 = new Invite("G", "F2", "G C", "X", Age.adulte);
		final Invite i04 = new Invite("G", "F2", "G I", "X", Age.adulte);
		final Invite i05 = new Invite("G", "F3", "G J", "X", Age.adulte);
		final Invite i06 = new Invite("B", "F3", "T P", "X", Age.adulte);
		final Invite i07 = new Invite("B", "F3", "T A", "X", Age.adulte);
		final Invite i08 = new Invite("C", "F4", "T J", "X", Age.adulte);
		final Invite i09 = new Invite("C", "F4", "T A", "X", Age.adulte);
		final Invite i10 = new Invite("C", "F5", "T J", "X", Age.adulte);
		mariage.addInvite(i01);
		mariage.addInvite(i02);
		mariage.addInvite(i03);
		mariage.addInvite(i04);
		mariage.addInvite(i05);
		mariage.addInvite(i06);
		mariage.addInvite(i07);
		mariage.addInvite(i08);
		mariage.addInvite(i09);
		mariage.addInvite(i10);

		return mariage;
	}

}
