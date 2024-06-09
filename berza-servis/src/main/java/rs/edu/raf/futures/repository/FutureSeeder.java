package rs.edu.raf.futures.repository;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.futures.model.FuturesContract;

@Component
@AllArgsConstructor
public class FutureSeeder implements CommandLineRunner{
    private FuturesContractRepository futuresContractRepository;

    @Override
    public void run(String... args) throws Exception {
        futuresContractRepository.deleteAll();
        futuresContractRepository.save(new FuturesContract("corn", 5000, "bushel", 1600.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("soybean", 5000, "bushel", 2700.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("soybean oil", 60000, "pound", 2100.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("soybean meal", 180000, "pound", 1750.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("Chicago wheat", 5000, "bushel", 2500.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("live cattle", 40000, "pound", 1600.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("wheat", 5000, "bushel", 2600.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("feeder cattle", 50000, "pound", 2850.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("lean hog", 40000, "pound", 1750.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("pork cutout", 40000, "pound", 1900.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("nonfat dry milk", 44000, "pound", 2150.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("class 3 milk", 200000, "pound", 1500.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("class 4 milk", 200000, "pound", 1500.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("cash-settled butter", 20000, "pound", 2100.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("cash-settled cheese", 20000, "pound", 2200.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("block cheese", 2000, "pound", 2100.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("oats", 5000, "bushel", 1800.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("rough rice", 180000, "pound", 1400.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("urea", 200000, "pound", 8000.0, "AGRICULTURE"));
        futuresContractRepository.save(new FuturesContract("lumber", 27500, "board feet", 1350.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("crude oil", 1000, "barrel", 5300.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("natural gas", 10000, "MMBtu", 3700.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("gasoline", 42000, "gallon", 6500.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("e-mini crude oil", 500, "barrel", 2750.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("NY harbor ULSD", 42000, "gallon", 7000.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("micro crude oil", 100, "barrel", 550.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("Henry Hub natural gas", 10000, "MMBtu", 3800.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("buckeye jet fuel", 42000, "gallon", 8000.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("gold", 100, "troy ounce", 8000.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("silver", 5000, "troy ounce", 9500.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("platinum", 50, "troy ounce", 3700.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("copper", 25000, "pound", 6100.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("aluminum", 50000, "pound", 3600.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("e-mini copper", 12500, "pound", 3050.0, "ENERGY"));
        futuresContractRepository.save(new FuturesContract("copper mini", 12500, "pound", 1500.0, "METALS"));
        futuresContractRepository.save(new FuturesContract("silver mini", 1000, "troy ounce", 2500.0, "METALS"));
        futuresContractRepository.save(new FuturesContract("platinum mini", 10, "troy ounce", 2500.0, "METALS"));
        futuresContractRepository.save(new FuturesContract("gold options", 100, "troy ounce", 5000.0, "METALS"));
        futuresContractRepository.save(new FuturesContract("silver options", 5000, "troy ounce", 6000.0, "METALS"));
        futuresContractRepository.save(new FuturesContract("palladium options", 100, "troy ounce", 7000.0, "METALS"));
        futuresContractRepository.save(new FuturesContract("cotton", 50000, "pound", 2000.0, "SOFTS"));
        futuresContractRepository.save(new FuturesContract("coffee", 37500, "pound", 1500.0, "SOFTS"));
        futuresContractRepository.save(new FuturesContract("sugar", 112000, "pound", 1200.0, "SOFTS"));
        futuresContractRepository.save(new FuturesContract("cocoa", 10, "metric ton", 1800.0, "SOFTS"));
        futuresContractRepository.save(new FuturesContract("orange juice", 15000, "pound", 2000.0, "SOFTS"));
        futuresContractRepository.save(new FuturesContract("lumber options", 1100, "board feet", 2000.0, "SOFTS"));
        futuresContractRepository.save(new FuturesContract("lean hog options", 40000, "pound", 2000.0, "MEATS"));
        futuresContractRepository.save(new FuturesContract("live cattle options", 40000, "pound", 2000.0, "MEATS"));
        futuresContractRepository.save(new FuturesContract("feeder cattle options", 50000, "pound", 2000.0, "MEATS"));
        futuresContractRepository.save(new FuturesContract("butter options", 20000, "pound", 3000.0, "MEATS"));
        futuresContractRepository.save(new FuturesContract("cheese options", 20000, "pound", 3000.0, "MEATS"));
        futuresContractRepository.save(new FuturesContract("pork belly options", 40000, "pound", 3500.0, "MEATS"));
    }
}
