package PetCryptoViewer.Model;

import javax.persistence.*;
import java.util.Objects;
// В этой таблице перчислены все доступные валюты
@Entity
@Table(name = "Currency")
public class Currency {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String str;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Currency() {
    }

    public Currency(String name){

        this.str = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id) && Objects.equals(str, currency.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, str);
    }
}
