package pet_crypto_viewer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "btc_price")
public class BTC_price {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column(name = "time")
    private String time;

    @Column(name = "value")
    private Integer value;

    public Integer getId() {
        return id;
    }

//    public Date getTime() {
//        return time;
//    }
//
    public Integer getValue() {
        return value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public void setTime(Date time) {
//        this.time = time;
//    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public BTC_price(Integer value, String time) {
        this.value = value;
        this.time = time;

    }
}
