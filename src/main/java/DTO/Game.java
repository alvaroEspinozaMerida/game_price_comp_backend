package DTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document

public class Game {
    @Indexed(unique = true)
    private String title;
    @Id
    private long steamAppID;
    @JsonProperty("thumb")
    private String thumb;
    List<Deal> deals = new ArrayList<>();
    @JsonProperty("screenshots")
    List<String> screenshots = new ArrayList<>();





    public void addNewDeal(Deal newDeal){
        deals.add(newDeal);
    }



}