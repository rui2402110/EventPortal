package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Event;

public class EntryEventDao extends Dao {

	// イベントに参加するメソッド
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

	// ユーザーIDを受け取り、参加しているイベントを全て取得するメソッド
	public List<Event> entryJoinedEventGet(String userId) throws Exception{
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// リストを定義
		List<Event> list = new ArrayList<>();
		try{
			statement = connection.prepareStatement("SELECT e.* , ee.status FROM EVENTS e JOIN EVENT_ENTRYS ee ON e.event_id = ee.event_id WHERE ee.user_id = ?;");
			// プリペアードステートメントにユーザーIDをセット
			statement.setString(1, userId);
			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				System.out.println("リザルトセット回ってます");

				// ループ内でEventクラスを再定義（各行ごとに新しいインスタンスを作成）
				Event event = new Event();
				// SELECTしたデータをインスタンスにセット
			    event.setEventId(resultSet.getString("event_id"));
			    event.setEventName(resultSet.getString("event_name"));
			    event.setEventOverview(resultSet.getString("event_overview"));
			    event.setHoldingDate(resultSet.getDate("holding_date") != null ? resultSet.getDate("holding_date").toLocalDate() : null);
			    event.setHoldingTime(resultSet.getTime("holding_time") != null ? resultSet.getTime("holding_time").toLocalTime() : null);
			    event.setAddress(resultSet.getString("address"));
			    event.setMapOutOfHall(resultSet.getString("map_out_of_hall"));
			    event.setMapInHall(resultSet.getString("map_in_hall"));
			    event.setMaxCount(resultSet.getInt("max_count"));
			    event.setPhoneNumber(resultSet.getString("phone_number"));
			    event.setLink(resultSet.getString("link"));
			    event.setCredit(resultSet.getString("credit"));
			    event.setUserId(resultSet.getString("user_id"));
			    event.setTicketInfo(resultSet.getString("ticket_info"));
			    event.setEventHoldState(resultSet.getString("event_hold_state"));
			    event.setEventAddDate(resultSet.getDate("event_add_date") != null ? resultSet.getDate("event_add_date").toLocalDate() : null);
			    event.setTotalPayment(resultSet.getInt("TOTAL_PAYMENT"));

			    // 作成したインスタンスをリストに格納
			    list.add(event);
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
		return list;

	}
	// イベントIDを受け取り、画像のurlを取得するメソッド
	public String urlGet(String eventId , int areaType) throws Exception{
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// 変数を定義
		String url = null;
		try{
			//  areaTypeが1の場合は会場内マップのurlを、2の場合は会場外マップのurlを検索
			if (areaType == 1){
				statement = connection.prepareStatement("SELECT MAP_IN_HALL FROM EVENTS WHERE EVENT_ID=?;");
			} else {
				statement = connection.prepareStatement("SELECT MAP_OUT_OF_HALL FROM EVENTS WHERE EVENT_ID=?;");
			}
			statement.setString(1, eventId);
			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()){
			    url = resultSet.getString(1);  // SELECT句の1番目のカラム
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
		return url;
	}

}