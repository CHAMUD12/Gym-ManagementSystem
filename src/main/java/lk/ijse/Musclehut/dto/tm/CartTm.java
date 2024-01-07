package lk.ijse.Musclehut.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CartTm {
    private String code;
    private String description;
    private int count;
    private double Price;
    private double tot;
    private Button btn;
}
