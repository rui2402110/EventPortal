package bean;

import java.io.Serializable;

/**
 * 注文明細情報を保持するBeanクラス
 */
public class OrderItem implements Serializable {
    private String orderItemId;    // 注文明細ID
    private String orderId;        // 注文ID
    private String itemId;         // 商品ID
    private int quantity;          // 数量
    private int unitPrice;         // 単価
    private Product product;       // 商品情報（JOIN用）

    // コンストラクタ
    public OrderItem() {
    }

    // Getter & Setter
    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // ユーティリティメソッド
    public int getSubtotal() {
        return unitPrice * quantity;
    }
}