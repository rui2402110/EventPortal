package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Event;
public class EventDao extends Dao {

	// ユーザーIDを使用して、ユーザーIDが合致するイベントを全て取得するメソッド
	public List<Event> userIdFilter(String userId) throws Exception {

		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		// リストを定義
		List<Event> list = new ArrayList<>();

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM EVENTS WHERE user_id=?");
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
			    event.setCategoryId(resultSet.getString("category_id"));
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

	// 状態が開催前のイベントを全て取得するメソッド
	public List<Event> joinedFilter() throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		// リストを定義
		List<Event> list = new ArrayList<>();

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM EVENTS WHERE EVENT_HOLD_STATE =1");

			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
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
			    event.setCategoryId(resultSet.getString("category_id"));
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

	// イベントIDからイベントを計算し、取得するメソッド
	public Event get(String event_id) throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// Eventクラスを定義
		Event event = new Event();

		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM EVENTS WHERE EVENT_ID =?");
			// プリペアードステートメントにユーザーIDをセット
			statement.setString(1, event_id);

			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
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
			    event.setCategoryId(resultSet.getString("category_id"));
			    event.setPhoneNumber(resultSet.getString("phone_number"));
			    event.setLink(resultSet.getString("link"));
			    event.setCredit(resultSet.getString("credit"));
			    event.setUserId(resultSet.getString("user_id"));
			    event.setTicketInfo(resultSet.getString("ticket_info"));
			    event.setEventHoldState(resultSet.getString("event_hold_state"));
			    event.setEventAddDate(resultSet.getDate("event_add_date") != null ? resultSet.getDate("event_add_date").toLocalDate() : null);
			    event.setTotalPayment(resultSet.getInt("TOTAL_PAYMENT"));
	} else {
		// リザルトセットが存在しない場合
		// インスタンスにnullをセット
		event = null;
	}} catch (Exception e) {
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
	return event;

}
	}
