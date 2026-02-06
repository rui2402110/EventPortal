package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Event;

/**
 * イベントDAO
 */
public class EventDao extends Dao {

    /**
     * イベントIDでイベントを取得
     * @param eventId イベントID
     * @return イベント情報（見つからない場合はnull）
     * @throws Exception
     */
    public Event get(String eventId) throws Exception {
        Event event = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM EVENTS WHERE event_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                event = mapResultSetToEvent(resultSet);
            }
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

        return event;
    }

    /**
     * 全イベントを取得
     * @return イベントリスト
     * @throws Exception
     */
    public List<Event> getAll() throws Exception {
        return filter(null);
    }

    /**
     * イベントをフィルタ条件で取得
     * @param school 学校（nullの場合は全件取得）
     * @return イベントリスト
     * @throws Exception
     */
    public List<Event> filter(String school) throws Exception {
        List<Event> eventList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql;
            if (school == null) {
                // 全件取得
                sql = "SELECT * FROM EVENTS ORDER BY holding_date DESC, event_id";
                statement = connection.prepareStatement(sql);
            } else {
                // 学校でフィルタ
                sql = "SELECT * FROM EVENTS WHERE school = ? ORDER BY holding_date DESC, event_id";
                statement = connection.prepareStatement(sql);
                statement.setString(1, school);
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Event event = mapResultSetToEvent(resultSet);
                eventList.add(event);
            }
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

        return eventList;
    }

    /**
     * 主催者IDでイベントを取得
     * @param hostId 主催者ID
     * @return イベントリスト
     * @throws Exception
     */
    public List<Event> getByHostId(String hostId) throws Exception {
        List<Event> eventList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM EVENTS WHERE host_id = ? ORDER BY holding_date DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, hostId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Event event = mapResultSetToEvent(resultSet);
                eventList.add(event);
            }
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

        return eventList;
    }

    /**
     * イベントを登録
     * @param event イベント情報
     * @return 登録件数
     * @throws Exception
     */
    public int save(Event event) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "INSERT INTO EVENTS (event_id, event_name, holding_date, holding_time, " +
                         "address, max_count, event_hold_state, phone_number, link, event_overview, host_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, event.getEventId());
            statement.setString(2, event.getEventName());
            statement.setString(3, event.getHoldingDate());
            statement.setString(4, event.getHoldingTime());
            statement.setString(5, event.getAddress());
            statement.setInt(6, event.getMaxCount());
            statement.setString(7, event.getEventHoldState());
            statement.setString(8, event.getPhoneNumber());
            statement.setString(9, event.getLink());
            statement.setString(10, event.getEventOverview());
            statement.setString(11, event.getHostId());
            count = statement.executeUpdate();
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

        return count;
    }

    /**
     * イベント情報を更新
     * @param event イベント情報
     * @return 更新件数
     * @throws Exception
     */
    public int update(Event event) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "UPDATE EVENTS SET event_name = ?, holding_date = ?, holding_time = ?, " +
                         "address = ?, max_count = ?, event_hold_state = ?, phone_number = ?, " +
                         "link = ?, event_overview = ? WHERE event_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, event.getEventName());
            statement.setString(2, event.getHoldingDate());
            statement.setString(3, event.getHoldingTime());
            statement.setString(4, event.getAddress());
            statement.setInt(5, event.getMaxCount());
            statement.setString(6, event.getEventHoldState());
            statement.setString(7, event.getPhoneNumber());
            statement.setString(8, event.getLink());
            statement.setString(9, event.getEventOverview());
            statement.setString(10, event.getEventId());
            count = statement.executeUpdate();
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

        return count;
    }

    /**
     * イベントを削除
     * @param eventId イベントID
     * @return 削除件数
     * @throws Exception
     */
    public int delete(String eventId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "DELETE FROM EVENTS WHERE event_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);
            count = statement.executeUpdate();
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

        return count;
    }

    /**
     * イベント件数を取得
     * @return イベント件数
     * @throws Exception
     */
    public int count() throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int count = 0;

        try {
            String sql = "SELECT COUNT(*) as cnt FROM EVENTS";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("cnt");
            }
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

        return count;
    }

    /**
     * イベント状態で検索
     * @param eventHoldState イベント開催状態（1:開催前, 2:開催中, 3:開催後）
     * @return イベントリスト
     * @throws Exception
     */
    public List<Event> getByState(String eventHoldState) throws Exception {
        List<Event> eventList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM EVENTS WHERE event_hold_state = ? ORDER BY holding_date DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventHoldState);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Event event = mapResultSetToEvent(resultSet);
                eventList.add(event);
            }
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

        return eventList;
    }

    /**
     * イベント名で検索（部分一致）
     * @param keyword キーワード
     * @return イベントリスト
     * @throws Exception
     */
    public List<Event> searchByName(String keyword) throws Exception {
        List<Event> eventList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM EVENTS WHERE event_name LIKE ? ORDER BY holding_date DESC";
            statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Event event = mapResultSetToEvent(resultSet);
                eventList.add(event);
            }
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

        return eventList;
    }

    /**
     * ResultSetからEventオブジェクトにマッピング
     * @param rs ResultSet
     * @return Eventオブジェクト
     * @throws SQLException
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getString("event_id"));
        event.setEventName(rs.getString("event_name"));
        event.setHoldingDate(rs.getString("holding_date"));
        event.setHoldingTime(rs.getString("holding_time"));
        event.setAddress(rs.getString("address"));
        event.setMaxCount(rs.getInt("max_count"));
        event.setEventHoldState(rs.getString("event_hold_state"));
        event.setPhoneNumber(rs.getString("phone_number"));
        event.setLink(rs.getString("link"));
        event.setEventOverview(rs.getString("event_overview"));
        event.setHostId(rs.getString("host_id"));

        // host_nameカラムがあれば取得（オプション）
        try {
            event.setHostName(rs.getString("host_name"));
        } catch (SQLException e) {
            // カラムがない場合は無視
        }

        return event;
    }
}