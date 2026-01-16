spackage dao;


public class ProductDao extends Dao {
	// 商品とイベント販売情報を新規に追加するメソッド
	public void productAdd(String eventId, ProductBean product) throws Exception {
		// コネクションを確立
	    Connection connection = getConnection();
	    // プリペアードステートメント
	    PreparedStatement statement1 = null;
			PreparedStatement statement2 = null;


	}
		}