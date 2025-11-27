package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.User;

/**
 * UserDao拡張クラス
 */
public class UserDaoEx extends UserDao {

    /**
     * ユーザーIDからユーザー情報を取得
     */
    public User get(String userId) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        User user = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT user_id, user_name, mail_address, password, " +
                "phone_number, user_type, isAuth " +
                "FROM USERS WHERE user_id = ?"
            );
            stmt.setString(1, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUser_id(rs.getString("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setMail_address(rs.getString("mail_address"));
                user.setPassword(rs.getString("password"));
                user.setPhone_number(rs.getString("phone_number"));
                user.setUser_type(rs.getInt("user_type"));
                user.setAuth(rs.getBoolean("isAuth"));
            }
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return user;
    }

    /**
     * ユーザータイプで絞り込んだユーザー一覧を取得
     */
    public List<User> getByType(int userType) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<User> users = new ArrayList<>();

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(
                "SELECT user_id, user_name, mail_address, password, " +
                "phone_number, user_type, isAuth " +
                "FROM USERS WHERE user_type = ? ORDER BY user_id"
            );
            stmt.setInt(1, userType);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getString("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setMail_address(rs.getString("mail_address"));
                user.setPassword(rs.getString("password"));
                user.setPhone_number(rs.getString("phone_number"));
                user.setUser_type(rs.getInt("user_type"));
                user.setAuth(rs.getBoolean("isAuth"));
                users.add(user);
            }

            System.out.println("ユーザータイプ " + userType + " のユーザー取得: " + users.size() + "件");

        } catch (Exception e) {
            System.err.println("ユーザータイプ別取得エラー: " + e.getMessage());
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return users;
    }

    /**
     * 全参加者を取得
     */
    public List<User> getAllEntryUsers() throws Exception {
        return getByType(1);
    }

    /**
     * 全主催者を取得
     */
    public List<User> getAllHostUsers() throws Exception {
        return getByType(2);
    }
}