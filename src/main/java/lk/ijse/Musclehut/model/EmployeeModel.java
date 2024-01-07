package lk.ijse.Musclehut.model;

import lk.ijse.Musclehut.db.DbConnection;
import lk.ijse.Musclehut.dto.EmployeeDto;
import lk.ijse.Musclehut.dto.MemberDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public List<EmployeeDto> getAllEmployee() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee";
        PreparedStatement pstm = connection.prepareStatement(sql);

        List<EmployeeDto> dtoList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String e_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String phone = resultSet.getString(4);
            String email = resultSet.getString(5);
            String salary = resultSet.getString(6);

            var dto = new EmployeeDto(e_id, name, address, phone, email, salary);
            dtoList.add(dto);
        }
        return dtoList;
    }

    public boolean saveEmployee(final EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO employee VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getId());
        pstm.setString(2, dto.getName());
        pstm.setString(3, dto.getAddress());
        pstm.setString(4, dto.getPhone());
        pstm.setString(5, dto.getEmail());
        pstm.setString(6, dto.getSalary());

        boolean isSaved = pstm.executeUpdate() > 0;

        return isSaved;
    }

    public EmployeeDto searchEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet = pstm.executeQuery();

        EmployeeDto dto = null;

        if (resultSet.next()) {
            String e_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String phone = resultSet.getString(4);
            String email = resultSet.getString(5);
            String salary = resultSet.getString(6);

            dto = new EmployeeDto(e_id, name, address, phone, email, salary);
        }

        return dto;
    }

    public boolean updateEmployee(final EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE employee SET name = ?, address = ?, phone = ?, email = ?, salary = ? WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, dto.getName());
        pstm.setString(2, dto.getAddress());
        pstm.setString(3, dto.getPhone());
        pstm.setString(4, dto.getEmail());
        pstm.setString(5, dto.getSalary());
        pstm.setString(6, dto.getId());

        return pstm.executeUpdate() > 0;
    }
    public boolean deleteEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM employee WHERE e_id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public String totalEmployeeCount() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT COUNT(*) AS EmployeeCount FROM employee";
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();


        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}

