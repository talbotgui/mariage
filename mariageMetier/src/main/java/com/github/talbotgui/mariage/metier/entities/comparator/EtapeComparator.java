package com.github.talbotgui.mariage.metier.entities.comparator;

import java.util.Comparator;

import com.github.talbotgui.mariage.metier.entities.Etape;

public class EtapeComparator implements Comparator<Etape> {

	@Override
	public int compare(Etape arg0, Etape arg1) {
		return arg0.getId().compareTo(arg1.getId());
	}
}
