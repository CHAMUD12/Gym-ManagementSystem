package lk.ijse.Musclehut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ExerciseDto {
    private String code;
    private String description;
    private double Price;
    private int countOfMonth;
}
