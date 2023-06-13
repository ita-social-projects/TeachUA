package com.softserve.club.service.impl;

import com.softserve.club.service.SqlDataExportService;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SqlDataExportServiceImpl implements SqlDataExportService {
    final DataSource dataSource;

    public SqlDataExportServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String createScript() throws SQLException {
        Connection connection = dataSource.getConnection();
        StringBuilder script = new StringBuilder();
        for (String table : getListOfTableNames(connection)) {
            ResultSet resultSet = getTableRows(table, connection);
            List<String> columns = getColumnsNames(resultSet);

            while (resultSet.next()) {
                List<String> values = getRowValues(resultSet, columns.size());
                script.append("\n");
                script.append(createSqlInsertStatement(table, values, columns));
            }
            script.append("\n");
        }
        return script.toString();
    }

    private String createSqlInsertStatement(String tableName, List<String> values, List<String> columns) {
        if (values.size() != columns.size()) {
            return null;
        }

        StringBuilder statement = new StringBuilder();

        statement.append("INSERT INTO ");
        statement.append(tableName);
        statement.append("(");

        boolean needComa = false;
        for (String column : columns) {
            if (needComa) {
                statement.append(", ");
            } else {
                needComa = true;
            }

            statement.append(column);
        }

        needComa = false;
        statement.append(") VALUES(");

        for (String value : values) {
            if (needComa) {
                statement.append(", ");
            } else {
                needComa = true;
            }
            if (value == null) {
                statement.append("null");
            } else {
                statement.append("'");
                statement.append(value.replace('\'', '`'));
                statement.append("'");
            }
        }
        statement.append(");");
        return statement.toString();
    }

    private List<String> getRowValues(ResultSet resultSet, int columnCount) throws SQLException {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            list.add(resultSet.getString(i));
        }
        return list;
    }

    private List<String> getColumnsNames(ResultSet resultSet) throws SQLException {
        List<String> list = new ArrayList<>();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            list.add(rsmd.getColumnName(i));
        }
        return list;
    }

    private ResultSet getTableRows(String tableName, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("select * from ?")) {
            statement.setString(1, tableName);
            return statement.executeQuery();
        }
    }

    private List<String> getListOfTableNames(Connection connection) {
        List<String> list = new ArrayList<>();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] { "TABLE" });
            while (resultSet.next()) {
                String tableName = resultSet.getString(3);
                list.add(tableName);
            }
        } catch (Exception e) {
            // Do nothing
        }

        return list;
    }
}
