package rs.edu.raf.currency.servis;

import rs.edu.raf.currency.dto.InflationDTO;

import java.util.List;

/**
 * This interface defines methods for retrieving inflation data.
 */
public interface InflationService {

    /**
     * Retrieves inflation data for a specific country.
     *
     * @param country The name of the country for which to retrieve inflation data.
     * @return A list of InflationDTO objects representing inflation data for the specified country.
     */
    List<InflationDTO> getInflationByCountry(String country);

    /**
     * Retrieves inflation data for a specific country and year.
     *
     * @param country The name of the country for which to retrieve inflation data.
     * @param year    The year for which to retrieve inflation data.
     * @return A list of InflationDTO objects representing inflation data for the specified country and year.
     */
    List<InflationDTO> getInflationByCountryAndYear(String country, String year);
}
