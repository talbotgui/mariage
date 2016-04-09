package com.github.talbotgui.mariage.metier.entities.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.github.talbotgui.mariage.metier.entities.Invite;

public class InviteComparator implements Serializable, Comparator<Invite> {
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(final Invite arg0, final Invite arg1) {
		return arg0.getId().compareTo(arg1.getId());
	}
}
