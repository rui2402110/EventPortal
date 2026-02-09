package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import bean.Product;

/**
 * 商品データアクセスクラス
 */
public class ProductDao extends Dao {
	public boolean productCreate(Product product) throws Exception {
	    // コネクションを確立
	    Connection connection = getConnection();
	    // プリペアードステートメント
	    PreparedStatement statement1 = null;
	    PreparedStatement statement2 = null;

	    try {
	        // statement1の初期化（typo修正: stetment → statement）
	        statement1 = connection.prepareStatement(
	            "INSERT INTO PRODUCTS(ITEM_ID, PRODUCT_NAME, IMAGE, OVERVIEW, CREATED_AT) VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP)"
	        );
	        statement1.setString(1, product.getItemId());
	        statement1.setString(2, product.getProductName());
	        statement1.setString(3, product.getImage());
	        statement1.setString(4, product.getOverview());
	        statement1.executeUpdate();

	        // statement2の初期化
	        statement2 = connection.prepareStatement(
	            "INSERT INTO EVENT_PRODUCT(EVENT_ID, ITEM_ID, PRICE, STOCK) VALUES(?, ?, ?, ?)"
	        );
	        statement2.setString(1, product.getEventId());
	        statement2.setString(2, product.getItemId());
	        statement2.setInt(3, product.getPrice());
	        statement2.setInt(4, product.getStock());
	        statement2.executeUpdate();

	        return true;

	    } catch (Exception e) {
	        // エラーハンドリング
	        e.printStackTrace();
	        throw e;
	    } finally {
	        // リソースのクローズ
	        if (statement1 != null) {
	            statement1.close();
	        }
	        if (statement2 != null) {
	            statement2.close();
	        }
	        if (connection != null) {
	            connection.close();
	        }
	    }
	}
}