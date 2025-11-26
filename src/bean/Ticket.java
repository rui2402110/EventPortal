package bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {
    private String ticketId;
    private String userId;
    private String eventId;
    private String qrImagePath;
    private int status;  // 1:有効, 2:使用済み, 3:無効
    private String ticketInfo;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;

    // イベント情報（結合用）
    private Event event;

    // ゲッター・セッター
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
}