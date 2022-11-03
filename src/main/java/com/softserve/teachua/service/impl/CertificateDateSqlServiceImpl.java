package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.CertificateDateSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class CertificateDateSqlServiceImpl implements CertificateDateSqlService {

    DataSource dataSource;

    @Autowired
    public CertificateDateSqlServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void moveData() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        String retrieveScript = String
                .format("SELECT %s, %s, %s FROM certificate_dates;",
                        "course_description",
                        "picture_path",
                        "project_description");
        ResultSet resultSet = statement.executeQuery(retrieveScript);
        resultSet.next();

        String updateScript =
                String
                .format("UPDATE certificate_templates SET course_description='%s', picture_path='%s', "
                                + "project_description='%s'",
                        resultSet.getString("course_description"),
                        resultSet.getString("picture_path"),
                        resultSet.getString("project_description"));
        statement.executeUpdate(updateScript);
    }


    @Override
    public void dropUnusedColumns(List<String> columns) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        String script = "ALTER TABLE certificate_dates DROP ";
        for (String column : columns) {
            statement.executeUpdate(script + column + ";");
        }
    }
}
