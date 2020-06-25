package pe.com.pacifico.receptorevento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cliente {

    private Date birthDate;
    private String dni;
    private String email1;
    private String email2;
    private String firstSurname;
    private String id;
    private String lastSurname;
    private String names;
    private String phoneNumber1;
    private String phoneNumber2;
    private String phoneNumber3;
    private String phoneNumber4;

    public Cliente() {

    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getDni() {
        return dni;
    }

    public String getEmail1() {
        return email1;
    }

    public String getEmail2() {
        return email2;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getId() {
        return id;
    }

    public String getLastSurname() {
        return lastSurname;
    }

    public String getNames() {
        return names;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public String getPhoneNumber3() {
        return phoneNumber3;
    }

    public String getPhoneNumber4() {
        return phoneNumber4;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastSurname(String lastSurname) {
        this.lastSurname = lastSurname;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public void setPhoneNumber3(String phoneNumber3) {
        this.phoneNumber3 = phoneNumber3;
    }

    public void setPhoneNumber4(String phoneNumber4) {
        this.phoneNumber4 = phoneNumber4;
    }

}