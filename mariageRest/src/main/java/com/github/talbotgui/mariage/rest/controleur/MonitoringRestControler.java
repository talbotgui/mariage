package com.github.talbotgui.mariage.rest.controleur;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jamonapi.MonitorFactory;

@RestController
public class MonitoringRestControler {

	@RequestMapping(value = "/monitoring", method = GET)
	public String monitoring() {
		return MonitorFactory.getReport();
	}

}
