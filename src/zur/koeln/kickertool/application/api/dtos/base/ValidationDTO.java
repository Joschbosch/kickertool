package zur.koeln.kickertool.application.api.dtos.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationDTO {

    private final List<String> errorMsgs = new ArrayList<>();
    
    @Override
    public String toString() {
    	// TODO: @Sascha, meinste, das ist ok hier? Wollte gerne aus der Fehlerliste ein String machen. Hilft mir bei meiner Exception.
    	return StringUtils.collectionToDelimitedString(getErrorMsgs(), "\n"); //$NON-NLS-1$
    }
    
    public void addErrorMsg(String errorMsg) {
    	getErrorMsgs().add(errorMsg);
    }
}
