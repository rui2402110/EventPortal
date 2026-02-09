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

    // ステータス定数
    public static final int STATUS_VALID = 1;
    public static final int STATUS_USED = 2;
    public static final int STATUS_INVALID = 3;

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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQrImageData() {
        return qrImageData;
    }

    public void setQrImageData(String qrImageData) {
        this.qrImageData = qrImageData;
    }

    public String getQrImagePath() {
        return qrImagePath;
    }

    public void setQrImagePath(String qrImagePath) {
        this.qrImagePath = qrImagePath;
    }

    public Timestamp getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }

    // ========== 便利メソッド ==========

    public boolean isValid() {
        return status == STATUS_VALID;
    }

    public boolean isUsed() {
        return status == STATUS_USED;
    }

    public boolean isInvalid() {
        return status == STATUS_INVALID;
    }

    public String getStatusString() {
        switch (status) {
            case STATUS_VALID:
                return "有効";
            case STATUS_USED:
                return "使用済み";
            case STATUS_INVALID:
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