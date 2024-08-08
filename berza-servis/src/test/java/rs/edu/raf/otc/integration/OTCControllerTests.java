package rs.edu.raf.otc.integration;

// Objasnjenje anotacija:
//  - @Suite: JUnit anotacija za grupisanje testova, u ovom slucaju Cucumber testova
//  - @IncludeEngines: ukljucuje Cucumber engine
//  - @SelectClasspathResource - koristi features definicije iz resources/features/integration/userservice direktorijuma
//  - @ConfigurationParameter - koristimo ovu anotaciju da naznacimo u kom paketu se nalazi Glue kod (obicno je to ovaj
//    isti paket)

//@Suite
//@IncludeEngines("cucumber")
//@SelectClasspathResource("features/otc/integration")
//@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.otc.integration")
public class OTCControllerTests {
}
