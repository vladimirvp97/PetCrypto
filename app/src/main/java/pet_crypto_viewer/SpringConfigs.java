package pet_crypto_viewer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class SpringConfigs {
    @Bean
    public SimpleDateFormat sdf(){
        return new SimpleDateFormat("HH:mm:ss");
    }
}
