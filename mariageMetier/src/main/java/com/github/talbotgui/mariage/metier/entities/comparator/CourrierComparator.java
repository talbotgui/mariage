package com.github.talbotgui.mariage.metier.entities.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.github.talbotgui.mariage.metier.entities.Courrier;

public class CourrierComparator implements Serializable, Comparator<Courrier> {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(final Courrier arg0, final Courrier arg1) {
		return arg0.getId().compareTo(arg1.getId());
	}
}
