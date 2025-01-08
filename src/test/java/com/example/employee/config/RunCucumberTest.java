package com.example.employee.config;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import java.io.File;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "usage")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "html:target/cucumber-reports/Cucumber.html")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.employee.config")
public class RunCucumberTest {

    @Test
    void testSuite() {
        File bddResourcesDirectory = new File("src/test/resources/features");
        assertThat(bddResourcesDirectory.listFiles()).isNotEmpty();
    }
}
