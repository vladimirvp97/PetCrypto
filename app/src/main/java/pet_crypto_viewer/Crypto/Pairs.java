package pet_crypto_viewer.Crypto;



import javax.persistence.*;
import java.util.Objects;

@Entity
//@IdClass(CompositePK.class)
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
    private Integer value;


    @Column(name = "time")
    private String time;

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

    public Integer getValue() {
        return value;
    }


    public String getTime() {
        return time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Pairs(Integer value, String time) {
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
