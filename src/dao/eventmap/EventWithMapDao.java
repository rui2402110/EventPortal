package dao.eventmap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Event;
import bean.Map;
import dao.Dao;

/**
 * イベントとマップを結合して取得するDAOクラス
 * EVENT、EVENT_MAP、MAPテーブルをJOINして取得
 */
public class EventWithMapDao extends Dao {

    /**
     * イベントIDでイベント情報とマップ情報を取得
     * @param eventId イベントID
     * @return EventWithMapオブジェクト（存在しない場合はnull）
     * @throws Exception
     */
    public EventWithMap findEventWithMapById(String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT e.*, em.map_id, m.map_name, m.description as map_description, " +
                        "m.default_lat, m.default_lng, m.default_zoom " +
                        "FROM EVENT e " +
                        "LEFT JOIN EVENT_MAP em ON e.event_id = em.event_id " +
                        "LEFT JOIN MAP m ON em.map_id = m.map_id " +
                        "WHERE e.event_id = ?";

            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return eventWithMapFromResultSet(resultSet);
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
     * 全イベントをマップ情報付きで取得
     * @return EventWithMapのリスト
     * @throws Exception
     */
    public List<EventWithMap> findAllEventsWithMap() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EventWithMap> events = new ArrayList<>();

        try {
            String sql = "SELECT e.*, em.map_id, m.map_name, m.description as map_description, " +
                        "m.default_lat, m.default_lng, m.default_zoom " +
                        "FROM EVENT e " +
                        "LEFT JOIN EVENT_MAP em ON e.event_id = em.event_id " +
                        "LEFT JOIN MAP m ON em.map_id = m.map_id " +
                        "ORDER BY e.event_add_date DESC";

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                events.add(eventWithMapFromResultSet(resultSet));
            }

            return events;

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
     * ユーザーIDで作成したイベントをマップ情報付きで取得
     * @param userId ユーザーID
     * @return EventWithMapのリスト
     * @throws Exception
     */
    public List<EventWithMap> findEventsByUserIdWithMap(String userId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EventWithMap> events = new ArrayList<>();

        try {
            String sql = "SELECT e.*, em.map_id, m.map_name, m.description as map_description, " +
                        "m.default_lat, m.default_lng, m.default_zoom " +
                        "FROM EVENT e " +
                        "LEFT JOIN EVENT_MAP em ON e.event_id = em.event_id " +
                        "LEFT JOIN MAP m ON em.map_id = m.map_id " +
                        "WHERE e.user_id = ? " +
                        "ORDER BY e.event_add_date DESC";

            statement = connection.prepareStatement(sql);
            statement.setString(1, userId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                events.add(eventWithMapFromResultSet(resultSet));
            }

            return events;

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
     * マップが設定されているイベントのみを取得
     * @return EventWithMapのリスト
     * @throws Exception
     */
    public List<EventWithMap> findEventsWithMapOnly() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EventWithMap> events = new ArrayList<>();

        try {
            String sql = "SELECT e.*, em.map_id, m.map_name, m.description as map_description, " +
                        "m.default_lat, m.default_lng, m.default_zoom " +
                        "FROM EVENT e " +
                        "INNER JOIN EVENT_MAP em ON e.event_id = em.event_id " +
                        "INNER JOIN MAP m ON em.map_id = m.map_id " +
                        "ORDER BY e.event_add_date DESC";

            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                events.add(eventWithMapFromResultSet(resultSet));
            }

            return events;

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
     * 開催日が近いイベントをマップ情報付きで取得
     * @param limit 取得件数
     * @return EventWithMapのリスト
     * @throws Exception
     */
    public List<EventWithMap> findUpcomingEventsWithMap(int limit) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<EventWithMap> events = new ArrayList<>();

        try {
            String sql = "SELECT e.*, em.map_id, m.map_name, m.description as map_description, " +
                        "m.default_lat, m.default_lng, m.default_zoom " +
                        "FROM EVENT e " +
                        "LEFT JOIN EVENT_MAP em ON e.event_id = em.event_id " +
                        "LEFT JOIN MAP m ON em.map_id = m.map_id " +
                        "WHERE e.holding_date >= CURDATE() " +
                        "ORDER BY e.holding_date ASC, e.holding_time ASC " +
                        "LIMIT ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, limit);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                events.add(eventWithMapFromResultSet(resultSet));
            }

            return events;

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
     * ResultSetからEventWithMapオブジェクトを生成
     * @param rs ResultSet
     * @return EventWithMapオブジェクト
     * @throws SQLException
     */
    private EventWithMap eventWithMapFromResultSet(ResultSet rs) throws SQLException {
        EventWithMap eventWithMap = new EventWithMap();

        // Eventの情報をセット
        Event event = new Event();
        event.setEventId(rs.getString("event_id"));
        event.setEventName(rs.getString("event_name"));
        event.setEventOverview(rs.getString("event_overview"));
        event.setAddress(rs.getString("address"));
        event.setUserId(rs.getString("user_id"));
        event.setHoldingDate(rs.getDate("holding_date").toLocalDate());
        event.setHoldingTime(rs.getTime("holding_time").toLocalTime());
        event.setMaxCount(rs.getInt("max_count"));
        event.setPhoneNumber(rs.getString("phone_number"));
        event.setLink(rs.getString("link"));
        event.setCredit(rs.getString("credit"));
        event.setEventHoldState(rs.getString("event_hold_state"));
        event.setEventAddDate(rs.getDate("event_add_date").toLocalDate());
        event.setTicketInfo(rs.getString("ticket_info"));

        eventWithMap.setEvent(event);

        // Mapの情報をセット（マップが関連付けられている場合のみ）
        int mapId = rs.getInt("map_id");
        if (!rs.wasNull()) {
            Map map = new Map();
            map.setMapId(mapId);
            map.setMapName(rs.getString("map_name"));
            map.setDescription(rs.getString("map_description"));

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

            eventWithMap.setMap(map);
        }

        return eventWithMap;
    }

    /**
     * EventとMapを保持する内部クラス
     */
    public static class EventWithMap {
        private Event event;
        private Map map;

        public EventWithMap() {
        }

        public EventWithMap(Event event, Map map) {
            this.event = event;
            this.map = map;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public Map getMap() {
            return map;
        }

        public void setMap(Map map) {
            this.map = map;
        }

        public boolean hasMap() {
            return map != null;
        }

        @Override
        public String toString() {
            return "EventWithMap{" +
                    "event=" + event +
                    ", map=" + map +
                    '}';
        }
    }
}