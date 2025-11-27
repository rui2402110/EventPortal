package bean;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * チケット情報を保持するBeanクラス
 */
public class Ticket implements Serializable {
    private String ticketId;           // チケットID
    private String userId;             // ユーザーID
    private String eventId;            // イベントID
    private String qrImagePath;        // QRコード画像ファイルパス
    private String qrImageData;        // QRコード画像データ（Base64）
    private int status;                // ステータス（1:有効, 2:使用済み, 3:無効）
    private String ticketInfo;         // チケット情報（座席番号など）
    private LocalDateTime createdAt;   // 作成日時
    private LocalDateTime usedAt;      // 使用日時
    private Event event;               // イベント情報（JOIN用）

    // コンストラクタ
    public Ticket() {
    }

    // Getter & Setter
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getQrImagePath() {
        return qrImagePath;
    }

    public void setQrImagePath(String qrImagePath) {
        this.qrImagePath = qrImagePath;
    }

    public String getQrImageData() {
        return qrImageData;
    }

    public void setQrImageData(String qrImageData) {
        this.qrImageData = qrImageData;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // ユーティリティメソッド
    public boolean isValid() {
        return status == 1;
    }

    public boolean isUsed() {
        return status == 2;
    }

    public boolean isInvalid() {
        return status == 3;
    }

    public String getStatusText() {
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
}