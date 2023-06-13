package PetCryptoViewer.Model;



import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
//Здесь будут сохранены валютные пары(указание валюты является внешним ключом к таблице Currency), время получения и значение.
@Entity
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

    public Currency getFirst_currency() {
        return first_currency;
    }

    public void setFirst_currency(Currency first_currency) {
        this.first_currency = first_currency;
    }

    public Currency getSecond_currency() {
        return second_currency;
    }

    public void setSecond_currency(Currency second_currency) {
        this.second_currency = second_currency;
    }

    public Double getValue() {
        return value;
    }


    public Instant getTime() {

        return time;
    }

    public String getConciseTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(time);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Pairs(Double value, Instant time) {
        this.value = value;
        this.time = time;
    }

    public Pairs() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pairs pairs = (Pairs) o;
        return id.equals(pairs.id) && first_currency.equals(pairs.first_currency) && second_currency.equals(pairs.second_currency) && value.equals(pairs.value) && time.equals(pairs.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, first_currency, second_currency, value, time);
    }
}
