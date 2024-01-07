package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ScheduleDetailModel {
    public boolean saveScheduleDetail(String scheduleId, List<CartTm> tmList) throws SQLException {
        for (CartTm cartTm : tmList) {
            if(!saveScheduleDetail(scheduleId, cartTm)) {
                return false;
            }
        }
        return true;
    }

    private boolean saveScheduleDetail(String scheduleId, CartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO schedule_detail VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, scheduleId);
        pstm.setString(2, cartTm.getCode());
        pstm.setInt(3, cartTm.getCount());
        pstm.setDouble(4, cartTm.getPrice());

        return pstm.executeUpdate() > 0;
    }
}
