package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductHoldDao extends Dao {

    /**
     * 在庫の減算と売上額の加算を個別に取得・計算して更新する
     */
    public void updateStockAndTotalPay(String eventId, String itemId, int soldCount, int salesAmount) throws Exception {
        Connection con = getConnection();

        try {
            con.setAutoCommit(false); // トランザクション開始

            // --- 1. 在庫の更新処理 ---
            // 現在の在庫を取得
            int currentStock = 0;
            String sqlGetStock = "SELECT stock FROM event_product WHERE item_id = ?";
            try (PreparedStatement st1 = con.prepareStatement(sqlGetStock)) {
                st1.setString(1, itemId);
                try (ResultSet rs1 = st1.executeQuery()) {
                    if (rs1.next()) {
                        currentStock = rs1.getInt("stock");
                    }
                }
            }

            // 在庫を減らしてアップデート
            int newStock = currentStock - soldCount;
            String sqlUpdateStock = "UPDATE event_product SET stock = ? WHERE item_id = ?";
            try (PreparedStatement st2 = con.prepareStatement(sqlUpdateStock)) {
                st2.setInt(1, newStock);
                st2.setString(2, itemId);
                st2.executeUpdate();
            }

            // --- 2. TOTALPAYの更新処理 ---
            // 現在の合計金額を取得
            int currentTotalPay = 0;
            String sqlGetPay = "SELECT total_payment FROM events WHERE event_id = ?";
            try (PreparedStatement st3 = con.prepareStatement(sqlGetPay)) {
                st3.setString(1, eventId);
                try (ResultSet rs2 = st3.executeQuery()) {
                    if (rs2.next()) {
                        currentTotalPay = rs2.getInt("total_payment");
                    }
                }
            }

            // 売上を加算してアップデート
            int newTotalPay = currentTotalPay + salesAmount;
            String sqlUpdatePay = "UPDATE events SET total_payment = ? WHERE event_id = ?";
            try (PreparedStatement st4 = con.prepareStatement(sqlUpdatePay)) {
                st4.setInt(1, newTotalPay);
                st4.setString(2, eventId);
                st4.executeUpdate();
            }

            con.commit(); // すべての処理が成功したら確定
        } catch (Exception e) {
            con.rollback(); // どこかでエラーが起きたら元に戻す
            throw e;
        } finally {
            con.close();
        }
    }
}
