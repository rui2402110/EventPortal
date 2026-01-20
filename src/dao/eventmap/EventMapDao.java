package dao.eventmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import dao.Dao;

/**
 * イベントとマップの関連を管理するDAOクラス
 * EVENT_MAPテーブルを使用
 */
public class EventMapDao extends Dao {

    /**
     * イベントにマップを関連付ける
     * 既存の関連があれば更新、なければ新規登録
     * @param eventId イベントID
     * @param mapId マップID
     * @return 実行件数
     * @throws Exception
     */
    public int attachMapToEvent(String eventId, int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            // 既存の関連があるかチェック
            if (hasMap(eventId)) {
                // 更新
                String sql = "UPDATE EVENT_MAP SET map_id = ?, updated_at = CURRENT_TIMESTAMP WHERE event_id = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, mapId);
                statement.setString(2, eventId);
            } else {
                // 新規登録
                String sql = "INSERT INTO EVENT_MAP (event_id, map_id) VALUES (?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, eventId);
                statement.setInt(2, mapId);
            }

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
     * イベントからマップの関連付けを解除
     * @param eventId イベントID
     * @return 削除件数
     * @throws Exception
     */
    public int detachMapFromEvent(String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "DELETE FROM EVENT_MAP WHERE event_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);

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
     * イベントに関連付けられたマップIDを取得
     * @param eventId イベントID
     * @return マップID（関連付けがない場合はnull）
     * @throws Exception
     */
    public Integer getMapIdByEventId(String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT map_id FROM EVENT_MAP WHERE event_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("map_id");
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
     * マップIDからそのマップを使用しているイベント数を取得
     * @param mapId マップID
     * @return 使用しているイベント数
     * @throws Exception
     */
    public int countEventsByMapId(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT COUNT(*) FROM EVENT_MAP WHERE map_id = ?";
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
     * イベントにマップが関連付けられているかチェック
     * @param eventId イベントID
     * @return マップが関連付けられている場合true
     * @throws Exception
     */
    public boolean hasMap(String eventId) throws Exception {
        Integer mapId = getMapIdByEventId(eventId);
        return mapId != null;
    }

    /**
     * 指定マップを使用している全イベントIDのリストを取得
     * @param mapId マップID
     * @return イベントIDのリスト
     * @throws Exception
     */
    public List<String> getEventIdsByMapId(int mapId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> eventIds = new ArrayList<>();

        try {
            String sql = "SELECT event_id FROM EVENT_MAP WHERE map_id = ? ORDER BY created_at DESC";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, mapId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                eventIds.add(resultSet.getString("event_id"));
            }

            return eventIds;

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
     * EVENT_MAPレコードを取得
     * @param eventId イベントID
     * @return EventMapオブジェクト（存在しない場合はnull）
     * @throws Exception
     */
    public EventMap findByEventId(String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM EVENT_MAP WHERE event_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return eventMapFromResultSet(resultSet);
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
     * 全EVENT_MAPレコードを取得
     * @return EventMapのリスト
     * @throws Exception
     */
    public List<EventMap> findAll() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EventMap> eventMaps = new ArrayList<>();

        try {
            String sql = "SELECT * FROM EVENT_MAP ORDER BY created_at DESC";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                eventMaps.add(eventMapFromResultSet(resultSet));
            }

            return eventMaps;

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
     * マップが削除可能かチェック（どのイベントでも使用されていないか）
     * @param mapId マップID
     * @return 削除可能な場合true
     * @throws Exception
     */
    public boolean canDeleteMap(int mapId) throws Exception {
        return countEventsByMapId(mapId) == 0;
    }

    /**
     * ResultSetからEventMapオブジェクトを生成
     * @param rs ResultSet
     * @return EventMapオブジェクト
     * @throws SQLException
     */
    private EventMap eventMapFromResultSet(ResultSet rs) throws SQLException {
        EventMap eventMap = new EventMap();
        eventMap.setEventId(rs.getString("event_id"));
        eventMap.setMapId(rs.getInt("map_id"));
        eventMap.setCreatedAt(rs.getTimestamp("created_at"));
        eventMap.setUpdatedAt(rs.getTimestamp("updated_at"));
        return eventMap;
    }

    /**
     * EVENT_MAPテーブルに対応するModelクラス
     */
    public static class EventMap {
        private String eventId;
        private Integer mapId;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        public EventMap() {
        }

        public EventMap(String eventId, Integer mapId, Timestamp createdAt, Timestamp updatedAt) {
            this.eventId = eventId;
            this.mapId = mapId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public Integer getMapId() {
            return mapId;
        }

        public void setMapId(Integer mapId) {
            this.mapId = mapId;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }

        public Timestamp getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
        }

        @Override
        public String toString() {
            return "EventMap{" +
                    "eventId='" + eventId + '\'' +
                    ", mapId=" + mapId +
                    ", createdAt=" + createdAt +
                    ", updatedAt=" + updatedAt +
                    '}';
        }
    }
}