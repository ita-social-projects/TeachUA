package com.softserve.teachua.dao.service;


import com.softserve.teachua.dao.BackupDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackupService {

    private final BackupDaoImpl backupDao;

    @Autowired
    public BackupService(BackupDaoImpl backupDao) {
        this.backupDao = backupDao;
    }

    public String getTable(String tableName) {
        return backupDao.getTable(tableName);
    }

    public String getAllTables(String tableNames) {

        String[] names = tableNames.split(",");
        String text = "";
        for (String i : names) {
            text = text + getTable(i);
        }

        return text;
    }
}
