package zur.koeln.kickertool.infrastructure.persistence;

import java.util.Properties;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseSettings {

    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dm = new DriverManagerDataSource("jdbc:derby:db", "admin", "kickern!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        Properties properties = new Properties();
        properties.setProperty("create", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        dm.setConnectionProperties(properties);
        dm.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver"); //$NON-NLS-1$

        return dm;
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
