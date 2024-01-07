package lk.ijse.Musclehut.dto;

import lk.ijse.Musclehut.dto.tm.CartTm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class PayScheduleDto {
    private String sheduleId;
    private String memId;
    private LocalDate date;
    private List<CartTm> tmList = new ArrayList<>();
}
