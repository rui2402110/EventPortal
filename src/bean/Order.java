package bean;

import java.io.Serializable;

/**
 * 注文情報を保持するBean
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;         // 注文ID
    private String menuId;          // メニューID
    private String userId;          // ユーザーID
    private String eventId;         // イベントID
    private int quantity;           // 数量
    private int totalPrice;         // 合計金額
    private String orderDate;       // 注文日時
    private String status;          // ステータス

    // メニュー名（JOIN用）
    private String menuName;
    private String menuType;

    // デフォルトコンストラクタ
    public Order() {
    }

    // Getter/Setter
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}