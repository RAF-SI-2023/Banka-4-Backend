package rs.edu.raf.opcija.integration.opcijaservis;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/opcija/integration/opcijaservis")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.opcija.integration.opcijaservis")
public class OpcijaServiceTests {
}
