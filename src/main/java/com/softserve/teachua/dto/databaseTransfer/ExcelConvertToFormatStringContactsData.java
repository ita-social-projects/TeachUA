package com.softserve.teachua.dto.databaseTransfer;

import org.springframework.stereotype.Component;

@Component
public class ExcelConvertToFormatStringContactsData {
    public String collectAllContactsData(String siteUrlField, String phoneField) {
        return parseSiteUrlCell(siteUrlField).concat(parsePhoneCell(phoneField));
    }

    private String parsePhoneCell(String phoneRow) {
        String formattedData = phoneRow.replace(" ", "");
        String[] phones = formattedData.split("[,;]");

        StringBuilder builder = new StringBuilder();
        for (String phone : phones) {
            builder.append("1::");
            builder.append(phone);
            builder.append(", ");
        }
        return builder.toString();
    }

    private String parseSiteUrlCell(String siteUrl) {
        StringBuilder builder = new StringBuilder();

        String formattedData = siteUrl.replace(" ", "");

        String[] siteUrls = formattedData.split("[,;]");

        for (String url : siteUrls) {
            if (url.contains("facebook")) {
                builder.append("2::");
                builder.append(url);
                builder.append(", ");
            } else {
                builder.append("6::");
                builder.append(url);
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
