package model;

import lombok.*;
import java.util.Collection;
import java.util.HashSet;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public
class Customer extends Client {

    private long id;
    private String firstName;
    private String lastName;
    private final Collection<Coupon> coupons = new HashSet<>();
}
