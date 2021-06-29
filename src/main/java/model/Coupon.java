package model;

import lombok.*;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private long id;
    @NonNull
    private long companyId;
    private String title;
    private LocalDate startDate;
    private double price;
    private String description;
    private String imageURL;
}
