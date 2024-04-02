package DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor


public class Game {
    private String title;
    private long steamAppID;
    List<Deal> dealsList = new ArrayList<>();


}