package util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseExporter {

    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/eventportal";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            exportDatabase("C:/eventportal_export.sql");
            System.out.println("データベースのエクスポートが完了しました！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportDatabase(String outputFile) throws Exception {
        Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

        // ヘッダー
        writer.println("-- EventPortal Database Export");
        writer.println("-- Generated: " + new java.util.Date());
        writer.println();

        DatabaseMetaData metaData = conn.getMetaData();

        // すべてのテーブルを取得
        ResultSet tables = metaData.getTables(null, "PUBLIC", "%", new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");

            // システムテーブルをスキップ
            if (tableName.startsWith("INFORMATION_SCHEMA")) {
                continue;
            }

            writer.println("-- ========================================");
            writer.println("-- Table: " + tableName);
            writer.println("-- ========================================");
            writer.println();

            // CREATE TABLE文を生成
            exportTableSchema(conn, writer, tableName);

            // INSERT文を生成
            exportTableData(conn, writer, tableName);

            writer.println();
        }

        writer.close();
        conn.close();
    }

    private static void exportTableSchema(Connection conn, PrintWriter writer, String tableName)
            throws Exception {

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM " + tableName);

        writer.println("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        boolean first = true;
        while (rs.next()) {
            if (!first) {
                writer.println(",");
            }

            String columnName = rs.getString("FIELD");
            String columnType = rs.getString("TYPE");
            String nullable = rs.getString("NULL");
            String key = rs.getString("KEY");

            writer.print("    " + columnName + " " + columnType);

            if ("NO".equals(nullable)) {
                writer.print(" NOT NULL");
            }

            if ("PRI".equals(key)) {
                writer.print(" PRIMARY KEY");
            }

            first = false;
        }

        writer.println();
        writer.println(");");
        writer.println();

        rs.close();
        stmt.close();
    }

    private static void exportTableData(Connection conn, PrintWriter writer, String tableName)
            throws Exception {

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

        int columnCount = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            writer.print("INSERT INTO " + tableName + " VALUES (");

            for (int i = 1; i <= columnCount; i++) {
                if (i > 1) {
                    writer.print(", ");
                }

                Object value = rs.getObject(i);
                if (value == null) {
                    writer.print("NULL");
                } else if (value instanceof String) {
                    writer.print("'" + value.toString().replace("'", "''") + "'");
                } else if (value instanceof java.sql.Date || value instanceof java.sql.Timestamp) {
                    writer.print("'" + value.toString() + "'");
                } else {
                    writer.print(value);
                }
            }

            writer.println(");");
        }

        rs.close();
        stmt.close();
    }
}