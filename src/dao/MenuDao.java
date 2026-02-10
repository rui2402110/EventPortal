package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Menu;

/**
 * メニューDAO
 */
public class MenuDao extends Dao {

    /**
     * イベントIDでメニュー一覧を取得
     */
    public List<Menu> getByEventId(String eventId) throws Exception {
        List<Menu> menuList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM MENUS WHERE event_id = ? ORDER BY menu_type, menu_id";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Menu menu = mapResultSetToMenu(resultSet);
                menuList.add(menu);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return menuList;
    }

    /**
     * メニューIDでメニューを取得
     */
    public Menu get(String menuId) throws Exception {
        Menu menu = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM MENUS WHERE menu_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, menuId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                menu = mapResultSetToMenu(resultSet);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return menu;
    }

    /**
     * メニューを登録
     */
    public int save(Menu menu) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "INSERT INTO MENUS (menu_id, event_id, menu_name, menu_type, " +
                         "price, description, stock_quantity, image_path) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, menu.getMenuId());
            statement.setString(2, menu.getEventId());
            statement.setString(3, menu.getMenuName());
            statement.setString(4, menu.getMenuType());
            statement.setInt(5, menu.getPrice());
            statement.setString(6, menu.getDescription());
            statement.setInt(7, menu.getStockQuantity());
            statement.setString(8, menu.getImagePath());

            count = statement.executeUpdate();

            System.out.println("✓ メニュー登録成功: " + menu.getMenuId() + " - " + menu.getMenuName());

        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    /**
     * メニューを更新
     */
    public int update(Menu menu) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "UPDATE MENUS SET menu_name = ?, menu_type = ?, price = ?, " +
                         "description = ?, stock_quantity = ?, image_path = ? " +
                         "WHERE menu_id = ?";

            statement = connection.prepareStatement(sql);
            statement.setString(1, menu.getMenuName());
            statement.setString(2, menu.getMenuType());
            statement.setInt(3, menu.getPrice());
            statement.setString(4, menu.getDescription());
            statement.setInt(5, menu.getStockQuantity());
            statement.setString(6, menu.getImagePath());
            statement.setString(7, menu.getMenuId());

            count = statement.executeUpdate();

        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    /**
     * メニューを削除
     */
    public int delete(String menuId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "DELETE FROM MENUS WHERE menu_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, menuId);
            count = statement.executeUpdate();

        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    /**
     * ResultSetからMenuオブジェクトにマッピング
     */
    private Menu mapResultSetToMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setMenuId(rs.getString("menu_id"));
        menu.setEventId(rs.getString("event_id"));
        menu.setMenuName(rs.getString("menu_name"));
        menu.setMenuType(rs.getString("menu_type"));
        menu.setPrice(rs.getInt("price"));
        menu.setDescription(rs.getString("description"));
        menu.setStockQuantity(rs.getInt("stock_quantity"));
        menu.setImagePath(rs.getString("image_path"));

        try {
            menu.setCreatedAt(rs.getString("created_at"));
        } catch (SQLException e) {
            menu.setCreatedAt(null);
        }

        return menu;
    }
}