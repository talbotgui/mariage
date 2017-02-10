package com.github.talbotgui.mariage.rest.controleur.dto;

import java.lang.reflect.Method;
import java.util.Collection;

import com.github.talbotgui.mariage.metier.dto.DTOUtils;

public class DtoAvecChoix {

	private Boolean choix0;
	private Boolean choix1;
	private Boolean choix2;
	private Boolean choix3;
	private Boolean choix4;
	private Boolean choix5;
	private Boolean choix6;
	private Boolean choix7;
	private Boolean choix8;
	private Boolean choix9;
	private Object dto;

	protected DtoAvecChoix() {
		super();
	}

	public DtoAvecChoix(final Object entite, final Class<?> dtoClass, final Method getterAttributDuChoix,
			final Collection<?> choixPossibles) throws ReflectiveOperationException {

		this.dto = DTOUtils.creerDto(entite, dtoClass);

		if (choixPossibles != null) {
			final Collection<?> objetsLies = (Collection<?>) getterAttributDuChoix.invoke(entite);
			int cpt = 0;
			for (final Object o : choixPossibles) {
				final boolean valeur = objetsLies.contains(o);
				this.setChoix(cpt, valeur);
				cpt++;
			}
		}
	}

	public Boolean getChoix0() {
		return this.choix0;
	}

	public Boolean getChoix1() {
		return this.choix1;
	}

	public Boolean getChoix2() {
		return this.choix2;
	}

	public Boolean getChoix3() {
		return this.choix3;
	}

	public Boolean getChoix4() {
		return this.choix4;
	}

	public Boolean getChoix5() {
		return this.choix5;
	}

	public Boolean getChoix6() {
		return this.choix6;
	}

	public Boolean getChoix7() {
		return this.choix7;
	}

	public Boolean getChoix8() {
		return this.choix8;
	}

	public Boolean getChoix9() {
		return this.choix9;
	}

	public Object getDto() {
		return this.dto;
	}

	private void setChoix(final int i, final boolean valeur) {
		if (i == 0) {
			this.setChoix0(valeur);
		} else if (i == 1) {
			this.setChoix1(valeur);
		} else if (i == 2) {
			this.setChoix2(valeur);
		} else if (i == 3) {
			this.setChoix3(valeur);
		} else if (i == 4) {
			this.setChoix4(valeur);
		} else if (i == 5) {
			this.setChoix5(valeur);
		} else if (i == 6) {
			this.setChoix6(valeur);
		} else if (i == 7) {
			this.setChoix7(valeur);
		} else if (i == 8) {
			this.setChoix8(valeur);
		} else if (i == 9) {
			this.setChoix9(valeur);
		}
	}

	public void setChoix0(final Boolean choix0) {
		this.choix0 = choix0;
	}

	public void setChoix1(final Boolean choix1) {
		this.choix1 = choix1;
	}

	public void setChoix2(final Boolean choix2) {
		this.choix2 = choix2;
	}

	public void setChoix3(final Boolean choix3) {
		this.choix3 = choix3;
	}

	public void setChoix4(final Boolean choix4) {
		this.choix4 = choix4;
	}

	public void setChoix5(final Boolean choix5) {
		this.choix5 = choix5;
	}

	public void setChoix6(final Boolean choix6) {
		this.choix6 = choix6;
	}

	public void setChoix7(final Boolean choix7) {
		this.choix7 = choix7;
	}

	public void setChoix8(final Boolean choix8) {
		this.choix8 = choix8;
	}

	public void setChoix9(final Boolean choix9) {
		this.choix9 = choix9;
	}

	public void setDto(final Object dto) {
		this.dto = dto;
	}

}
