package bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * チケットBean
 */
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    /** チケットID */
    private String ticketId;

    /** イベントID */
    private String eventId;

    /** ユーザーID */
    private String userId;

    /** 参加者名 */
    private String participantName;

    /** ステータス (1:有効, 2:使用済み, 3:無効) */
    private int status;

    /** QR画像データ (Base64エンコード) */
    private String qrImageData;

    /** QR画像パス */
    private String qrImagePath;

    /** 使用日時 */
    private Timestamp usedAt;

    /**
     * デフォルトコンストラクタ
     */
    public Ticket() {
    }

    /**
     * コンストラクタ
     * @param ticketId チケットID
     * @param eventId イベントID
     * @param userId ユーザーID
     * @param participantName 参加者名
     * @param status ステータス
     */
    public Ticket(String ticketId, String eventId, String userId, String participantName, int status) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.userId = userId;
        this.participantName = participantName;
        this.status = status;
    }

    // ========== Getter / Setter ==========

    /**
     * チケットIDを取得
     * @return チケットID
     */
    public String getTicketId() {
        return ticketId;
    }

    /**
     * チケットIDを設定
     * @param ticketId チケットID
     */
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * イベントIDを取得
     * @return イベントID
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * イベントIDを設定
     * @param eventId イベントID
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * ユーザーIDを取得
     * @return ユーザーID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定
     * @param userId ユーザーID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 参加者名を取得
     * @return 参加者名
     */
    public String getParticipantName() {
        return participantName;
    }

    /**
     * 参加者名を設定
     * @param participantName 参加者名
     */
    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    /**
     * ステータスを取得
     * @return ステータス (1:有効, 2:使用済み, 3:無効)
     */
    public int getStatus() {
        return status;
    }

    /**
     * ステータスを設定
     * @param status ステータス (1:有効, 2:使用済み, 3:無効)
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * QR画像データを取得
     * @return QR画像データ (Base64エンコード)
     */
    public String getQrImageData() {
        return qrImageData;
    }

    /**
     * QR画像データを設定
     * @param qrImageData QR画像データ (Base64エンコード)
     */
    public void setQrImageData(String qrImageData) {
        this.qrImageData = qrImageData;
    }

    /**
     * QR画像パスを取得
     * @return QR画像パス
     */
    public String getQrImagePath() {
        return qrImagePath;
    }

    /**
     * QR画像パスを設定
     * @param qrImagePath QR画像パス
     */
    public void setQrImagePath(String qrImagePath) {
        this.qrImagePath = qrImagePath;
    }

    /**
     * 使用日時を取得
     * @return 使用日時
     */
    public Timestamp getUsedAt() {
        return usedAt;
    }

    /**
     * 使用日時を設定
     * @param usedAt 使用日時
     */
    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }

    /**
     * ステータスが有効かどうかを判定
     * @return true:有効, false:無効
     */
    public boolean isValid() {
        return status == 1;
    }

    /**
     * ステータスが使用済みかどうかを判定
     * @return true:使用済み, false:未使用
     */
    public boolean isUsed() {
        return status == 2;
    }

    /**
     * ステータスが無効かどうかを判定
     * @return true:無効, false:有効
     */
    public boolean isInvalid() {
        return status == 3;
    }

    /**
     * ステータスを文字列で取得
     * @return ステータス文字列
     */
    public String getStatusString() {
        switch (status) {
            case 1:
                return "有効";
            case 2:
                return "使用済み";
            case 3:
                return "無効";
            default:
                return "不明";
        }
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", userId='" + userId + '\'' +
                ", participantName='" + participantName + '\'' +
                ", status=" + status +
                ", qrImageData=" + (qrImageData != null ? "存在" : "なし") +
                ", qrImagePath='" + qrImagePath + '\'' +
                ", usedAt=" + usedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketId != null ? ticketId.equals(ticket.ticketId) : ticket.ticketId == null;
    }

    @Override
    public int hashCode() {
        return ticketId != null ? ticketId.hashCode() : 0;
    }
}