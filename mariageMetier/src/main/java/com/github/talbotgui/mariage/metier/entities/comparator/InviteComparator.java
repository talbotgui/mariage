package com.github.talbotgui.mariage.metier.entities.comparator;

import java.util.Comparator;

import com.github.talbotgui.mariage.metier.entities.Invite;

public class InviteComparator implements Comparator<Invite> {

	@Override
	public int compare(Invite arg0, Invite arg1) {
		return arg0.getId().compareTo(arg1.getId());
	}
}
