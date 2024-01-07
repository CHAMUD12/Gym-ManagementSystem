package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.ExerciseDto;
import lk.ijse.Musclehut.dto.tm.CartTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseModel {

    public boolean saveExercise(ExerciseDto exerciseDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO exercise VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, exerciseDto.getCode());
        pstm.setString(2, exerciseDto.getDescription());
        pstm.setDouble(3, exerciseDto.getPrice());
        pstm.setInt(4, exerciseDto.getCountOfMonth());

        return pstm.executeUpdate() > 0;
    }
    public boolean deleteExercise (String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM exercise WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        return pstm.executeUpdate() > 0;
    }

    public List<ExerciseDto> loadAllExercises() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM exercise";
        PreparedStatement pstm = connection.prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ExerciseDto> dtoList = new ArrayList<>();

        while (resultSet.next()) {
            var dto = new ExerciseDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );

            dtoList.add(dto);
        }

        return dtoList;
    }

    public boolean updateExercise(ExerciseDto exerciseDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE exercise SET description = ?, price = ?, count_of_month = ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, exerciseDto.getDescription());
        pstm.setDouble(2, exerciseDto.getPrice());
        pstm.setInt(3, exerciseDto.getCountOfMonth());
        pstm.setString(4, exerciseDto.getCode());

        return pstm.executeUpdate() > 0;
    }

    public boolean updateExercise(List<CartTm> tmList) throws SQLException {
        for (CartTm cartTm : tmList) {
            if(!updateCount(cartTm)) {
                return false;
            }
        }
        return true;
    }

    private boolean updateCount(CartTm cartTm) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE exercise SET count_of_month = count_of_month - ? WHERE code = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, cartTm.getCount());
        pstm.setString(2, cartTm.getCode());

        return pstm.executeUpdate() > 0; //true
    }

    public ExerciseDto searchExercise(String code) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "SELECT * FROM exercise WHERE code = ?";

        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, code);

        ResultSet resultSet = pstm.executeQuery();

        ExerciseDto dto = null;

        if(resultSet.next()) {
            dto = new ExerciseDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return dto;
    }

    public String totalExerciseCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS ExersiceCount FROM exercise";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();


        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}
