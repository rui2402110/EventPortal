package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Product;

/**
 * 商品データアクセスクラス
 */
public class ProductDao extends Dao {
	// イベントIDが一致するイベントを表示するメソッド
	public List<Product> productGet(String eventId) throws Exception {
		List<Product> productList = new ArrayList<>();
	    Connection connection = getConnection();
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;

	    try {
	        // SQL文の作成
	        String sql = "SELECT PRODUCTS.ITEM_ID, PRODUCTS.PRODUCT_NAME, PRODUCTS.IMAGE, PRODUCTS.OVERVIEW, " +
	                     "EVENT_PRODUCT.EVENT_ID, EVENT_PRODUCT.PRICE, EVENT_PRODUCT.STOCK " +
	                     "FROM EVENT_PRODUCT " +
	                     "INNER JOIN PRODUCTS ON EVENT_PRODUCT.ITEM_ID = PRODUCTS.ITEM_ID " +
	                     "WHERE EVENT_PRODUCT.EVENT_ID = ?";

	        statement = connection.prepareStatement(sql);

	        // プリペアードステートメントにイベントIDをセット
	        statement.setString(1, eventId);

	        // SQLを実行し結果を取得
	        resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            Product product = new Product();
	            product.setItemId(resultSet.getString("ITEM_ID"));
	            product.setProductName(resultSet.getString("PRODUCT_NAME"));
	            product.setImage(resultSet.getString("IMAGE"));
	            product.setOverview(resultSet.getString("OVERVIEW"));
	            product.setEventId(resultSet.getString("EVENT_ID"));
	            product.setPrice(resultSet.getInt("PRICE"));
	            product.setStock(resultSet.getInt("STOCK"));

	            // リストに追加
	            productList.add(product);
	        }
	    } catch (Exception e) {
			throw e;
		} finally {
			// プリペアードステートメントを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}

			// コネクションを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
	    return productList ;

	}

	// 商品IDで商品情報を1件取得するメソッド
	public Product productGetById(String itemId) throws Exception {
	    Connection connection = getConnection();
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;
	    Product product = null;

	    try {
	        // PRODUCTSとEVENT_PRODUCTを結合して取得
	        String sql = "SELECT PRODUCTS.ITEM_ID, PRODUCTS.PRODUCT_NAME, PRODUCTS.IMAGE, PRODUCTS.OVERVIEW, " +
	                     "EVENT_PRODUCT.EVENT_ID, EVENT_PRODUCT.PRICE, EVENT_PRODUCT.STOCK " +
	                     "FROM PRODUCTS " +
	                     "INNER JOIN EVENT_PRODUCT ON PRODUCTS.ITEM_ID = EVENT_PRODUCT.ITEM_ID " +
	                     "WHERE PRODUCTS.ITEM_ID = ?";

	        statement = connection.prepareStatement(sql);
	        statement.setString(1, itemId);

	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            product = new Product();
	            product.setItemId(resultSet.getString("ITEM_ID"));
	            product.setProductName(resultSet.getString("PRODUCT_NAME"));
	            product.setImage(resultSet.getString("IMAGE"));
	            product.setOverview(resultSet.getString("OVERVIEW"));
	            product.setEventId(resultSet.getString("EVENT_ID"));
	            product.setPrice(resultSet.getInt("PRICE"));
	            product.setStock(resultSet.getInt("STOCK"));
	        }

	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (resultSet != null) resultSet.close();
	        if (statement != null) statement.close();
	        if (connection != null) connection.close();
	    }

	    return product;
	}

	// ProductデータとeventIdから商品を作成するメソッド
	public boolean productCreate(Product product, String eventId) throws Exception {
	    Connection connection = getConnection();
	    PreparedStatement productsStatement = null;
	    PreparedStatement eventProductStatement = null;
	    boolean result = false;

	    try {
	        // オートコミットをオフにする（2つのテーブル更新をセットにするため）
	        connection.setAutoCommit(false);

	        // 1. PRODUCTSテーブルへのインサート
	        productsStatement = connection.prepareStatement(
	            "INSERT INTO PRODUCTS (ITEM_ID, PRODUCT_NAME, IMAGE, OVERVIEW, CREATED_AT) VALUES (?, ?, ?, ?, ?)");
	        productsStatement.setString(1, product.getItemId());
	        productsStatement.setString(2, product.getProductName());
	        productsStatement.setString(3, product.getImage());
	        productsStatement.setString(4, product.getOverview());
	        productsStatement.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

	        int affected1 = productsStatement.executeUpdate();

	        // 2. EVENT_PRODUCTテーブルへのインサート
	        eventProductStatement = connection.prepareStatement(
	            "INSERT INTO EVENT_PRODUCT (EVENT_ID, ITEM_ID, PRICE, STOCK) VALUES (?, ?, ?, ?)");
	        eventProductStatement.setString(1, eventId);
	        eventProductStatement.setString(2, product.getItemId());
	        eventProductStatement.setInt(3, product.getPrice());
	        eventProductStatement.setInt(4, product.getStock());

	        int affected2 = eventProductStatement.executeUpdate();

	        // 両方成功したかチェック
	        if (affected1 > 0 && affected2 > 0) {
	            connection.commit(); // 確定
	            result = true;
	        } else {
	            connection.rollback(); // 失敗時は戻す
	        }

	    } catch (Exception e) {
	        if (connection != null) connection.rollback();
	        throw e;
	    } finally {
	        if (productsStatement != null) productsStatement.close();
	        if (eventProductStatement != null) eventProductStatement.close();
	        if (connection != null) connection.close();
	    }
	    return result;
	}

	// 商品情報を更新するメソッド
	public boolean productUpdate(Product product, String eventId) throws Exception {
	    Connection connection = getConnection();
	    PreparedStatement productsStatement = null;
	    PreparedStatement eventProductStatement = null;
	    boolean result = false;

	    try {
	        // オートコミットをオフにする
	        connection.setAutoCommit(false);

	        // 1. PRODUCTSテーブルの更新
	        productsStatement = connection.prepareStatement(
	            "UPDATE PRODUCTS SET PRODUCT_NAME = ?, IMAGE = ?, OVERVIEW = ? WHERE ITEM_ID = ?");
	        productsStatement.setString(1, product.getProductName());
	        productsStatement.setString(2, product.getImage());
	        productsStatement.setString(3, product.getOverview());
	        productsStatement.setString(4, product.getItemId());

	        int affected1 = productsStatement.executeUpdate();

	        // 2. EVENT_PRODUCTテーブルの更新
	        eventProductStatement = connection.prepareStatement(
	            "UPDATE EVENT_PRODUCT SET PRICE = ?, STOCK = ? WHERE ITEM_ID = ? AND EVENT_ID = ?");
	        eventProductStatement.setInt(1, product.getPrice());
	        eventProductStatement.setInt(2, product.getStock());
	        eventProductStatement.setString(3, product.getItemId());
	        eventProductStatement.setString(4, eventId);

	        int affected2 = eventProductStatement.executeUpdate();

	        // 両方成功したかチェック
	        if (affected1 > 0 && affected2 > 0) {
	            connection.commit();
	            result = true;
	        } else {
	            connection.rollback();
	        }

	    } catch (Exception e) {
	        if (connection != null) connection.rollback();
	        throw e;
	    } finally {
	        if (productsStatement != null) productsStatement.close();
	        if (eventProductStatement != null) eventProductStatement.close();
	        if (connection != null) connection.close();
	    }
	    return result;
	}

	// 商品を削除するメソッド
	public boolean productDelete(String itemId, String eventId) throws Exception {
	    Connection connection = getConnection();
	    PreparedStatement eventProductStatement = null;
	    PreparedStatement productsStatement = null;
	    boolean result = false;

	    try {
	        // オートコミットをオフにする
	        connection.setAutoCommit(false);

	        // 1. 先にEVENT_PRODUCTテーブルから削除（外部キー制約のため）
	        eventProductStatement = connection.prepareStatement(
	            "DELETE FROM EVENT_PRODUCT WHERE ITEM_ID = ? AND EVENT_ID = ?");
	        eventProductStatement.setString(1, itemId);
	        eventProductStatement.setString(2, eventId);

	        int affected1 = eventProductStatement.executeUpdate();

	        // 2. PRODUCTSテーブルから削除
	        productsStatement = connection.prepareStatement(
	            "DELETE FROM PRODUCTS WHERE ITEM_ID = ?");
	        productsStatement.setString(1, itemId);

	        int affected2 = productsStatement.executeUpdate();

	        // 両方成功したかチェック
	        if (affected1 > 0 && affected2 > 0) {
	            connection.commit();
	            result = true;
	        } else {
	            connection.rollback();
	        }

	    } catch (Exception e) {
	        if (connection != null) connection.rollback();
	        throw e;
	    } finally {
	        if (eventProductStatement != null) eventProductStatement.close();
	        if (productsStatement != null) productsStatement.close();
	        if (connection != null) connection.close();
	    }
	    return result;
	}

	// 最新の商品IDを取得し、新しいIDを生成するメソッド
    public String itemIdGet() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // PRODUCTSテーブルから最新のITEM_IDを降順で1件取得
            statement = connection.prepareStatement("SELECT ITEM_ID FROM PRODUCTS ORDER BY ITEM_ID DESC LIMIT 1");
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String lastItemId = resultSet.getString("ITEM_ID");
                return incrementItemId(lastItemId);
            } else {
                // データが1件もない場合の初期値
                return "ITM001";
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try { resultSet.close(); } catch (SQLException e) { throw e; }
            }
            if (statement != null) {
                try { statement.close(); } catch (SQLException e) { throw e; }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { throw e; }
            }
        }
    }

    // 商品IDをインクリメント（+1）するロジック
    private static String incrementItemId(String currentId) {
        // 例: ITM0001 のような形式を想定
        final String prefix = "ITM";
        final int idLen = 3; // 数値部分の桁数（001）

        // プレフィックス(ITM)を除いた数値部分を取得
        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);

        // 1を加算
        number++;

        // 新しいIDを生成（例: ITM + 002）
        return prefix + String.format("%0" + idLen + "d", number);
    }

}
