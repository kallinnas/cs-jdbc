package model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "customer")
public class Customer extends Client {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Set<Coupon> coupons;

    public Customer() {
        coupons = new HashSet<>();
    }

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
