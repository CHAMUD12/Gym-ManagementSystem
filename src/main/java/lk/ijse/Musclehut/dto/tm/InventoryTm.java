package lk.ijse.Musclehut.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class InventoryTm {
    private String id;
    private String name;
    private String category;
    private String count;
}
