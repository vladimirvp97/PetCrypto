package pet_crypto_viewer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Configuration
public class SpringConfigs {

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Bean
    public DateTimeFormatter dtf(){
        FormatStyle fs = FormatStyle.MEDIUM;
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(fs).withZone(ZoneOffset.UTC);
        return dtf;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
