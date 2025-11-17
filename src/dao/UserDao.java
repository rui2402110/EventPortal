package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.User;

public class UserDao extends Dao {
	public User get(String id,int user_type) throws Exception {
		// Userクラスを再定義
		User user = new User();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;
		try {
			// プリペアードステートメントにSQL文をセット
			statement = connection.prepareStatement("SELECT * FROM USERS WHERE user_id=? AND user_type=? ");
			// プリペアードステートメントにユーザーIDをセット
			statement.setString(1, id);
			statement.setInt(2, user_type);
			// SQL文の実行
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				// SELECTしたデータをインスタンスにセット
				user.setUser_id(resultSet.getString("user_id"));
				user.setPassword(resultSet.getString("password"));
				user.setPhone_number(resultSet.getString("phone_number"));
				user.setUser_name(resultSet.getString("user_name"));
				user.setUser_type(resultSet.getInt("user_type"));
				user.setAuth(resultSet.getBoolean("isAuth"));
			} else {
				// リザルトセットが存在しない場合
				// インスタンスにnullをセット
				user = null;
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
		return user;

	}
	public User login(String id, String password, int user_type) throws Exception {
		// ユーザーインスタンスにgetメソッドで取得したデータを入れる
		User user = get(id,user_type);
		// userがnullまたはパスワードが一致しない場合
			if (user == null || !user.getPassword().equals(password)) {
				return null;
			}
		return user;
	}

	public Boolean signin(String id, String user_name, String mail_address,
            String password, String phone_number, int user_type) throws Exception {
Connection connection = getConnection();
PreparedStatement statement = null;
boolean result = false;

try {
User user = get(id, user_type);

// ユーザーが存在しない場合は新規登録
if (user == null) {
  statement = connection.prepareStatement(
      "INSERT INTO USERS (USER_ID, USER_NAME, MAIL_ADDRESS, PASSWORD, PHONE_NUMBER, USER_TYPE, isAuth) VALUES(?,?,?,?,?,?,true)");

  // 引数から直接値をセット(userオブジェクトではなく)
  statement.setString(1, id);
  statement.setString(2, user_name);
  statement.setString(3, mail_address);
  statement.setString(4, password);
  statement.setString(5, phone_number);
  statement.setInt(6, user_type);

  int affected = statement.executeUpdate();
  result = (affected > 0);
} else {
  // ユーザーが既に存在する場合
  result = false; // または適切な処理
}
} catch (Exception e) {
throw e;
} finally {
if (statement != null) {
  try {
      statement.close();
  } catch (SQLException sqle) {
      throw sqle;
  }
}
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


