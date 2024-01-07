package lk.ijse.Musclehut.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class EmployeeTm {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String salary;
}
