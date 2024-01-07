package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.InventoryDto;
import lk.ijse.Musclehut.dto.MemberDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryModel {
    public List<InventoryDto> getAllInventory() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM inventory";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<InventoryDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String i_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String category = resultSet.getString(3);
            String count = resultSet.getString(4);

            var dto = new InventoryDto(i_id, name, category, count);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean saveInventory(final InventoryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO inventory VALUES(?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getCategory());
        pstm.setString(4, dto.getCount());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public InventoryDto searchInventory(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM inventory WHERE i_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        InventoryDto dto = null;

        if(resultSet.next()) {
            String i_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String category = resultSet.getString(3);
            String count = resultSet.getString(4);

            dto = new InventoryDto(i_id, name, category, count);
        }

        return dto;
    }

    public boolean updateInventory(final InventoryDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE inventory SET name = ?, category = ?, count = ? WHERE i_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getCategory());
        pstm.setString(3, dto.getCount());
        pstm.setString(4, dto.getId());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteInventory(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM inventory WHERE i_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

}
