package domain.pack;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Id
    @Column(name = "at", nullable = false, unique = true, length = 8)
    @NotBlank(message = "Ο ΑΤ είναι υποχρεωτικός.")
    @Size(min = 8, max = 8, message = "Ο ΑΤ πρέπει να αποτελείται από 8 χαρακτήρες.")
    private String at;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "Το όνομα είναι υποχρεωτικό.")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "Το επίθετο είναι υποχρεωτικό.")
    private String lastName;

    @Column(name = "gender", nullable = false)
    @NotBlank(message = "Το φύλο είναι υποχρεωτικό.")
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    @NotBlank(message = "Η ημερομηνία γέννησης είναι υποχρεωτική.")
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}", message = "Η ημερομηνία γέννησης πρέπει να είναι μορφής ΗΗ-ΜΜ-ΕΕΕΕ.")
    private String dateOfBirth;

    @Column(name = "afm")
    @Pattern(regexp = "^$|^\\d{9}$", message = "Ο ΑΦΜ πρέπει να έχει 9 ψηφία.")
    private String afm;

    @Column(name = "address")
    private String address;


    public Citizen() {}

    public Citizen(String at, String firstName, String lastName, String gender, String dateOfBirth, String afm, String address) {
        this.at = at;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.afm = afm;
        this.address = address;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
