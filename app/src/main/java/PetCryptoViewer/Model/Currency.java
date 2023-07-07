package PetCryptoViewer.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// В этой таблице перчислены все доступные валюты
@Entity
@Data
@NoArgsConstructor
@Table(name = "Currency")
public class Currency {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String str;

    public Currency(String str) {
        this.str = str;
    }

    public Currency(Integer id, String str) {
        this.id = id;
        this.str = str;
    }
}