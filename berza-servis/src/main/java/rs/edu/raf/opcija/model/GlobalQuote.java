package rs.edu.raf.opcija.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticker;//symbol

    private Long sharesOutstanding;//ukupan broj akcija kompanije

    private double open;

    private double high;

    private double low;

    private double price;//trenutna cena akcije kompanije

    private long volume;

    private Date latestTradingDay;

    private double previousClose;

    private double change;

    private String changePercent;
}
