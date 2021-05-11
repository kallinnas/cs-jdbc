package model;

import lombok.*;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Company extends Client {

    private long id;
    private String name;
    private String imageURL;

    public Company() {
        this.coupons = new HashSet<>();
    }

    private Set<Coupon> coupons;

}
