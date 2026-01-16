package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Event;

public class EntryEventDao extends Dao {

	public boolean join(String userId, String eventId) throws Exception {
	    Connection connection = getConnection();
	    PreparedStatement stSelect = null;
	    PreparedStatement stUpdate = null;
	    PreparedStatement stInsert = null;
	    boolean result = false;

	    try {
	        // 1. 既にデータが存在するか確認（キャンセル済み：status='9' のデータがあるか）
	        stSelect = connection.prepareStatement(
	            "SELECT status FROM EVENT_ENTRYS WHERE user_id = ? AND event_id = ?");
	        stSelect.setString(1, userId);
	        stSelect.setString(2, eventId);
	        ResultSet rs = stSelect.executeQuery();

	        if (rs.next()) {
	            // データが存在する場合
	            String status = rs.getString("status");
	            if ("9".equals(status)) {
	                // ステータスが'9'なら'2'に更新する
	                stUpdate = connection.prepareStatement(
	                    "UPDATE EVENT_ENTRYS SET status = '2' WHERE user_id = ? AND event_id = ?");
	                stUpdate.setString(1, userId);
	                stUpdate.setString(2, eventId);
	                int affected = stUpdate.executeUpdate();
	                result = (affected > 0);
	            } else {
	                // すでに'2'（参加中）などの場合は、何もしない（または二重登録防止）
	                result = false;
	            }
	        } else {
	            // 2. データが存在しない場合は新規登録
	            stInsert = connection.prepareStatement(
	                "INSERT INTO EVENT_ENTRYS (event_id, user_id, status) VALUES (?, ?, '2')");
	            stInsert.setString(1, eventId);
	            stInsert.setString(2, userId);
	            int affected = stInsert.executeUpdate();
	            result = (affected > 0);
	        }

	    } catch (Exception e) {
	        throw e;
	    } finally {
	        // リソースの解放（各ステートメントを閉じる）
	        if (stSelect != null) stSelect.close();
	        if (stUpdate != null) stUpdate.close();
	        if (stInsert != null) stInsert.close();
	        if (connection != null) connection.close();
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
			statement = connection.prepareStatement("SELECT e.* , ee.status FROM EVENTS e JOIN EVENT_ENTRYS ee ON e.event_id = ee.event_id WHERE ee.user_id = ? AND ee.status <> 9;");
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
	// イベントIDとユーザーIDを受け取り、イベントの参加をキャンセルするメソッド
	public boolean eventCanncel(String eventId, String userId) throws Exception {
	    Connection connection = getConnection();
	    PreparedStatement statement = null;
	    // 更新に成功したかどうかを保持するフラグ
	    boolean isSuccess = false;

	    try {
	    	// エントリーを論理削除（statusを9に更新）
	        statement = connection.prepareStatement("UPDATE EVENT_ENTRYS SET status = '9' WHERE user_id = ? AND event_id = ?;");

	        // プレースホルダに値をセット
	        statement.setString(1, userId);
	        statement.setString(2, eventId);

	        // SQLを実行し、更新された行数を取得
	        int updatedRows = statement.executeUpdate();

	        // 1行以上更新されていれば成功とみなす
	        if (updatedRows > 0) {
	            isSuccess = true;
	        }

	    }catch (Exception e) {
	        // 呼び出し元へ例外を投げる
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

	    return isSuccess;
	}

}