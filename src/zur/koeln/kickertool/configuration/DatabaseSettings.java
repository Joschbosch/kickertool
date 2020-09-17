package zur.koeln.kickertool.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseSettings {

	private static final String DB_NAME = "db"; //$NON-NLS-1$

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dm = new DriverManagerDataSource("jdbc:derby:" + DB_NAME, "admin", "kickern!"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Properties properties = new Properties();

		final Path dbDir = Paths.get(DB_NAME);

		String createProperty  = Boolean.toString((dbDir == null || !(dbDir.toFile().exists() && dbDir.toFile().isDirectory())));
		properties.setProperty("create", createProperty); //$NON-NLS-1$
		
		dm.setConnectionProperties(properties);
		dm.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver"); //$NON-NLS-1$

		return dm;
	}

}
