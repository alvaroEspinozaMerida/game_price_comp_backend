package DTO;
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
    List<Deal> deals = new ArrayList<>();


}