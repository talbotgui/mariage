package com.github.talbotgui.mariage.rest.controleur;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.github.talbotgui.mariage.metier.entities.Etape;
import com.github.talbotgui.mariage.metier.entities.EtapeCeremonie;
import com.github.talbotgui.mariage.metier.entities.EtapeRepas;
import com.github.talbotgui.mariage.metier.entities.Invite;
import com.github.talbotgui.mariage.metier.entities.Mariage;
import com.github.talbotgui.mariage.metier.entities.PresenceEtape;

public class ObjectMother {

	private static PresenceEtape a(Etape e) {
		return new PresenceEtape(e, false);
	}

	public static Mariage creeMariageSimple() throws ParseException {
		SimpleDateFormat da = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dh = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Mariage mariage = new Mariage(da.parse("15/07/2016"), "Marie", "Guillaume");

		Etape a = new EtapeCeremonie("Accueil", dh.parse("15/07/2016 13:00"), "Parking du chateau");
		Etape b = new EtapeCeremonie("Eglise", dh.parse("15/07/2016 14:00"), "Eglise de Ham", "Abbé Fromont");
		Etape c = new EtapeCeremonie("Marie", dh.parse("15/07/2016 15:00"), "Marie de Ham", "Grégory Labille");
		Etape d = new EtapeCeremonie("Photos", dh.parse("15/07/2016 16:00"), "Park Délicourt");
		Etape e = new EtapeRepas("Vin d'honneur", dh.parse("15/07/2016 17:00"), "Grenier de Rouez");
		Etape f = new EtapeRepas("Repas", dh.parse("15/07/2016 21:00"), "Grenier de Rouez");
		mariage.addEtape(a);
		mariage.addEtape(b);
		mariage.addEtape(c);
		mariage.addEtape(d);
		mariage.addEtape(e);
		mariage.addEtape(f);

		Invite i01 = new Invite("G Alpes", "G Fabrice", p(a), p(b), p(c), a(d), p(e), p(f));
		Invite i02 = new Invite("G Alpes", "G Anais", p(a), a(b), p(c), p(d), a(e), p(f));
		Invite i03 = new Invite("G Alpes", "G Celine", p(a), a(b), p(c), a(d), a(e), p(f));
		Invite i04 = new Invite("G Alpes", "G Isabelle", p(a), p(b), p(c), p(d), p(e), p(f));
		Invite i05 = new Invite("G Alpes", "G Jean", p(a), p(b), p(c), a(d), a(e), p(f));
		Invite i06 = new Invite("Baugy", "T JP", p(a), p(b), a(c), p(d), p(e), a(f));
		Invite i07 = new Invite("Baugy", "T Annick", p(a), p(b), p(c), p(d), p(e), p(f));
		Invite i08 = new Invite("Chartres", "T Jessica", p(a), p(b), a(c), p(d), p(e), a(f));
		Invite i09 = new Invite("Chartres", "T Axel", p(a), a(b), p(c), p(d), a(e), p(f));
		Invite i10 = new Invite("Chartres", "T Johan", p(a), p(b), a(c), p(d), a(e), p(f));
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

	private static PresenceEtape p(Etape e) {
		return new PresenceEtape(e, true);
	}

}
