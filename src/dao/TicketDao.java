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

public class TicketDao extends Dao {

    // チケットIDからチケット情報を取得
    public Ticket get(String ticketId) throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        Ticket ticket = null;

        try {
            stmt = conn.prepareStatement(
                "SELECT * FROM TICKETS WHERE ticket_id = ?"
            );
            stmt.setString(1, ticketId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setUserId(rs.getString("user_id"));
                ticket.setEventId(rs.getString("event_id"));
                ticket.setQrImagePath(rs.getString("qr_image_path"));
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

    // ユーザーIDから全チケットを取得（イベント情報も含む）
    public List<Ticket> getByUserId(String userId) throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        List<Ticket> tickets = new ArrayList<>();
        EventDao eventDao = new EventDao();

        try {
            stmt = conn.prepareStatement(
                "SELECT * FROM TICKETS WHERE user_id = ? ORDER BY created_at DESC"
            );
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getString("ticket_id"));
                ticket.setUserId(rs.getString("user_id"));
                ticket.setEventId(rs.getString("event_id"));
                ticket.setQrImagePath(rs.getString("qr_image_path"));
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

                // イベント情報を取得
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

    // イベントIDとユーザーIDからチケットを取得
    public Ticket getByEventAndUser(String eventId, String userId) throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        Ticket ticket = null;

        try {
            stmt = conn.prepareStatement(
                "SELECT * FROM TICKETS WHERE event_id = ? AND user_id = ?"
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

    // チケットを使用済みにする
    public boolean markAsUsed(String ticketId, LocalDateTime usedTime) throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(
                "UPDATE TICKETS SET status = 2, used_at = ? WHERE ticket_id = ? AND status = 1"
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

    // チケットを作成
    public boolean create(Ticket ticket) throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(
                "INSERT INTO TICKETS (ticket_id, user_id, event_id, qr_image_path, status, ticket_info, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, ticket.getTicketId());
            stmt.setString(2, ticket.getUserId());
            stmt.setString(3, ticket.getEventId());
            stmt.setString(4, ticket.getQrImagePath());
            stmt.setInt(5, ticket.getStatus());
            stmt.setString(6, ticket.getTicketInfo());
            stmt.setTimestamp(7, Timestamp.valueOf(ticket.getCreatedAt()));

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // イベントの入場済み人数を取得
    public int getAdmittedCount(String eventId) throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;
        int count = 0;

        try {
            stmt = conn.prepareStatement(
                "SELECT COUNT(*) as count FROM TICKETS WHERE event_id = ? AND status = 2"
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

    // 新しいチケットIDを生成
    public String generateTicketId() throws Exception {
        Connection conn = getConnection();
        PreparedStatement stmt = null;

        try {
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

    // チケットIDをインクリメント
    private String incrementTicketId(String currentId) {
        final String prefix = "TKT";
        final int idLen = 3;

        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%0" + idLen + "d", number);
    }
}