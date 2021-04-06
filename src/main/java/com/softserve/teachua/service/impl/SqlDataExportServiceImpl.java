package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.SqlDataExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SqlDataExportServiceImpl implements SqlDataExportService {
    DataSource dataSource;

    @Autowired
    public SqlDataExportServiceImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<String> getListOfTableNames() {
        List<String> list = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null,
                    null, new String[] {"TABLE"});
            while(resultSet.next()) {
                String tableName = resultSet.getString(3);
                list.add(tableName);
            }
        } catch (Exception e) {
            // Do nothing
        }

        return list;

        /*PreparedStatement statement = dataSource.getConnection().prepareStatement(
                "select * from information_schema.tables " +
                        "where table_schema = 'public'");
        ResultSet result = statement.executeQuery();

        while(result.next()){

            StringBuilder sb = new StringBuilder();
            ResultSetMetaData rsmd = result.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            for (int i = 1; i <= numberOfColumns; i++) {
                sb.append(result.getString(i));
                if (i < numberOfColumns) {
                    sb.append(", ");
                }
            }
            String data = sb.toString();

            list.add(result.getString(3));
        }*/

        //"select table_name from user_tables").list();
    }
}
