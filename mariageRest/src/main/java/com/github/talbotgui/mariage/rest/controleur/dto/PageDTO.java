package com.github.talbotgui.mariage.rest.controleur.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageDTO<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;

	private final List<T> content;
	private boolean first;
	private boolean last;
	private int number;
	private long totalElements;
	private int totalPages;

	public PageDTO() {
		super();
		this.content = new ArrayList<>();
	}

	public PageDTO(final Page<T> page) {
		this.content = new ArrayList<>();
		this.content.addAll(page.getContent());
		this.first = page.isFirst();
		this.last = page.isLast();
		this.number = page.getNumber();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

	public List<T> getContent() {
		return this.content;
	}

	public int getNumber() {
		return this.number;
	}

	public int getNumberOfElements() {
		return this.content.size();
	}

	public long getTotalElements() {
		return this.totalElements;
	}

	public int getTotalPages() {
		return this.totalPages;
	}

	public boolean isFirst() {
		return this.first;
	}

	public boolean isLast() {
		return this.last;
	}

}
