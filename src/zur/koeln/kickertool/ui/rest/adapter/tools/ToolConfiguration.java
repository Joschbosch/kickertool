package zur.koeln.kickertool.ui.rest.adapter.tools;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfiguration {

    @Bean
    public CustomModelMapper customModelMapper() {
        return new CustomModelMapper();
    }

	
}
