package DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.time.LocalDate;

//Update to take into account todays date
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deal {
    private String storeID;
    LocalDate date;
    private String dealID;
    double price;
    double retailPrice;
    double savings;
    public void setDate(){
        date = LocalDate.now();
    }
}