package rs.edu.raf.opcija.servis;

import java.time.Instant;

public class IzvedeneVrednostiUtil {

    //opcija se kupuje,prodaje ili iskoriscava
    //opcija ima predefinisanu cenu prodaje ili kupovine akcija i predefinisan datum isteka
    //opcija se moze iskoristiti bilo kada pre datuma isteka


    //glavna razlika opcija i kupovine akcije u realnom vremenu jeste sto opcija ima fiksnu cenu akcije
    //odnosno iako trenutna cena akcije menja vrednost, cena akcije u opciji ostaje nepromenjena

    public double calculateBlackScholesValue(double trenutnaCenaOsnovneAkcijeKompanije,
                                             double strikePrice,
                                             double impliedVolatility,
                                             long expiration) {
        double S = trenutnaCenaOsnovneAkcijeKompanije;
        double K = strikePrice;
        double r = 0.05; //pretpostavljena vrednost bezrizicne kamatne stope
        double sigma = impliedVolatility;
        double timeToExpiration = calculateTimeToExpirationInYears(expiration);

        if(timeToExpiration == 0)//opcija je istekla
            return 0;

        double d1 = (Math.log(S / K) + (r + 0.5 * Math.pow(sigma, 2)) * timeToExpiration) / (sigma * Math.sqrt(timeToExpiration));

        double d2 = d1 - sigma * Math.sqrt(timeToExpiration);
        double N_d1 = cumulativeProbability(d1);
        double N_d2 = cumulativeProbability(d2);

        //System.out.println("timeToExp "+timeToExpiration);
        //System.out.println("NaN "+Math.sqrt(timeToExpiration));

        double callOptionValue = S * N_d1 - K * Math.exp(-r * timeToExpiration) * N_d2;

        return callOptionValue;
    }

    private double cumulativeProbability(double x) {
        double t = 1.0 / (1.0 + 0.2316419 * Math.abs(x));
        double d = 0.39894228 * Math.exp(-x * x / 2.0);
        double p = 1 - d * t * (0.319381530 + t * (-0.356563782 + t * (1.781477937 + t * (-1.821255978 + t * 1.330274429))));
        if (x > 0) {
            return p;
        } else {
            return 1 - p;
        }
    }
    /*//Funkcija za izracunavanje kumulativne distribucije normalne distribucije
    public static double cumulativeProbability(double x) {
        return 0.5 * (1 + Erf.erf(x / Math.sqrt(2)));
    }
    */

    private double calculateTimeToExpirationInYears(double expiration) {
        long nowSeconds = Instant.now().getEpochSecond();
        long expirationSeconds = (long) expiration;
        double timeInSeconds = expirationSeconds - nowSeconds;
        if (timeInSeconds <= 0) {
            return 0; //opcija je vec istekla, pa vreme do isteka treba biti 0
        }
        return timeInSeconds / (365 * 24 * 60 * 60); //pretvara u godine
    }

    public double calculateThetaCall(double S, double K, double r, double sigma, double T) {
        double d1 = (Math.log(S / K) + (r + 0.5 * Math.pow(sigma, 2)) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        double N_prime_d1 = probabilityDensityFunction(d1);

        return -((S * N_prime_d1 * sigma) / (2 * Math.sqrt(T))) - (r * K * Math.exp(-r * T) * cumulativeProbability(d2));
    }
    public double calculateThetaPut(double S, double K, double r, double sigma, double T) {
        double d1 = (Math.log(S / K) + (r + 0.5 * Math.pow(sigma, 2)) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        double N_prime_d1 = probabilityDensityFunction(d1);

        return -((S * N_prime_d1 * sigma) / (2 * Math.sqrt(T))) + (r * K * Math.exp(-r * T) * cumulativeProbability(-d2));
    }
    //funkcija za izracunavanje gustine verovatnoce normalne distribucije
    public double probabilityDensityFunction(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    public double calculateMaintenanceMargin(double marketCap, double marginPercentage) {
        return marketCap * (marginPercentage / 100.0);
    }


    //i za call i za put opcije
    /*public double proceniVrednostOpcije(
                                        //stvarna trenutna trzisna cena akcije koja se menja u svakom trenutku
                                        double trenutnaCenaOsnovneAkcije,

                                        //cena izvrsenja opcije je kupovina(call) ili prodaja(put) akcija u okviru opcije bilo kada do isteka opcije
                                        double cenaIzvrsenjaOpcije,
                                        double stopaBezRizika,
                                        double brojGodinaDoIstekaOpcije,
                                        double volatilnostCeneOsnovneAkcije){

        double d1 = (Math.log(trenutnaCenaOsnovneAkcije / cenaIzvrsenjaOpcije) +
                    (stopaBezRizika + (volatilnostCeneOsnovneAkcije * volatilnostCeneOsnovneAkcije) / 2) * brojGodinaDoIstekaOpcije)
                     / (volatilnostCeneOsnovneAkcije * Math.sqrt(brojGodinaDoIstekaOpcije));

        double d2 = d1 - volatilnostCeneOsnovneAkcije * Math.sqrt(brojGodinaDoIstekaOpcije);

        NormalDistribution normalDistribution = new NormalDistribution();
        double Nd1 = normalDistribution.cumulativeProbability(d1);
        double Nd2 = normalDistribution.cumulativeProbability(d2);


        double vrednostOpcije = trenutnaCenaOsnovneAkcije * Nd1 - cenaIzvrsenjaOpcije *
                                Math.exp(-stopaBezRizika * brojGodinaDoIstekaOpcije) * Nd2;

        return vrednostOpcije;
    }*/


}
