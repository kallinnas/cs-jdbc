package model;

import lombok.*;
import java.time.LocalDate;


@Data
class Coupon {

    private long id;
    private long companyId;
    private String title;
    private LocalDate date;
    private double price;
    private String description;
    private String imageURL;

}
