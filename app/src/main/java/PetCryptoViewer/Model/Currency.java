package PetCryptoViewer.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
// В этой таблице перчислены все доступные валюты
@Entity
@Data @NoArgsConstructor
@Table(name = "Currency")
public class Currency {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name")
    private String str;

}