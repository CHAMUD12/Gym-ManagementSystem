package lk.ijse.Musclehut.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ExerciseTm {
    private String code;
    private String description;
    private double Price;
    private int countOfMonth;
    private Button btn;
}
