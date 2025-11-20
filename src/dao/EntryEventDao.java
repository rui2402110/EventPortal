package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EntryEventDao extends Dao {
	public boolean join(String userId , String eventId) throws Exception{
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// 変数を定義
		boolean result = false ;
		try {
			statement = connection.prepareStatement("INSERT INTO EVENT_ENTRYS (event_id , user_id , status)VALUES(? , ? , 2)");
			statement.setString(1 ,userId);
			statement.setString(2 ,eventId);
			statement.executeUpdate();
			int affected = statement.executeUpdate();
	        result = (affected > 0);
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
		return result;
	}
}