package zur.koeln.kickertool.ui.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfiguration {

    @Bean
    public CustomModelMapper customModelMapper() {
        return new CustomModelMapper();
    }

	
}
