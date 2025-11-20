package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

public class EntryEventDao extends Dao {

    /**
     * イベント参加情報を登録
     * @param session HTTPセッション
     * @param eventId イベントID
     * @return 登録件数
     * @throws Exception
     */
    public int insertEntry(HttpSession session, String eventId) throws Exception {

        // セッションからユーザーIDを取得
        String userId = (String) session.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            throw new IllegalStateException("ユーザーIDがセッションに存在しません");
        }

        // コネクションを確立
        Connection connection = getConnection();
        // プリペアードステートメント
        PreparedStatement statement = null;
        // 登録結果
        int result = 0;

        try {
            // プリペアードステートメントにSQL文をセット
            statement = connection.prepareStatement(
                "INSERT INTO ENTRY_EVENT (EVENT_ID, USER_ID, ENTRY_DATE) VALUES (?, ?, CURRENT_TIMESTAMP)"
            );
            // プリペアードステートメントにイベントIDをセット
            statement.setString(1, eventId);
            // プリペアードステートメントにユーザーIDをセット
            statement.setString(2, userId);

            // SQL文の実行
            result = statement.executeUpdate();

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

    /**
     * イベント参加情報を登録（オーバーロード版）
     * @param userId ユーザーID
     * @param eventId イベントID
     * @return 登録件数
     * @throws Exception
     */
    public int insertEntry(String userId, String eventId) throws Exception {

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("ユーザーIDが不正です");
        }
        if (eventId == null || eventId.isEmpty()) {
            throw new IllegalArgumentException("イベントIDが不正です");
        }

        // コネクションを確立
        Connection connection = getConnection();
        // プリペアードステートメント
        PreparedStatement statement = null;
        // 登録結果
        int result = 0;

        try {
            // プリペアードステートメントにSQL文をセット
            statement = connection.prepareStatement(
                "INSERT INTO ENTRY_EVENT (EVENT_ID, USER_ID, ENTRY_DATE) VALUES (?, ?, CURRENT_TIMESTAMP)"
            );
            // プリペアードステートメントにイベントIDをセット
            statement.setString(1, eventId);
            // プリペアードステートメントにユーザーIDをセット
            statement.setString(2, userId);

            // SQL文の実行
            result = statement.executeUpdate();

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