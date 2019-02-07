package zur.koeln.kickertool.infrastructure.persistence;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseSettings {

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dm = new DriverManagerDataSource("jdbc:derby:db", "admin", "kickern!");

        Properties properties = new Properties();
        //        properties.setProperty("create", "true");
        dm.setConnectionProperties(properties);
        dm.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");

        return dm;
    }


}
