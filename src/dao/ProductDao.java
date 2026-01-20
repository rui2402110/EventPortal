package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Product;

/**
 * 商品データアクセスクラス
 */
public class ProductDao extends Dao {

    /**
     * 商品IDから商品情報を取得
     */
    public Product get(String itemId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Product product = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT item_id, product_name, overview, image " +
                "FROM PRODUCTS WHERE item_id = ?"
            );
            stmt.setString(1, itemId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setItemId(rs.getString("item_id"));
                product.setProductName(rs.getString("product_name"));
                product.setOverview(rs.getString("overview"));
                product.setImage(rs.getString("image"));
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return product;
    }

    /**
     * イベントIDから商品一覧を取得（価格・在庫含む）
     */
    public List<Product> getByEventId(String eventId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Product> products = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT p.item_id, p.product_name, p.overview, p.image, " +
                "ep.price, ep.stock, ep.event_id " +
                "FROM PRODUCTS p " +
                "INNER JOIN EVENT_PRODUCTS ep ON p.item_id = ep.item_id " +
                "WHERE ep.event_id = ? " +
                "ORDER BY p.item_id"
            );
            stmt.setString(1, eventId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setItemId(rs.getString("item_id"));
                product.setProductName(rs.getString("product_name"));
                product.setOverview(rs.getString("overview"));
                product.setImage(rs.getString("image"));
                product.setPrice(rs.getInt("price"));
                product.setStock(rs.getInt("stock"));
                product.setEventId(rs.getString("event_id"));
                products.add(product);
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return products;
    }

    /**
     * 商品を新規登録
     */
    public boolean create(Product product) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "INSERT INTO PRODUCTS (item_id, product_name, overview, image) " +
                "VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, product.getItemId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getOverview());
            stmt.setString(4, product.getImage());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 商品情報を更新
     */
    public boolean update(Product product) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "UPDATE PRODUCTS SET product_name = ?, overview = ?, image = ? " +
                "WHERE item_id = ?"
            );
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getOverview());
            stmt.setString(3, product.getImage());
            stmt.setString(4, product.getItemId());

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * イベント商品（価格・在庫）を登録
     */
    public boolean addEventProduct(String eventId, String itemId, int price, int stock) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "INSERT INTO EVENT_PRODUCTS (event_id, item_id, price, stock) " +
                "VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, eventId);
            stmt.setString(2, itemId);
            stmt.setInt(3, price);
            stmt.setInt(4, stock);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * イベント商品の価格・在庫を更新
     */
    public boolean updateEventProduct(String eventId, String itemId, int price, int stock) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "UPDATE EVENT_PRODUCTS SET price = ?, stock = ? " +
                "WHERE event_id = ? AND item_id = ?"
            );
            stmt.setInt(1, price);
            stmt.setInt(2, stock);
            stmt.setString(3, eventId);
            stmt.setString(4, itemId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 在庫を更新
     */
    public boolean updateStock(String eventId, String itemId, int newStock) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "UPDATE EVENT_PRODUCTS SET stock = ? " +
                "WHERE event_id = ? AND item_id = ?"
            );
            stmt.setInt(1, newStock);
            stmt.setString(2, eventId);
            stmt.setString(3, itemId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 在庫を減らす（注文時）
     */
    public boolean decreaseStock(String eventId, String itemId, int quantity) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "UPDATE EVENT_PRODUCTS SET stock = stock - ? " +
                "WHERE event_id = ? AND item_id = ? AND stock >= ?"
            );
            stmt.setInt(1, quantity);
            stmt.setString(2, eventId);
            stmt.setString(3, itemId);
            stmt.setInt(4, quantity);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 新しい商品IDを生成
     */
    public String generateItemId() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT item_id FROM PRODUCTS ORDER BY item_id DESC LIMIT 1"
            );

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("item_id");
                return incrementItemId(lastId);
            } else {
                return "ITM001";
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * 商品IDをインクリメント
     */
    private String incrementItemId(String currentId) {
        final String prefix = "ITM";
        final int idLen = 3;

        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);
        number++;

        return prefix + String.format("%0" + idLen + "d", number);
    }

    /**
     * 商品を削除
     */
    public boolean delete(String itemId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "DELETE FROM PRODUCTS WHERE item_id = ?"
            );
            stmt.setString(1, itemId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * イベント商品を削除
     */
    public boolean deleteEventProduct(String eventId, String itemId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "DELETE FROM EVENT_PRODUCTS WHERE event_id = ? AND item_id = ?"
            );
            stmt.setString(1, eventId);
            stmt.setString(2, itemId);

            int affected = stmt.executeUpdate();
            return affected > 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}