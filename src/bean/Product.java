package bean;

import java.io.Serializable;

public class Product implements Serializable {
	    /** イベントID */
	    private String eventId;

	    /** 商品ID */
	    private String itemId;

	    /** 価格 */
	    private Integer price;

	    /** 概要 */
	    private String overview;

	    /** 在庫 */
	    private Integer stock;

	    /** 画像 */
	    private String image;

	    /** 商品名 */
	    private String productName;

		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId;
		}

		public String getItemId() {
			return itemId;
		}

		public void setItemId(String itemId) {
			this.itemId = itemId;
		}

		public Integer getPrice() {
			return price;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}

		public String getOverview() {
			return overview;
		}

		public void setOverview(String overview) {
			this.overview = overview;
		}

		public Integer getStock() {
			return stock;
		}

		public void setStock(Integer stock) {
			this.stock = stock;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

	    // Getter and Setter

}
