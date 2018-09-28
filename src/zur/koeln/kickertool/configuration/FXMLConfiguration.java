package zur.koeln.kickertool.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;

@Configuration
public class FXMLConfiguration {
	
	@Bean
	@Primary
	public FXMLGUIservice createFXMLGUIService() {
		return new FXMLGUIservice();
	}
}
