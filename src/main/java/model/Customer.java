package model;

import lombok.*;

import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
class Customer extends Client {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Set<Coupon> coupons;

    Customer() {
        coupons = new HashSet<>();
    }

}
