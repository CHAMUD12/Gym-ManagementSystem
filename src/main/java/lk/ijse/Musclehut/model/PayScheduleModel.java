package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.PayScheduleDto;

import java.sql.Connection;
import java.sql.SQLException;

public class PayScheduleModel {
    private final ScheduleModel scheduleModel = new ScheduleModel();
    private final ExerciseModel exerciseModel = new ExerciseModel();
    private final ScheduleDetailModel scheduleDetailModel = new ScheduleDetailModel();

    public boolean paySchedule(PayScheduleDto pDto) throws SQLException {
        boolean result = false;
        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isScheduleSaved = scheduleModel.saveSchedule(pDto.getSheduleId(), pDto.getMemId(), pDto.getDate());
            if (isScheduleSaved) {
                boolean isUpdated = exerciseModel.updateExercise(pDto.getTmList());
                if(isUpdated) {
                    boolean isScheduleDetailSaved = scheduleDetailModel.saveScheduleDetail(pDto.getSheduleId(), pDto.getTmList());
                    if(isScheduleDetailSaved) {
                        connection.commit();
                        result = true;
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
        return result;
    }
}
