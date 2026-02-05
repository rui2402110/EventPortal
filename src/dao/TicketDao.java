package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bean.Ticket;

/**
 * チケットDAO
 */
public class TicketDao extends Dao {

    /**
     * チケットIDでチケットを取得
     * @param ticketId チケットID
     * @return チケット情報
     * @throws Exception
     */
    public Ticket get(String ticketId) throws Exception {
        Ticket ticket = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM ticket WHERE ticket_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ticketId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ticket = mapResultSetToTicket(resultSet);
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
        return ticket;
    }

    /**
     * イベントIDとユーザーIDでチケットを取得
     * @param eventId イベントID
     * @param userId ユーザーID
     * @return チケット情報
     * @throws Exception
     */
    public Ticket getByEventAndUser(String eventId, String userId) throws Exception {
        Ticket ticket = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM ticket WHERE event_id = ? AND user_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);
            statement.setString(2, userId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ticket = mapResultSetToTicket(resultSet);
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
        return ticket;
    }

    /**
     * イベントIDで全チケットを取得
     * @param eventId イベントID
     * @return チケットリスト
     * @throws Exception
     */
    public List<Ticket> getByEventId(String eventId) throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM ticket WHERE event_id = ? ORDER BY ticket_id";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Ticket ticket = mapResultSetToTicket(resultSet);
                ticketList.add(ticket);
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
        return ticketList;
    }

    /**
     * ユーザーIDで全チケットを取得
     * @param userId ユーザーID
     * @return チケットリスト
     * @throws Exception
     */
    public List<Ticket> getByUserId(String userId) throws Exception {
        List<Ticket> ticketList = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT * FROM ticket WHERE user_id = ? ORDER BY ticket_id";
            statement = connection.prepareStatement(sql);
            statement.setString(1, userId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Ticket ticket = mapResultSetToTicket(resultSet);
                ticketList.add(ticket);
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
        return ticketList;
    }

    /**
     * イベントの入場者数を取得（status=2のチケット数）
     * @param eventId イベントID
     * @return 入場者数
     * @throws Exception
     */
    public int getAdmittedCount(String eventId) throws Exception {
        int count = 0;
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT COUNT(*) as count FROM ticket WHERE event_id = ? AND status = 2";
            statement = connection.prepareStatement(sql);
            statement.setString(1, eventId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("count");
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
     * チケットを登録
     * @param ticket チケット情報
     * @return 登録件数
     * @throws Exception
     */
    public int insert(Ticket ticket) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "INSERT INTO ticket (ticket_id, event_id, user_id, participant_name, status, qr_image_data, qr_image_path, used_at) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ticket.getTicketId());
            statement.setString(2, ticket.getEventId());
            statement.setString(3, ticket.getUserId());
            statement.setString(4, ticket.getParticipantName());
            statement.setInt(5, ticket.getStatus());
            statement.setString(6, ticket.getQrImageData());
            statement.setString(7, ticket.getQrImagePath());
            statement.setTimestamp(8, ticket.getUsedAt());
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
     * チケット情報を更新
     * @param ticket チケット情報
     * @return 更新件数
     * @throws Exception
     */
    public int update(Ticket ticket) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "UPDATE ticket SET event_id = ?, user_id = ?, participant_name = ?, " +
                         "status = ?, qr_image_data = ?, qr_image_path = ?, used_at = ? " +
                         "WHERE ticket_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ticket.getEventId());
            statement.setString(2, ticket.getUserId());
            statement.setString(3, ticket.getParticipantName());
            statement.setInt(4, ticket.getStatus());
            statement.setString(5, ticket.getQrImageData());
            statement.setString(6, ticket.getQrImagePath());
            statement.setTimestamp(7, ticket.getUsedAt());
            statement.setString(8, ticket.getTicketId());
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
     * チケットのステータスを更新（入場処理用）
     * @param ticketId チケットID
     * @param status ステータス（1:有効, 2:使用済み, 3:無効）
     * @param usedAt 使用日時
     * @return 更新件数
     * @throws Exception
     */
    public int updateStatus(String ticketId, int status, Timestamp usedAt) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "UPDATE ticket SET status = ?, used_at = ? WHERE ticket_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, status);
            statement.setTimestamp(2, usedAt);
            statement.setString(3, ticketId);
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
     * QR画像データを更新
     * @param ticketId チケットID
     * @param qrImageData Base64エンコードされたQR画像データ
     * @return 更新件数
     * @throws Exception
     */
    public int updateQRImage(String ticketId, String qrImageData) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "UPDATE ticket SET qr_image_data = ? WHERE ticket_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, qrImageData);
            statement.setString(2, ticketId);
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
     * チケットを削除
     * @param ticketId チケットID
     * @return 削除件数
     * @throws Exception
     */
    public int delete(String ticketId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            String sql = "DELETE FROM ticket WHERE ticket_id = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ticketId);
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
     * ResultSetからTicketオブジェクトにマッピング
     * @param rs ResultSet
     * @return Ticketオブジェクト
     * @throws SQLException
     */
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketId(rs.getString("ticket_id"));
        ticket.setEventId(rs.getString("event_id"));
        ticket.setUserId(rs.getString("user_id"));
        ticket.setParticipantName(rs.getString("participant_name"));
        ticket.setStatus(rs.getInt("status"));
        ticket.setQrImageData(rs.getString("qr_image_data"));
        ticket.setQrImagePath(rs.getString("qr_image_path"));
        ticket.setUsedAt(rs.getTimestamp("used_at"));
        return ticket;
    }
}