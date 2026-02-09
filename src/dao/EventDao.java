package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Event;

/**
 * イベントDAO（完全版・全カラム対応）
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
     * イベントを登録（必須カラムのみ・シンプル版）
     * @param event イベント情報
     * @return 登録件数
     * @throws Exception
     */
    public int save(Event event) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            // 必須カラムのみ指定（NULLを許容するカラムは省略）
            String sql = "INSERT INTO EVENTS (" +
                         "event_id, event_name, event_overview, holding_date, holding_time, " +
                         "address, max_count, event_hold_state, host_id" +
                         ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, event.getEventId());
            statement.setString(2, event.getEventName());
            statement.setString(3, event.getEventOverview());
            statement.setString(4, event.getHoldingDate());
            statement.setString(5, event.getHoldingTime());
            statement.setString(6, event.getAddress());
            statement.setInt(7, event.getMaxCount());
            statement.setString(8, event.getEventHoldState() != null ? event.getEventHoldState() : "1");
            statement.setString(9, event.getHostId());

            count = statement.executeUpdate();

            System.out.println("✓ イベント登録成功: " + event.getEventId() + " - " + event.getEventName());

        } catch (Exception e) {
            System.err.println("✗ イベント登録エラー: " + e.getMessage());
            System.err.println("SQL実行時エラー詳細:");
            e.printStackTrace();
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
                         "link = ?, event_overview = ?, category_id = ?, map_in_hall = ?, " +
                         "map_out_of_hall = ?, ticket_info = ? WHERE event_id = ?";
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
            statement.setString(10, event.getCategoryId());
            statement.setString(11, event.getMapInHall());
            statement.setString(12, event.getMapOutOfHall());
            statement.setString(13, event.getTicketInfo());
            statement.setString(14, event.getEventId());
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

        // 必須フィールド（確実に存在する）
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

        // オプションフィールド（存在しない場合はスキップ）
        event.setHostId(getStringOrNull(rs, "host_id"));
        event.setHostName(getStringOrNull(rs, "host_name"));
        event.setCategoryId(getStringOrNull(rs, "category_id"));
        event.setCredit(getStringOrNull(rs, "credit"));
        event.setEventAddDate(getStringOrNull(rs, "event_add_date"));
        event.setMapInHall(getStringOrNull(rs, "map_in_hall"));
        event.setMapOutOfHall(getStringOrNull(rs, "map_out_of_hall"));
        event.setTicketInfo(getStringOrNull(rs, "ticket_info"));
        event.setUserId(getStringOrNull(rs, "user_id"));

        // INT型のオプションフィールド
        try {
            event.setTotalPayment(rs.getInt("total_payment"));
        } catch (SQLException e) {
            event.setTotalPayment(0);
        }

        return event;
    }

    /**
     * ResultSetから安全に文字列を取得
     * @param rs ResultSet
     * @param columnName カラム名
     * @return 値（カラムが存在しない場合はnull）
     */
    private String getStringOrNull(ResultSet rs, String columnName) {
        try {
            return rs.getString(columnName);
        } catch (SQLException e) {
            return null;
        }
    }
}