package com.github.talbotgui.mariage.rest.controleur.utils;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageWrapperForTest<T> extends PageImpl<T> {
	private static final long serialVersionUID = 1L;

	@JsonCreator(mode = Mode.PROPERTIES)
	public PageWrapperForTest(@JsonProperty("content") final List<T> content, @JsonProperty("number") final int number,
			@JsonProperty("size") final int size, @JsonProperty("totalElements") final long totalElements) {
		super(content, new PageRequest(number, size), totalElements);
	}

}
