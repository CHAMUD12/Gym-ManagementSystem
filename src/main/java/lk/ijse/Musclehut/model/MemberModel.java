package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.MemberDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberModel {
    public boolean saveMember(final MemberDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO member VALUES(?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getAddress());
        pstm.setString(4, dto.getPhone());
        pstm.setString(5, dto.getEmail());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public boolean updateMember(final MemberDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE member SET name = ?, address = ?, phone = ?, email = ? WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getAddress());
        pstm.setString(3, dto.getPhone());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getId());

        return pstm.executeUpdate() > 0;
    }

    public MemberDto searchMember(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM member WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        MemberDto dto = null;

        if(resultSet.next()) {
            String m_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String phone = resultSet.getString(4);
            String email = resultSet.getString(5);

            dto = new MemberDto(m_id, name, address, phone, email);
        }

        return dto;
    }

    public boolean deleteMember(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM member WHERE m_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public List<MemberDto> getAllMembers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM member";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<MemberDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String m_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String phone = resultSet.getString(4);
            String email = resultSet.getString(5);

            var dto = new MemberDto(m_id, name, address, phone, email);
            dtoList.add(dto);
        }
        return dtoList;
    }
    public List<MemberDto> loadAllMembers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM member";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();

        List<MemberDto> memList = new ArrayList<>();

        while (resultSet.next()) {
            memList.add(new MemberDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return memList;
    }

    public String totalMemberCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS MemberCount FROM member";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();


        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}

