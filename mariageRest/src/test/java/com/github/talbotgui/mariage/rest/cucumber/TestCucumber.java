package com.github.talbotgui.mariage.rest.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, glue = { "com.github.talbotgui.mariage.rest.cucumber", "cucumber.api.spring" })
public class TestCucumber {

}
