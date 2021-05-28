package model;

import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public
class Company extends Client {

    private long id;
    private String name;
    private String imageURL;
    private final Collection<Coupon> coupons = new HashSet<>();
}
