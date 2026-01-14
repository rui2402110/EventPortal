package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import bean.Event;
import bean.Ticket;

/**
 * チケットDAO（完全版）
 */
public class TicketDao extends Dao {

    /**
     * チケットIDからチケット情報を取得
     */
    public Ticket get(String ticketId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Ticket ticket = null;

        try {
            conn = getConnection();

            stmt = conn.prepareStatement(
                "SELECT ticket_id, user_id, event_id, qr_image_path, qr_image_data, " +
                "status, ticket_info, created_at, used_at " +
                "FROM TICKETS WHERE ticket_id = ?"
            );
            stmt.setString(1, ticketId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setUserId(rs.getString("user_id"));
                ticket.setEventId(rs.getString("event_id"));
                ticket.setQrImagePath(rs.getString("qr_image_path"));
                ticket.setQrImageData(rs.getString("qr_image_data"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setTicketInfo(rs.getString("ticket_info"));

                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    ticket.setCreatedAt(createdAtTs.toLocalDateTime());
                }

                Timestamp usedAtTs = rs.getTimestamp("used_at");
                if (usedAtTs != null) {
                    ticket.setUsedAt(usedAtTs.toLocalDateTime());
                }
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return ticket;
    }

    /**
     * ユーザーIDから全チケットを取得（イベント情報も含む）
     */
    public List<Ticket> getByUserId(String userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Ticket> tickets = new ArrayList<>();
        EventDao eventDao = new EventDao();

        try {
            conn = getConnection();

            stmt = conn.prepareStatement(
                "SELECT ticket_id, user_id, event_id, qr_image_path, qr_image_data, " +
                "status, ticket_info, created_at, used_at " +
                "FROM TICKETS WHERE user_id = ? ORDER BY created_at DESC"
            );
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setUserId(rs.getString("user_id"));
                ticket.setEventId(rs.getString("event_id"));
                ticket.setQrImagePath(rs.getString("qr_image_path"));
                ticket.setQrImageData(rs.getString("qr_image_data"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setTicketInfo(rs.getString("ticket_info"));

                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    ticket.setCreatedAt(createdAtTs.toLocalDateTime());
                }

                Timestamp usedAtTs = rs.getTimestamp("used_at");
                if (usedAtTs != null) {
                    ticket.setUsedAt(usedAtTs.toLocalDateTime());
                }

                // イベント情報を取得して設定
                Event event = eventDao.get(ticket.getEventId());
                ticket.setEvent(event);

                tickets.add(ticket);
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return tickets;
    }

    /**
     * イベントIDから全チケットを取得
     */
    public List<Ticket> getByEventId(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Ticket> tickets = new ArrayList<>();

        try {
            conn = getConnection();

            stmt = conn.prepareStatement(
                "SELECT ticket_id, user_id, event_id, qr_image_path, qr_image_data, " +
                "status, ticket_info, created_at, used_at " +
                "FROM TICKETS WHERE event_id = ? ORDER BY created_at DESC"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setUserId(rs.getString("user_id"));
                ticket.setEventId(rs.getString("event_id"));
                ticket.setQrImagePath(rs.getString("qr_image_path"));
                ticket.setQrImageData(rs.getString("qr_image_data"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setTicketInfo(rs.getString("ticket_info"));

                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    ticket.setCreatedAt(createdAtTs.toLocalDateTime());
                }

                Timestamp usedAtTs = rs.getTimestamp("used_at");
                if (usedAtTs != null) {
                    ticket.setUsedAt(usedAtTs.toLocalDateTime());
                }

                tickets.add(ticket);
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return tickets;
    }

    /**
     * イベントIDとユーザーIDからチケットを取得
     */
    public Ticket getByEventAndUser(String eventId, String userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Ticket ticket = null;

        try {
            conn = getConnection();

            stmt = conn.prepareStatement(
                "SELECT ticket_id, user_id, event_id, qr_image_path, qr_image_data, " +
                "status, ticket_info, created_at, used_at " +
                "FROM TICKETS WHERE event_id = ? AND user_id = ?"
            );
            stmt.setString(1, eventId);
            stmt.setString(2, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setUserId(rs.getString("user_id"));
                ticket.setEventId(rs.getString("event_id"));
                ticket.setQrImagePath(rs.getString("qr_image_path"));
                ticket.setQrImageData(rs.getString("qr_image_data"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setTicketInfo(rs.getString("ticket_info"));

                Timestamp createdAtTs = rs.getTimestamp("created_at");
                if (createdAtTs != null) {
                    ticket.setCreatedAt(createdAtTs.toLocalDateTime());
                }

                Timestamp usedAtTs = rs.getTimestamp("used_at");
                if (usedAtTs != null) {
                    ticket.setUsedAt(usedAtTs.toLocalDateTime());
                }
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return ticket;
    }

    /**
     * チケットを使用済みにする
     */
    public boolean markAsUsed(String ticketId, LocalDateTime usedTime) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();

            stmt = conn.prepareStatement(
                "UPDATE TICKETS SET status = 2, used_at = ? " +
                "WHERE ticket_id = ? AND status = 1"
            );
            stmt.setTimestamp(1, Timestamp.valueOf(usedTime));
            stmt.setString(2, ticketId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * チケットを作成
     */
    public boolean create(Ticket ticket) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();

            stmt = conn.prepareStatement(
                "INSERT INTO TICKETS (ticket_id, user_id, event_id, qr_image_path, " +
                "qr_image_data, status, ticket_info, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, ticket.getTicketId());
            stmt.setString(2, ticket.getUserId());
            stmt.setString(3, ticket.getEventId());
            stmt.setString(4, ticket.getQrImagePath());
            stmt.setString(5, ticket.getQrImageData());
            stmt.setInt(6, ticket.getStatus());
            stmt.setString(7, ticket.getTicketInfo());
            stmt.setTimestamp(8, Timestamp.valueOf(ticket.getCreatedAt()));

            int affected = stmt.executeUpdate();

            System.out.println("チケット作成: " + ticket.getTicketId() +
                             " (QR画像データ: " + (ticket.getQrImageData() != null ? "あり" : "なし") + ")");

            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * イベントの入場済み人数を取得
     */
    public int getAdmittedCount(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT COUNT(*) as count FROM TICKETS " +
                "WHERE event_id = ? AND status = 2"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return count;
    }

    /**
     * イベントの有効チケット数を取得
     */
    public int getValidTicketCount(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT COUNT(*) as count FROM TICKETS " +
                "WHERE event_id = ? AND status = 1"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return count;
    }

    /**
     * イベントの全チケット数を取得
     */
    public int getTotalTicketCount(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT COUNT(*) as count FROM TICKETS WHERE event_id = ?"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return count;
    }

    /**
     * 新しいチケットIDを生成
     */
    public String generateTicketId() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT ticket_id FROM TICKETS ORDER BY ticket_id DESC LIMIT 1"
            );

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("ticket_id");
                return incrementTicketId(lastId);
            } else {
                return "TKT001";
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * チケットIDをインクリメント
     */
    private String incrementTicketId(String currentId) {
        final String prefix = "TKT";
        final int idLen = 3;

        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%0" + idLen + "d", number);
    }

    /**
     * チケットを削除（論理削除：status=3に設定）
     */
    public boolean invalidate(String ticketId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "UPDATE TICKETS SET status = 3 WHERE ticket_id = ?"
            );
            stmt.setString(1, ticketId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * チケットを物理削除
     */
    public boolean delete(String ticketId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "DELETE FROM TICKETS WHERE ticket_id = ?"
            );
            stmt.setString(1, ticketId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}