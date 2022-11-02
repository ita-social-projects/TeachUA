package com.softserve.teachua.service.impl;

import com.softserve.teachua.service.CertificateDateSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
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
    public void dropUnusedColumns(List<String> columns) throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        String script = "ALTER TABLE certificate_dates DROP ";
        for (String column : columns) {
            statement.executeUpdate(script + column + ";");
        }
    }
}
