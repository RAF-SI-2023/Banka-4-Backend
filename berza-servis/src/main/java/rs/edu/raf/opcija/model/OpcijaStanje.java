package rs.edu.raf.opcija.model;

public enum OpcijaStanje {
    //call je kupovina akcija po ceni iz opcije nezavisno od trenutne cene akcije
    //put je prodaja akcija po ceni iz opcije nezavisno od trenutne cene akcije

    IN_THE_MONEY,//kada izvrsenje opcije(put ili call) donosi profit za vlasnika opcije
    //za call je kada je trenutna cena akcije veca od cene akcije iz opcije pa je kupovina isplatljiva
    //za put je kada je trenutna cena akcije manja od cene akcije iz opcije pa je prodaja isplatljiva

    OUT_OF_MONEY,//kada izvrsenje opcije(put ili call) donosi gubitak vlasniku
    AT_THE_MONEY,//cena izvrsenja opcije odnosno akcija u okviru opcije==cena osnovne akcije odnsono nema profita

    //UNEXERCISED,//neiskoriscena
    //EXERCISED,//iskoriscena

    EXPIRED,//istekao datum
    SOLD,//prodata opcija
}
