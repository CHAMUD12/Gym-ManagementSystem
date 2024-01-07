package lk.ijse.Musclehut.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class IncomeReport {
    private  String date;
    private int numOfShedule ;
    private int numberOfSoldExercise;
    private double finalCost;

    public IncomeReport(String date, int numberOfSoldItem) {
        this.date = date;
        this.numberOfSoldExercise = numberOfSoldItem;
    }
}
