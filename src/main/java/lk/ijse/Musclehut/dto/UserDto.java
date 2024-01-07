package lk.ijse.Musclehut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserDto {
    private String email;
    private String name;
    private String password;
}
