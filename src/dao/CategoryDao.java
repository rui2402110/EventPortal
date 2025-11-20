package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CategoryDao extends Dao {
	// カンマ区切りのカテゴリを新規に追加するメソッド
	public void categoryAdd(String categorys) throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// カテゴリを分割しリストに格納
		List<String> category = Arrays.asList(categorys.split(","));
		try {
		for (String item : category){

			String catId = categoryIdGet();
			statement = connection.prepareStatement("INSERT INTO CATEGORY(category_id , category_name)VALUES(? , ?)");
			statement.setString(1 ,catId);
			statement.setString(2 ,item);
			statement.executeUpdate();


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
	}

	// イベントIDを利用してカテゴリIDを取得するメソッド
	public String get(String eventId)  throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// 変数を定義
		String categoryId = null ;
		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT category_id FROM event_category WHERE event_id = ? ");
			// プリペアードステートメントにイベントIDをセット
			statement.setString(1, eventId);
			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
			    // データが存在する場合
			    categoryId = resultSet.getString("category_id");
			} else {
			    // データが存在しない場合
			    categoryId = "CAT001";
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
		return categoryId ;
	}

	// カテゴリIDを新規に取得するメソッド
	public String categoryIdGet() throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT category_id FROM category ORDER BY category_id DESC LIMIT 1");
			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();

			// カテゴリidを結果から取得、カテゴリidが存在しない場合は"CAT001"をリターン
			if (resultSet.next()){
				String lastEventId = resultSet.getString("category_id");
                return incrementCategoryId(lastEventId);
			} else {
				return "CAT001" ;
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
	}

	// カテゴリIDを作成するメソッド
	private static String incrementCategoryId(String currentId) {
	    final String prefix = "CAT";
	    final int idLen = 3;
        // プレフィックスを除いた数値部分を取得
        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);

        // 1を加算
        number++;

        // 新しいIDを生成
        return prefix + String.format("%0" + idLen + "d", number);
    }

	// event_categoryを作成するメソッド
	public void eventCategoryAdd(String eventId, String categoryId) throws Exception{
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		try {
			// ステートメント
			statement = connection.prepareStatement("INSERT INTO EVENT_CATEGORY(category_id , event_id)VALUES(? , ?)");
			statement.setString(1 ,categoryId);
			statement.setString(2 ,eventId);
			statement.executeUpdate();

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
	}
}




