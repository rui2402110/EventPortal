package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import bean.EntryQRCode;

/**
 * QRコードデータアクセスクラス
 */
public class QRCodeDao extends Dao {

    /**
     * ユーザーIDとイベントIDからQRコードを取得
     */
    public EntryQRCode getQRCodeByUserAndEvent(String userId, String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        EntryQRCode qrCode = null;

        try {
            statement = connection.prepareStatement(
                "SELECT qr_code_id, user_id, event_id, qr_code_data, qr_code_image_path, " +
                "issued_datetime, expiration_datetime, usage_status, used_datetime " +
                "FROM ENTRY_QRCODES WHERE user_id = ? AND event_id = ?"
            );
            statement.setString(1, userId);
            statement.setString(2, eventId);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                qrCode = new EntryQRCode();
                qrCode.setQrCodeId(rs.getString("qr_code_id"));
                qrCode.setUserId(rs.getString("user_id"));
                qrCode.setEventId(rs.getString("event_id"));
                qrCode.setQrCodeData(rs.getString("qr_code_data"));
                qrCode.setQrCodeImagePath(rs.getString("qr_code_image_path"));

                Timestamp issuedTs = rs.getTimestamp("issued_datetime");
                if (issuedTs != null) {
                    qrCode.setIssuedDateTime(issuedTs.toLocalDateTime());
                }

                Timestamp expirationTs = rs.getTimestamp("expiration_datetime");
                if (expirationTs != null) {
                    qrCode.setExpirationDateTime(expirationTs.toLocalDateTime());
                }

                qrCode.setUsageStatus(rs.getInt("usage_status"));

                Timestamp usedTs = rs.getTimestamp("used_datetime");
                if (usedTs != null) {
                    qrCode.setUsedDateTime(usedTs.toLocalDateTime());
                }
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }

        return qrCode;
    }

    /**
     * QRコードを新規作成
     */
    public String createQRCode(String eventId, String userId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            // QRコードIDを生成
            String qrCodeId = generateQRCodeId();

            // QRコードデータを生成（イベントID-ユーザーID-タイムスタンプ）
            String qrCodeData = eventId + "-" + userId + "-" + System.currentTimeMillis();

            // 有効期限を設定（発行から30日後）
            LocalDateTime issuedDateTime = LocalDateTime.now();
            LocalDateTime expirationDateTime = issuedDateTime.plusDays(30);

            statement = connection.prepareStatement(
                "INSERT INTO ENTRY_QRCODES (qr_code_id, user_id, event_id, qr_code_data, " +
                "issued_datetime, expiration_datetime, usage_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 0)"
            );
            statement.setString(1, qrCodeId);
            statement.setString(2, userId);
            statement.setString(3, eventId);
            statement.setString(4, qrCodeData);
            statement.setTimestamp(5, Timestamp.valueOf(issuedDateTime));
            statement.setTimestamp(6, Timestamp.valueOf(expirationDateTime));

            int affected = statement.executeUpdate();
            if (affected > 0) {
                System.out.println("QRコード作成成功: " + qrCodeId);
                return qrCodeId;
            } else {
                return null;
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
    }

    /**
     * QRコードを使用済みにする
     */
    public boolean markAsUsed(String qrCodeId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                "UPDATE ENTRY_QRCODES SET usage_status = 1, used_datetime = ? " +
                "WHERE qr_code_id = ? AND usage_status = 0"
            );
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(2, qrCodeId);

            int affected = statement.executeUpdate();
            return affected > 0;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
    }

    /**
     * 新しいQRコードIDを生成
     */
    private String generateQRCodeId() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(
                "SELECT qr_code_id FROM ENTRY_QRCODES ORDER BY qr_code_id DESC LIMIT 1"
            );

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("qr_code_id");
                return incrementQRCodeId(lastId);
            } else {
                return "QRC001";
            }
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
    }

    /**
     * QRコードIDをインクリメント
     */
    private String incrementQRCodeId(String currentId) {
        final String prefix = "QRC";
        final int idLen = 3;

        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%0" + idLen + "d", number);
    }
}