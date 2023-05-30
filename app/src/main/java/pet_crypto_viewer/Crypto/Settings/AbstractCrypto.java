package pet_crypto_viewer.Crypto.Settings;

public abstract class AbstractCrypto {
    private Integer id;
    private String time;
    private Integer value;

    public Integer getId() {
        return id;
    }


    public Integer getValue() {
        return value;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }


    public void setValue(Integer value) {
        this.value = value;
    }

    public AbstractCrypto(Integer value, String time) {
        this.value = value;
        this.time = time;
    }

    public AbstractCrypto() {
    }
}
