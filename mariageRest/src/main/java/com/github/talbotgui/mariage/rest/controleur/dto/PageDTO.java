package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageDTO<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<T> content;
	private boolean first;
	private boolean last;
	private int number;
	private long totalElements;
	private int totalPages;

	public PageDTO() {
		super();
		this.content = new ArrayList<T>();
	}

	public PageDTO(final Page<T> page) {
		this.content = new ArrayList<T>();
		this.content.addAll(page.getContent());
		this.first = page.isFirst();
		this.last = page.isLast();
		this.number = page.getNumber();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

	public List<T> getContent() {
		return content;
	}

	public int getNumber() {
		return number;
	}

	public int getNumberOfElements() {
		return this.content.size();
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean isFirst() {
		return first;
	}

	public boolean isLast() {
		return last;
	}

}
