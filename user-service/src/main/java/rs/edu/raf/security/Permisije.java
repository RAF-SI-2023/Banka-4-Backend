package rs.edu.raf.security;

public class Permisije {
    public static final long listanje_korisnika = 0b1;//binarni broj
    public static final long dodavanje_korisnika = 0b10;
    public static final long editovanje_korisnika = 0b100;
    public static final long deaktiviranje_korisnika = 0b1000;
    public static final long kreiranje_racuna = 0b10000;
    public static final long editovanje_racuna = 0b100000;
    public static final long brisanje_racuna = 0b1000000;
    public static final long listanje_radnika = 0b10000000;
    public static final long dodavanje_radnika = 0b100000000;
    public static final long editovanje_radnika = 0b100000000;
    public static final long deaktiviranje_radnika = 0b1000000000;
}
