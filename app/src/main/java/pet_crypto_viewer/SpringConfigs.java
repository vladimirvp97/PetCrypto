package pet_crypto_viewer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Configuration
public class SpringConfigs {
    @Bean
    public DateTimeFormatter dtf(){
        FormatStyle fs = FormatStyle.MEDIUM;
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(fs).withZone(ZoneOffset.UTC);
        return dtf;
    }
}
