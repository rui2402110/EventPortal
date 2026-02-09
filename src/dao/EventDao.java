package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import bean.Event;

/**
 * イベントDAO（TIME/DATE型対応完全版）
 */
public class EventDao extends Dao {

    /**
     * イベントIDでイベントを取得
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
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return event;
    }

    /**
     * 全イベントを取得
     */
    public List<Event> getAll() throws Exception {
        return filter(null);
    }

    /**
     * イベントをフィルタ条件で取得
     */
    public List<Event> filter(String school) throws Exception {
        List<Event> eventList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql;
            if (school == null) {
                sql = "SELECT * FROM EVENTS ORDER BY holding_date DESC, event_id";
                statement = connection.prepareStatement(sql);
            } else {
                sql = "SELECT * FROM EVENTS WHERE school = ? ORDER BY holding_date DESC, event_id";
                statement = connection.prepareStatement(sql);
                statement.setString(1, school);
            }

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Event event = mapResultSetToEvent(resultSet);
                eventList.add(event);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return eventList;
    }

    /**
     * 主催者IDでイベントを取得
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
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return eventList;
    }

    /**
     * イベントを登録（TIME/DATE型対応版）
     */
    public int save(Event event) throws Exception {
        System.out.println("\n  ┌─────────────────────────────────────────┐");
        System.out.println("  │   EventDao.save() メソッド開始          │");
        System.out.println("  └─────────────────────────────────────────┘");

        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "INSERT INTO EVENTS (" +
                         "event_id, event_name, event_overview, holding_date, holding_time, " +
                         "address, max_count, event_hold_state, host_id" +
                         ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            System.out.println("    SQL: " + sql);

            statement = connection.prepareStatement(sql);

            // 1. event_id
            statement.setString(1, event.getEventId());
            System.out.println("    1. event_id = " + event.getEventId());

            // 2. event_name
            statement.setString(2, event.getEventName());
            System.out.println("    2. event_name = " + event.getEventName());

            // 3. event_overview
            statement.setString(3, event.getEventOverview());
            System.out.println("    3. event_overview = " + event.getEventOverview().substring(0, Math.min(30, event.getEventOverview().length())) + "...");

            // 4. holding_date (DATE型)
            Date sqlDate = Date.valueOf(event.getHoldingDate());
            statement.setDate(4, sqlDate);
            System.out.println("    4. holding_date = " + event.getHoldingDate() + " → " + sqlDate);

            // 5. holding_time (TIME型) ★重要
            Time sqlTime = Time.valueOf(event.getHoldingTime() + ":00");
            statement.setTime(5, sqlTime);
            System.out.println("    5. holding_time = " + event.getHoldingTime() + " → " + sqlTime);

            // 6. address
            statement.setString(6, event.getAddress());
            System.out.println("    6. address = " + event.getAddress());

            // 7. max_count
            statement.setInt(7, event.getMaxCount());
            System.out.println("    7. max_count = " + event.getMaxCount());

            // 8. event_hold_state
            statement.setString(8, event.getEventHoldState() != null ? event.getEventHoldState() : "1");
            System.out.println("    8. event_hold_state = " + (event.getEventHoldState() != null ? event.getEventHoldState() : "1"));

            // 9. host_id
            statement.setString(9, event.getHostId());
            System.out.println("    9. host_id = " + event.getHostId());

            System.out.println("\n    → executeUpdate() 実行中...");
            count = statement.executeUpdate();
            System.out.println("    ← executeUpdate() 完了: " + count + "件");

            System.out.println("\n  ✓ イベント登録成功！");

        } catch (Exception e) {
            System.err.println("\n  ✗✗✗ EventDao.save() エラー ✗✗✗");
            System.err.println("    " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    /**
     * イベント情報を更新
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
            statement.setDate(2, Date.valueOf(event.getHoldingDate()));
            statement.setTime(3, Time.valueOf(event.getHoldingTime() + ":00"));
            statement.setString(4, event.getAddress());
            statement.setInt(5, event.getMaxCount());
            statement.setString(6, event.getEventHoldState());
            statement.setString(7, event.getPhoneNumber());
            statement.setString(8, event.getLink());
            statement.setString(9, event.getEventOverview());
            statement.setString(10, event.getEventId());
            count = statement.executeUpdate();
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    /**
     * イベントを削除
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
        } finally {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return count;
    }

    /**
     * ResultSetからEventオブジェクトにマッピング
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();

        event.setEventId(rs.getString("event_id"));
        event.setEventName(rs.getString("event_name"));

        // DATE型からStringに変換
        Date holdingDate = rs.getDate("holding_date");
        if (holdingDate != null) {
            event.setHoldingDate(holdingDate.toString());
        }

        // TIME型からStringに変換（HH:mm形式）
        Time holdingTime = rs.getTime("holding_time");
        if (holdingTime != null) {
            event.setHoldingTime(holdingTime.toString().substring(0, 5));
        }

        event.setAddress(rs.getString("address"));
        event.setMaxCount(rs.getInt("max_count"));
        event.setEventHoldState(rs.getString("event_hold_state"));
        event.setPhoneNumber(rs.getString("phone_number"));
        event.setLink(rs.getString("link"));
        event.setEventOverview(rs.getString("event_overview"));
        event.setHostId(getStringOrNull(rs, "host_id"));
        event.setHostName(getStringOrNull(rs, "host_name"));
        event.setCategoryId(getStringOrNull(rs, "category_id"));
        event.setMapInHall(getStringOrNull(rs, "map_in_hall"));
        event.setMapOutOfHall(getStringOrNull(rs, "map_out_of_hall"));

        return event;
    }

    /**
     * ResultSetから安全に文字列を取得
     */
    private String getStringOrNull(ResultSet rs, String columnName) {
        try {
            return rs.getString(columnName);
        } catch (SQLException e) {
            return null;
        }
    }
}