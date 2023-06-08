package com.softserve.teachua.dao;

import com.softserve.commons.exception.BadRequestException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BackupDaoImpl implements BackupDao {
    private final DataSource dataSource;

    @Autowired
    public BackupDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String getTable(String tableName) {
        String sql = "SELECT * FROM ?";

        StringBuilder query = new StringBuilder("insert into ");
        query.append(tableName);
        query.append("(");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery(sql);

            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();

            formTableHeader(query, metadata, columnCount);
            query.append(") values\n");

            boolean bln = false;
            formTableData(query, resultSet, metadata, columnCount, bln);
            query.append(";\n");

            log.info(query.toString());
        } catch (SQLException e) {
            throw new BadRequestException(e.getMessage());
        }

        return query.toString();
    }

    private void formTableHeader(StringBuilder query, ResultSetMetaData metadata, int columnCount)
            throws SQLException {
        for (int i = 0; i < columnCount; i++) {
            String columnName = metadata.getColumnName(i + 1);
            query.append(columnName);
            if (i == (columnCount - 1)) {
                query.append(" ");
            } else {
                query.append(", ");
            }
        }
    }

    private void formTableData(StringBuilder query, ResultSet resultSet, ResultSetMetaData metadata, int columnCount,
                                  boolean bln) throws SQLException {
        while (resultSet.next()) {
            if (bln) {
                query.append(",\n");
            }
            bln = true;
            query.append("(");
            for (int i = 0; i < columnCount; i++) {
                String columnName = metadata.getColumnName(i + 1);
                Object columnValue = resultSet.getObject(columnName);
                if (null == columnValue) {
                    columnValue = "";
                }
                query.append("'").append(columnValue).append("'");
                if (i == (columnCount - 1)) {
                    query.append(")");
                } else {
                    query.append(", ");
                }
            }
        }
    }
}
