package model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public
class Company extends Client {

    private long id;
    private String name;
    private String imageURL;

    public Company() {
        this.coupons = new HashSet<>();
    }

    private Set<Coupon> coupons;

}
