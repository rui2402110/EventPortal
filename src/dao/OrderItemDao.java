package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.OrderItem;
import bean.Product;

/**
 * 注文明細データアクセスクラス
 */
public class OrderItemDao extends Dao {

    /**
     * 注文IDから注文明細一覧を取得
     */
    public List<OrderItem> getByOrderId(String orderId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<OrderItem> items = new ArrayList<>();
        ProductDao productDao = new ProductDao();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT order_item_id, order_id, item_id, quantity, unit_price " +
                "FROM ORDER_ITEMS WHERE order_id = ?"
            );
            stmt.setString(1, orderId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getString("order_item_id"));
                item.setOrderId(rs.getString("order_id"));
                item.setItemId(rs.getString("item_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getInt("unit_price"));

                // 商品情報を取得
                Product product = productDao.get(item.getItemId());
                item.setProduct(product);

                items.add(item);
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return items;
    }

    /**
     * 注文明細を作成
     */
    public boolean create(OrderItem item) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "INSERT INTO ORDER_ITEMS (order_item_id, order_id, item_id, " +
                "quantity, unit_price) VALUES (?, ?, ?, ?, ?)"
            );
            stmt.setString(1, item.getOrderItemId());
            stmt.setString(2, item.getOrderId());
            stmt.setString(3, item.getItemId());
            stmt.setInt(4, item.getQuantity());
            stmt.setInt(5, item.getUnitPrice());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 新しい注文明細IDを生成
     */
    public String generateOrderItemId() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT order_item_id FROM ORDER_ITEMS " +
                "ORDER BY order_item_id DESC LIMIT 1"
            );

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("order_item_id");
                return incrementOrderItemId(lastId);
            } else {
                return "ORI001";
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 注文明細IDをインクリメント
     */
    private String incrementOrderItemId(String currentId) {
        final String prefix = "ORI";
        final int idLen = 3;

        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%0" + idLen + "d", number);
    }

    /**
     * 商品の販売数を集計
     */
    public int getTotalSoldQuantity(String eventId, String itemId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        int total = 0;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT COALESCE(SUM(oi.quantity), 0) as total " +
                "FROM ORDER_ITEMS oi " +
                "INNER JOIN ORDERS o ON oi.order_id = o.order_id " +
                "WHERE o.event_id = ? AND oi.item_id = ? AND o.status != 9"
            );
            stmt.setString(1, eventId);
            stmt.setString(2, itemId);

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
}