package com.softserve.teachua.dao;

import com.softserve.teachua.exception.BadRequestException;
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
        StringBuilder query = new StringBuilder("insert into ");
        query.append(tableName);
        query.append("(");

        String sql = "SELECT * FROM ?";
        try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tableName);
            ResultSet resultSet = ps.executeQuery();
            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();

            for (int i = 0; i < columnCount; i++) {
                String columnName;
                try {
                    columnName = metadata.getColumnName(i + 1);
                    query.append(columnName);
                    if (i == (columnCount - 1)) {
                        query.append(" ");
                    } else {
                        query.append(", ");
                    }
                } catch (SQLException e) {
                    throw new BadRequestException("not found2");
                }
            }
            query.append(") values\n");

            try {
                boolean bln = false;
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
                        query.append("'" + columnValue + "'");
                        if (i == (columnCount - 1)) {
                            query.append(")");
                        } else {
                            query.append(", ");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new BadRequestException("not found3");
            }
            query.append(";\n");

            log.info(query.toString());
        } catch (SQLException e) {
            log.error("Exception occurred while interact with database", e);
        }
        return query.toString();
    }
}
