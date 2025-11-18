package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Event;

public class HostEventDao extends Dao {
	// イベントを作成するメソッド
	public boolean eventCreate(Event event) throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		// Daoを定義
		EventDao eventDao = new EventDao();

		boolean result = false;
		try {
			Event existingEvent =eventDao.get(event.getEventId());
			if (existingEvent == null){
				// イベントが重複していない場合に処理を実行
				// SQL文をステートメントにセット
				statement = connection.prepareStatement("INSERT INTO EVENTS (" +
					    "event_id, event_name, event_overview, holding_date, holding_time, " +
					    "address, map_out_of_hall, map_in_hall, max_count, " +
					    "phone_number, link, credit, user_id, ticket_info, event_hold_state, " +
					    "event_add_date" +
					    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

					statement.setString(1, event.getEventId());
					statement.setString(2, event.getEventName());
					statement.setString(3, event.getEventOverview());
					statement.setObject(4, event.getHoldingDate());
					statement.setObject(5, event.getHoldingTime());
					statement.setString(6, event.getAddress());
					statement.setString(7, event.getMapOutOfHall());
					statement.setString(8, event.getMapInHall());
					statement.setInt(9, event.getMaxCount());
					statement.setString(10, event.getPhoneNumber());
					statement.setString(11, event.getLink());
					statement.setString(12, event.getCredit());
					statement.setString(13, event.getUserId());
					statement.setString(14, event.getTicketInfo());
					statement.setString(15, event.getEventHoldState());
					statement.setObject(16, event.getEventAddDate());

		        int affected = statement.executeUpdate();
		        result = (affected > 0);

			} else {
				return false ;
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
		return result;
	}


	// イベントIDを新規に取得するメソッド
	public String eventIdGet() throws Exception {
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT event_id FROM events ORDER BY event_id DESC LIMIT 1");
			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();

			// イベントidを結果から取得、イベントidが存在しない場合は"EVT001"をリターン
			if (resultSet.next()){
				String lastEventId = resultSet.getString("event_id");
                return incrementEventId(lastEventId);
			} else {
				return "EVT001" ;
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

	// イベントIDを作成するメソッド
	private static String incrementEventId(String currentId) {
	    final String prefix = "EVT";
	    final int idLen = 3;
        // プレフィックスを除いた数値部分を取得
        String numberPart = currentId.substring(prefix.length());
        int number = Integer.parseInt(numberPart);

        // 1を加算
        number++;

        // 新しいIDを生成
        return prefix + String.format("%0" + idLen + "d", number);
    }


}
