package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;

import java.sql.*;
import java.time.LocalDate;

public class ScheduleModel {
    public boolean saveSchedule(String scheduleId, String memId, LocalDate date) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO schedule VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, scheduleId);
        pstm.setString(2, memId);
        pstm.setDate(3, Date.valueOf(date));

        return pstm.executeUpdate() > 0;
    }

    public String generateNextScheduleId() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT s_id FROM schedule ORDER BY s_id DESC LIMIT 1";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        String currentScheduleId = null;

        if (resultSet.next()) {
            currentScheduleId = resultSet.getString(1);
            return splitScheduleId(currentScheduleId);
        }
        return splitScheduleId(null);
    }

    private String splitScheduleId(String currentScheduleId) {
        if (currentScheduleId != null) {
            String[] split = currentScheduleId.split("O");
            int id = Integer.parseInt(split[1]);
            id++;
            return "O00" + id;
        }
        return "O001";
    }
}
