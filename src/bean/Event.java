package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event implements Serializable {
    /** イベントID */
    private String eventId;

    /** イベント名 */
    private String eventName;

    /** イベント概要 */
    private String eventOverview;

    /** 開催日 */
    private LocalDate holdingDate;

    /** 開催時間 */
    private LocalTime holdingTime;

    /** 開催場所(住所) */
    private String address;

    /** 会場外マップ */
    private String mapOutOfHall;

    /** 会場内マップ */
    private String mapInHall;

    /** 最大人数 */
    private Integer maxCount;

    /** カテゴリID */
    private String categoryId;

    /** 電話番号 */
    private String phoneNumber;

    /** リンク */
    private String link;

    /** クレジット */
    private String credit;

    /** ユーザーID */
    private String userId;

    /** チケット情報 */
    private String ticketInfo;

    /** イベント開催状態 */
    private String eventHoldState;

    /** イベント作成日 */
    private LocalDate eventAddDate;

    /** イベント合計入金額 */
    private Integer totalPayment;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventOverview() {
		return eventOverview;
	}

	public void setEventOverview(String eventOverview) {
		this.eventOverview = eventOverview;
	}

	public LocalDate getHoldingDate() {
		return holdingDate;
	}

	public void setHoldingDate(LocalDate holdingDate) {
		this.holdingDate = holdingDate;
	}

	public LocalTime getHoldingTime() {
		return holdingTime;
	}

	public void setHoldingTime(LocalTime holdingTime) {
		this.holdingTime = holdingTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMapOutOfHall() {
		return mapOutOfHall;
	}

	public void setMapOutOfHall(String mapOutOfHall) {
		this.mapOutOfHall = mapOutOfHall;
	}

	public String getMapInHall() {
		return mapInHall;
	}

	public void setMapInHall(String mapInHall) {
		this.mapInHall = mapInHall;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTicketInfo() {
		return ticketInfo;
	}

	public void setTicketInfo(String ticketInfo) {
		this.ticketInfo = ticketInfo;
	}

	public String getEventHoldState() {
		return eventHoldState;
	}

	public void setEventHoldState(String eventHoldState) {
		this.eventHoldState = eventHoldState;
	}

	public LocalDate getEventAddDate() {
		return eventAddDate;
	}

	public void setEventAddDate(LocalDate eventAddDate) {
		this.eventAddDate = eventAddDate;
	}

	public Integer getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Integer totalPayment) {
		this.totalPayment = totalPayment;
	}

}
