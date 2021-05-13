package model;

import lombok.*;
import java.util.Collection;
import java.util.HashSet;

@Data
@EqualsAndHashCode(callSuper = true)
class Customer extends Client {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private Collection<Coupon> coupons;

    Customer() {
        coupons = new HashSet<>();
    }

}
