package at.dertanzbogen.api.domain.main.Course;

import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CourseProgramm {
    private String programm;

    public CourseProgramm(String programm) {
        this.programm = Ensure.ensureNonNullValid(programm, "Programm");
    }

    public String getProgramm() {
        return programm;
    }

    public void setProgramm(String programm) {
        this.programm = Ensure.ensureNonNullValid(programm, "Programm");
    }
}
