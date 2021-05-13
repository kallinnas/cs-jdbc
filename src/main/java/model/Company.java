package model;

import lombok.*;

import java.util.Collection;
import java.util.HashSet;

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

    private Collection<Coupon> coupons;

}
