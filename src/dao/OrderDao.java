package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bean.Order;

/**
 * 注文データアクセスクラス
 */
public class OrderDao extends Dao {

    /**
     * 注文IDから注文情報を取得
     */
    public Order get(String orderId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Order order = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT order_id, user_id, event_id, ticket_id, order_date, " +
                "total_amount, status FROM ORDERS WHERE order_id = ?"
            );
            stmt.setString(1, orderId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setUserId(rs.getString("user_id"));
                order.setEventId(rs.getString("event_id"));
                order.setTicketId(rs.getString("ticket_id"));

                Timestamp orderDateTs = rs.getTimestamp("order_date");
                if (orderDateTs != null) {
                    order.setOrderDate(orderDateTs.toLocalDateTime());
                }

                order.setTotalAmount(rs.getInt("total_amount"));
                order.setStatus(rs.getInt("status"));
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return order;
    }

    /**
     * ユーザーIDとイベントIDから注文一覧を取得
     */
    public List<Order> getByUserAndEvent(String userId, String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> orders = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT order_id, user_id, event_id, ticket_id, order_date, " +
                "total_amount, status FROM ORDERS " +
                "WHERE user_id = ? AND event_id = ? " +
                "ORDER BY order_date DESC"
            );
            stmt.setString(1, userId);
            stmt.setString(2, eventId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setUserId(rs.getString("user_id"));
                order.setEventId(rs.getString("event_id"));
                order.setTicketId(rs.getString("ticket_id"));

                Timestamp orderDateTs = rs.getTimestamp("order_date");
                if (orderDateTs != null) {
                    order.setOrderDate(orderDateTs.toLocalDateTime());
                }

                order.setTotalAmount(rs.getInt("total_amount"));
                order.setStatus(rs.getInt("status"));
                orders.add(order);
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return orders;
    }

    /**
     * イベントIDから全注文を取得（主催者用）
     */
    public List<Order> getByEvent(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Order> orders = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT order_id, user_id, event_id, ticket_id, order_date, " +
                "total_amount, status FROM ORDERS " +
                "WHERE event_id = ? " +
                "ORDER BY order_date DESC"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getString("order_id"));
                order.setUserId(rs.getString("user_id"));
                order.setEventId(rs.getString("event_id"));
                order.setTicketId(rs.getString("ticket_id"));

                Timestamp orderDateTs = rs.getTimestamp("order_date");
                if (orderDateTs != null) {
                    order.setOrderDate(orderDateTs.toLocalDateTime());
                }

                order.setTotalAmount(rs.getInt("total_amount"));
                order.setStatus(rs.getInt("status"));
                orders.add(order);
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return orders;
    }

    /**
     * 注文を作成
     */
    public boolean create(Order order) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "INSERT INTO ORDERS (order_id, user_id, event_id, ticket_id, " +
                "order_date, total_amount, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getUserId());
            stmt.setString(3, order.getEventId());
            stmt.setString(4, order.getTicketId());
            stmt.setTimestamp(5, Timestamp.valueOf(order.getOrderDate()));
            stmt.setInt(6, order.getTotalAmount());
            stmt.setInt(7, order.getStatus());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 注文ステータスを更新
     */
    public boolean updateStatus(String orderId, int newStatus) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "UPDATE ORDERS SET status = ? WHERE order_id = ?"
            );
            stmt.setInt(1, newStatus);
            stmt.setString(2, orderId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 新しい注文IDを生成
     */
    public String generateOrderId() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT order_id FROM ORDERS ORDER BY order_id DESC LIMIT 1"
            );

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("order_id");
                return incrementOrderId(lastId);
            } else {
                return "ORD001";
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 注文IDをインクリメント
     */
    private String incrementOrderId(String currentId) {
        final String prefix = "ORD";
        final int idLen = 3;

        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%0" + idLen + "d", number);
    }

    /**
     * イベントの注文統計を取得
     */
    public int getTotalOrderAmount(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int total = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT COALESCE(SUM(total_amount), 0) as total " +
                "FROM ORDERS WHERE event_id = ? AND status != 9"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return total;
    }

    /**
     * イベントの注文数を取得
     */
    public int getOrderCount(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT COUNT(*) as count FROM ORDERS " +
                "WHERE event_id = ? AND status != 9"
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
}