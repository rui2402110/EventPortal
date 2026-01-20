package dao.eventmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import bean.MapPin;
import dao.Dao;

/**
 * MAP_PINテーブルへのデータアクセスを行うDAOクラス
 */
public class MapPinDao extends Dao {

    /**
     * ピンを新規登録
     * @param pin 登録するピン情報
     * @return 登録されたピンID（自動採番）
     * @throws Exception
     */
    public int insert(MapPin pin) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            String sql = "INSERT INTO MAP_PIN (map_id, pin_name, latitude, longitude, address, " +
                        "description, pin_color, icon_type, display_order) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, pin.getMapId());
            statement.setString(2, pin.getPinName());
            statement.setDouble(3, pin.getLatitude());
            statement.setDouble(4, pin.getLongitude());
            statement.setString(5, pin.getAddress());
            statement.setString(6, pin.getDescription());
            statement.setString(7, pin.getPinColor());
            statement.setString(8, pin.getIconType());

            if (pin.getDisplayOrder() != null) {
                statement.setInt(9, pin.getDisplayOrder());
            } else {
                statement.setNull(9, Types.INTEGER);
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("ピンの登録に失敗しました。");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("ピンIDの取得に失敗しました。");
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
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
    }

    /**
     * ピン情報を更新
     * @param pin 更新するピン情報
     * @return 更新件数
     * @throws Exception
     */
    public int update(MapPin pin) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "UPDATE MAP_PIN SET pin_name = ?, latitude = ?, longitude = ?, " +
                        "address = ?, description = ?, pin_color = ?, icon_type = ?, " +
                        "display_order = ? WHERE pin_id = ?";

            statement = connection.prepareStatement(sql);

            statement.setString(1, pin.getPinName());
            statement.setDouble(2, pin.getLatitude());
            statement.setDouble(3, pin.getLongitude());
            statement.setString(4, pin.getAddress());
            statement.setString(5, pin.getDescription());
            statement.setString(6, pin.getPinColor());
            statement.setString(7, pin.getIconType());

            if (pin.getDisplayOrder() != null) {
                statement.setInt(8, pin.getDisplayOrder());
            } else {
                statement.setNull(8, Types.INTEGER);
            }

            statement.setInt(9, pin.getPinId());

            return statement.executeUpdate();

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
    }

    /**
     * ピンを削除
     * @param pinId 削除するピンID
     * @return 削除件数
     * @throws Exception
     */
    public int delete(int pinId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "DELETE FROM MAP_PIN WHERE pin_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, pinId);

            return statement.executeUpdate();

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
    }

    /**
     * 指定マップの全ピンを削除
     * @param mapId マップID
     * @return 削除件数
     * @throws Exception
     */
    public int deleteByMapId(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "DELETE FROM MAP_PIN WHERE map_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, mapId);

            return statement.executeUpdate();

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
    }

    /**
     * ピンIDで1件取得
     * @param pinId ピンID
     * @return ピン情報（存在しない場合はnull）
     * @throws Exception
     */
    public MapPin findById(int pinId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM MAP_PIN WHERE pin_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, pinId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return pinFromResultSet(resultSet);
            }
            return null;

        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
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
    }

    /**
     * 指定マップの全ピンを取得
     * @param mapId マップID
     * @return ピンのリスト
     * @throws Exception
     */
    public List<MapPin> findByMapId(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<MapPin> pins = new ArrayList<>();

        try {
            String sql = "SELECT * FROM MAP_PIN WHERE map_id = ? ORDER BY display_order, created_at";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, mapId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                pins.add(pinFromResultSet(resultSet));
            }

            return pins;

        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
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
    }

    /**
     * ピン名で検索（部分一致）
     * @param mapId マップID
     * @param pinName ピン名
     * @return ピンのリスト
     * @throws Exception
     */
    public List<MapPin> findByPinName(int mapId, String pinName) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<MapPin> pins = new ArrayList<>();

        try {
            String sql = "SELECT * FROM MAP_PIN WHERE map_id = ? AND pin_name LIKE ? " +
                        "ORDER BY display_order, created_at";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, mapId);
            statement.setString(2, "%" + pinName + "%");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                pins.add(pinFromResultSet(resultSet));
            }

            return pins;

        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
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
    }

    /**
     * 指定マップのピン数を取得
     * @param mapId マップID
     * @return ピン数
     * @throws Exception
     */
    public int countByMapId(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT COUNT(*) FROM MAP_PIN WHERE map_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, mapId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;

        } catch (Exception e) {
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
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
    }

    /**
     * 複数のピンを一括登録
     * @param pins 登録するピンのリスト
     * @throws Exception
     */
    public void insertBatch(List<MapPin> pins) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "INSERT INTO MAP_PIN (map_id, pin_name, latitude, longitude, address, " +
                        "description, pin_color, icon_type, display_order) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);

            for (MapPin pin : pins) {
                statement.setInt(1, pin.getMapId());
                statement.setString(2, pin.getPinName());
                statement.setDouble(3, pin.getLatitude());
                statement.setDouble(4, pin.getLongitude());
                statement.setString(5, pin.getAddress());
                statement.setString(6, pin.getDescription());
                statement.setString(7, pin.getPinColor());
                statement.setString(8, pin.getIconType());

                if (pin.getDisplayOrder() != null) {
                    statement.setInt(9, pin.getDisplayOrder());
                } else {
                    statement.setNull(9, Types.INTEGER);
                }

                statement.addBatch();
            }

            statement.executeBatch();

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
    }

    /**
     * 表示順序を更新
     * @param pinId ピンID
     * @param displayOrder 表示順序
     * @return 更新件数
     * @throws Exception
     */
    public int updateDisplayOrder(int pinId, int displayOrder) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "UPDATE MAP_PIN SET display_order = ? WHERE pin_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, displayOrder);
            statement.setInt(2, pinId);

            return statement.executeUpdate();

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
    }

    /**
     * ResultSetからMapPinオブジェクトを生成
     * @param rs ResultSet
     * @return MapPinオブジェクト
     * @throws SQLException
     */
    private MapPin pinFromResultSet(ResultSet rs) throws SQLException {
        MapPin pin = new MapPin();
        pin.setPinId(rs.getInt("pin_id"));
        pin.setMapId(rs.getInt("map_id"));
        pin.setPinName(rs.getString("pin_name"));
        pin.setLatitude(rs.getDouble("latitude"));
        pin.setLongitude(rs.getDouble("longitude"));
        pin.setAddress(rs.getString("address"));
        pin.setDescription(rs.getString("description"));
        pin.setPinColor(rs.getString("pin_color"));
        pin.setIconType(rs.getString("icon_type"));

        int displayOrder = rs.getInt("display_order");
        if (!rs.wasNull()) {
            pin.setDisplayOrder(displayOrder);
        }

        pin.setCreatedAt(rs.getTimestamp("created_at"));
        pin.setUpdatedAt(rs.getTimestamp("updated_at"));

        return pin;
    }
}