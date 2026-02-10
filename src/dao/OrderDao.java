package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.Order;

/**
 * 注文DAO
 */
public class OrderDao extends Dao {

    /**
     * 注文を登録（在庫を減らす）
     */
    public boolean createOrder(Order order) throws Exception {
        Connection connection = getConnection();
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            // トランザクション開始
            connection.setAutoCommit(false);

            // 在庫確認
            String checkSql = "SELECT stock_quantity FROM MENUS WHERE menu_id = ? FOR UPDATE";
            checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setString(1, order.getMenuId());
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.err.println("✗ メニューが見つかりません");
                connection.rollback();
                return false;
            }

            int currentStock = rs.getInt("stock_quantity");
            System.out.println("現在の在庫: " + currentStock);
            System.out.println("注文数量: " + order.getQuantity());

            if (currentStock < order.getQuantity()) {
                System.err.println("✗ 在庫不足");
                connection.rollback();
                return false;
            }

            // 在庫を減らす
            String updateSql = "UPDATE MENUS SET stock_quantity = stock_quantity - ? WHERE menu_id = ?";
            updateStmt = connection.prepareStatement(updateSql);
            updateStmt.setInt(1, order.getQuantity());
            updateStmt.setString(2, order.getMenuId());
            int updateCount = updateStmt.executeUpdate();

            if (updateCount == 0) {
                System.err.println("✗ 在庫更新失敗");
                connection.rollback();
                return false;
            }

            System.out.println("✓ 在庫更新成功: " + (currentStock - order.getQuantity()) + "個");

            // 注文を登録
            String insertSql = "INSERT INTO ORDERS (order_id, menu_id, user_id, event_id, quantity, total_price, status) " +
                               "VALUES (?, ?, ?, ?, ?, ?, '注文済み')";
            insertStmt = connection.prepareStatement(insertSql);
            insertStmt.setString(1, order.getOrderId());
            insertStmt.setString(2, order.getMenuId());
            insertStmt.setString(3, order.getUserId());
            insertStmt.setString(4, order.getEventId());
            insertStmt.setInt(5, order.getQuantity());
            insertStmt.setInt(6, order.getTotalPrice());

            int insertCount = insertStmt.executeUpdate();

            if (insertCount == 0) {
                System.err.println("✗ 注文登録失敗");
                connection.rollback();
                return false;
            }

            System.out.println("✓ 注文登録成功: " + order.getOrderId());

            // コミット
            connection.commit();
            return true;

        } catch (Exception e) {
            System.err.println("✗ 注文処理エラー: " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (checkStmt != null) checkStmt.close();
            if (updateStmt != null) updateStmt.close();
            if (insertStmt != null) insertStmt.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }
}