package com.softserve.teachua.dao.service;

import com.softserve.teachua.dao.BackupDaoImpl;
import org.springframework.stereotype.Component;

@Component
public class BackupService {
    private final BackupDaoImpl backupDao;

    public BackupService(BackupDaoImpl backupDao) {
        this.backupDao = backupDao;
    }

    public String getTable(String tableName) {
        return backupDao.getTable(tableName);
    }

    public String getAllTables(String tableNames) {
        String[] names = tableNames.split(",");
        StringBuilder stringBuilder = new StringBuilder();
        for (String i : names) {
            stringBuilder.append(getTable(i));
        }

        return stringBuilder.toString();
    }
}
