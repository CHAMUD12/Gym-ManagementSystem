package lk.ijse.Musclehut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class EmployeeDto {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String salary;
}
