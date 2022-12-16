package com.udemy.neutrinosys.peopledbweb.bizmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 *
 */
@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
public class Person implements  Entity{
    @Id
    @GeneratedValue
    private  Long id;

    @NotEmpty(message="Last name can not be empty")
    private String lastName;

    @Past(message="Date of birth  must be in the past")
    @NotNull(message = "Date of birth must be specified")
    private ZonedDateTime dob;


    @NotEmpty(message="First name can not be empty")
    private String firstName;
    @DecimalMin(value="1000",message="Salary must be at least 1000")
    @NotNull(message = "Salary must be specified")
    private BigDecimal Salary;
    public Person() {

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                dob.withZoneSameInstant(ZoneId.of("+0")).equals(person.dob.withZoneSameInstant(ZoneId.of("+0")));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dob);
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }



//    @Email(message="Email must be valid")
//    @NotEmpty(message = "Email must not be empty")
//    private String email;


//default constructor
public Person(long id, String firstName, String lastName, ZonedDateTime dob,BigDecimal Salary) {
    this.firstName=firstName;
    this.lastName=lastName;
    this.dob=dob;
    this.Salary=Salary;


}

    /**
     * Getters and setters for Person Concept
     *
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ZonedDateTime getDob() {
        return dob;
    }

    public void setDob(ZonedDateTime dob) {
        this.dob = dob;
    }
    public @DecimalMin(value = "1000", message = "Salary must be at least 1000") @NotNull(message = "Salary must be specified") BigDecimal getSalary() {
        return Salary;
    }

    public void setSalary(BigDecimal salary) {
        Salary = salary;
    }

    @Override
    public String name() {
        return null;
    }

    public String convertDobToTimeStamp(ZonedDateTime dob) {

        return null;
    }


//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }





}
