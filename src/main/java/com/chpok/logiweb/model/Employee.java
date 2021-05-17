package com.chpok.logiweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee extends AbstractModel{
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "current_position")
    private String currentPosition;

    public static Builder builder() {
        return new Builder();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public Employee() {}

    private Employee(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.currentPosition = builder.currentPosition;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String currentPosition;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withCurrentPosition(String currentPosition) {
            this.currentPosition = currentPosition;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return firstName.equals(employee.firstName) && lastName.equals(employee.lastName) && currentPosition.equals(employee.currentPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, currentPosition);
    }
}
