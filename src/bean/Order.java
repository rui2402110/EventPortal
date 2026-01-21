package bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 注文情報を保持するBeanクラス
 */
public class Order implements Serializable {
    private String orderId;              // 注文ID
    private String userId;               // ユーザーID
    private String eventId;              // イベントID
    private String ticketId;             // チケットID
    private LocalDateTime orderDate;     // 注文日時
    private int totalAmount;             // 合計金額
    private int status;                  // ステータス（1:注文済み, 2:準備中, 3:完了, 9:キャンセル）
    private List<OrderItem> items;       // 注文明細リスト

    // コンストラクタ
    public Order() {
    }

    // Getter & Setter
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // ユーティリティメソッド
    public String getStatusText() {
        switch (status) {
            case 1:
                return "注文済み";
            case 2:
                return "準備中";
            case 3:
                return "完了";
            case 9:
                return "キャンセル";
            default:
                return "不明";
        }
    }

    public boolean isActive() {
        return status != 9;
    }
}