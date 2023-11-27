package at.dertanzbogen.api.domain.main.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMassage {

    private HttpStatus status;
    private String message;
}
