package dao.eventmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import bean.Map;
import dao.Dao;

/**
 * MAPテーブルへのデータアクセスを行うDAOクラス
 */
public class MapDao extends Dao {

    /**
     * マップを新規登録
     * @param map 登録するマップ情報
     * @return 登録されたマップID（自動採番）
     * @throws Exception
     */
    public int insert(Map map) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            String sql = "INSERT INTO MAP (map_name, description, default_lat, default_lng, " +
                        "default_zoom, created_by) VALUES (?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, map.getMapName());
            statement.setString(2, map.getDescription());

            if (map.getDefaultLat() != null) {
                statement.setDouble(3, map.getDefaultLat());
            } else {
                statement.setNull(3, Types.DECIMAL);
            }

            if (map.getDefaultLng() != null) {
                statement.setDouble(4, map.getDefaultLng());
            } else {
                statement.setNull(4, Types.DECIMAL);
            }

            if (map.getDefaultZoom() != null) {
                statement.setInt(5, map.getDefaultZoom());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setString(6, map.getCreatedBy());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("マップの登録に失敗しました。");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("マップIDの取得に失敗しました。");
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
     * マップ情報を更新
     * @param map 更新するマップ情報
     * @return 更新件数
     * @throws Exception
     */
    public int update(Map map) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "UPDATE MAP SET map_name = ?, description = ?, default_lat = ?, " +
                        "default_lng = ?, default_zoom = ? WHERE map_id = ?";

            statement = connection.prepareStatement(sql);

            statement.setString(1, map.getMapName());
            statement.setString(2, map.getDescription());

            if (map.getDefaultLat() != null) {
                statement.setDouble(3, map.getDefaultLat());
            } else {
                statement.setNull(3, Types.DECIMAL);
            }

            if (map.getDefaultLng() != null) {
                statement.setDouble(4, map.getDefaultLng());
            } else {
                statement.setNull(4, Types.DECIMAL);
            }

            if (map.getDefaultZoom() != null) {
                statement.setInt(5, map.getDefaultZoom());
            } else {
                statement.setNull(5, Types.INTEGER);
            }

            statement.setInt(6, map.getMapId());

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
     * マップを削除
     * @param mapId 削除するマップID
     * @return 削除件数
     * @throws Exception
     */
    public int delete(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "DELETE FROM MAP WHERE map_id = ?";
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
     * マップIDで1件取得
     * @param mapId マップID
     * @return マップ情報（存在しない場合はnull）
     * @throws Exception
     */
    public Map findById(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM MAP WHERE map_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, mapId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapFromResultSet(resultSet);
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
     * 全マップを取得
     * @return マップのリスト
     * @throws Exception
     */
    public List<Map> findAll() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Map> maps = new ArrayList<>();

        try {
            String sql = "SELECT * FROM MAP ORDER BY created_at DESC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                maps.add(mapFromResultSet(resultSet));
            }

            return maps;

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
     * 作成者でマップを検索
     * @param createdBy 作成者
     * @return マップのリスト
     * @throws Exception
     */
    public List<Map> findByCreatedBy(String createdBy) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Map> maps = new ArrayList<>();

        try {
            String sql = "SELECT * FROM MAP WHERE created_by = ? ORDER BY created_at DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, createdBy);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                maps.add(mapFromResultSet(resultSet));
            }

            return maps;

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
     * マップ名で検索（部分一致）
     * @param mapName マップ名
     * @return マップのリスト
     * @throws Exception
     */
    public List<Map> findByMapName(String mapName) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Map> maps = new ArrayList<>();

        try {
            String sql = "SELECT * FROM MAP WHERE map_name LIKE ? ORDER BY created_at DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + mapName + "%");

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                maps.add(mapFromResultSet(resultSet));
            }

            return maps;

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
     * ResultSetからMapオブジェクトを生成
     * @param rs ResultSet
     * @return Mapオブジェクト
     * @throws SQLException
     */
    private Map mapFromResultSet(ResultSet rs) throws SQLException {
        Map map = new Map();
        map.setMapId(rs.getInt("map_id"));
        map.setMapName(rs.getString("map_name"));
        map.setDescription(rs.getString("description"));

        double lat = rs.getDouble("default_lat");
        if (!rs.wasNull()) {
            map.setDefaultLat(lat);
        }

        double lng = rs.getDouble("default_lng");
        if (!rs.wasNull()) {
            map.setDefaultLng(lng);
        }

        int zoom = rs.getInt("default_zoom");
        if (!rs.wasNull()) {
            map.setDefaultZoom(zoom);
        }

        map.setCreatedBy(rs.getString("created_by"));
        map.setCreatedAt(rs.getTimestamp("created_at"));
        map.setUpdatedAt(rs.getTimestamp("updated_at"));

        return map;
    }
}