package at.dertanzbogen.api.domain.main.User;

import at.dertanzbogen.api.domain.main.Media;
import at.dertanzbogen.api.domain.validation.Ensure;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static at.dertanzbogen.api.domain.validation.Ensure.ensureValidName;


@NoArgsConstructor
@ToString
public class Personal {

    private String firstName;
    private String lastName;
    private Media profilPicture;


//    public Personal(String firstName, String lastName, String mimeType, int size, int with, int height, String fileName) {
//        this.firstName = ensureValidName(firstName);
//        this.lastName = ensureValidName(lastName);
//        this.profilPicture = new Media(mimeType, size, with, height, fileName);
//    }

    public Personal(String firstName, String lastName) {
        this.firstName = ensureValidName(firstName);
        this.lastName = ensureValidName(lastName);
    }

//    public Personal(String firstName, String lastName, Media profilPicture) {
//        this.firstName = ensureValidName(firstName);
//        this.lastName = ensureValidName(lastName);
//        this.profilPicture = profilPicture;
//    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = ensureValidName(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = ensureValidName(lastName);
    }

    public Media getProfilPicture() {
        return profilPicture;
    }

//    public void setProfilPicture(String fileName, String mimeType, int size, int with, int height) {
//        this.profilPicture = new Media(fileName,mimeType, size, with, height);
//    }

    public void setProfilPicture(Media profilPicture) {
        this.profilPicture = profilPicture;
    }
}
