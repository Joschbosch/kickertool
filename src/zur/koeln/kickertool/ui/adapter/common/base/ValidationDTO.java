package zur.koeln.kickertool.ui.adapter.common.base;

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
    	return StringUtils.collectionToDelimitedString(getErrorMsgs(), "\n"); //$NON-NLS-1$
    }

    public void addErrorMsg(String errorMsg) {
    	getErrorMsgs().add(errorMsg);
    }
}
