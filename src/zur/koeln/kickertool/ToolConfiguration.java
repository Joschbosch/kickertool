package zur.koeln.kickertool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;

@Configuration
public class ToolConfiguration {

    @Bean
    public CustomModelMapper customModelMapper() {
        return new CustomModelMapper();
    }

	
}
