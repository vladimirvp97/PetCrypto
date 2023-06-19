package PetCryptoViewer.Model;



import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
//Здесь будут сохранены валютные пары(указание валюты является внешним ключом к таблице Currency), время получения и значение.
@Entity
@Data @NoArgsConstructor
@Table(name = "Pairs")
public class Pairs {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "first_currency")
    private Currency first_currency;

    @ManyToOne
    @JoinColumn(name = "second_currency")
    private Currency second_currency;

    @Column(name = "value")
    private Double value;

    @Column(name = "time")
    private Instant time;

    public Pairs(Double value, Instant time) {
        this.value = value;
        this.time = time;
    }

    public String getConciseTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(time);
    }

}
