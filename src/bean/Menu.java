package bean;

import java.io.Serializable;

/**
 * メニュー情報を保持するBean
 */
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private String menuId;          // メニューID
    private String eventId;         // イベントID
    private String menuName;        // メニュー名
    private String menuType;        // メニュー種別（グッズ/フード）
    private int price;              // 価格
    private String description;     // 説明
    private int stockQuantity;      // 在庫数
    private String imagePath;       // 画像パス
    private String createdAt;       // 作成日時

    // イベント名（JOIN用）
    private String eventName;

    // デフォルトコンストラクタ
    public Menu() {
    }

    // Getter/Setter
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}