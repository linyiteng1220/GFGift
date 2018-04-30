package com.liteng1220.lyt.sqlite;

/**
 * 文件下载记录表
 */
public class TableFileDownload {
    public static final String TABLE_NAME = "sd5404336346544543463235"; // FileDownload
    public static final String COLUMN_URL = "sd524943"; // url
    public static final String COLUMN_FILENAME = "sd3740433645324436"; // filename
    public static final String COLUMN_FILE_PATH = "sd3740433615325139"; // filePath，文件的完整路径

    private static final String[] COLUMNS_TITLE = {
            COLUMN_URL,
            COLUMN_FILENAME,
            COLUMN_FILE_PATH
    };
    private static final String[] COLUMNS_TYPE = {
            "VARCHAR NOT NULL",
            "VARCHAR NOT NULL",
            "VARCHAR NOT NULL"
    };

    public static String getCreateTableSql() {
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(TABLE_NAME);
        sql.append(" ( ");

        for (int k = 0; k < COLUMNS_TITLE.length; k++) {
            sql.append(COLUMNS_TITLE[k]);
            sql.append(" ");
            sql.append(COLUMNS_TYPE[k]);
            sql.append(",");
        }

        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(" ); ");

        return sql.toString();
    }

}
