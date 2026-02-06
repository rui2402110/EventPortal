package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * イベントBean（完全版）
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    /** イベントID */
    private String eventId;

    /** イベント名 */
    private String eventName;

    /** 開催日 */
    private String holdingDate;

    /** 開催時刻 */
    private String holdingTime;

    /** 住所 */
    private String address;

    /** 定員 */
    private int maxCount;

    /** 開催状態 (1:開催前, 2:開催中, 3:開催後) */
    private String eventHoldState;

    /** 電話番号 */
    private String phoneNumber;

    /** リンク */
    private String link;

    /** イベント概要 */
    private String eventOverview;

    /** 主催者ID */
    private String hostId;

    /** 主催者名 */
    private String hostName;

    /** ユーザーがこのイベントのチケットを持っているか */
    private boolean hasTicket;

    /** カテゴリID */
    private String categoryId;

    /** クレジット */
    private String credit;

    /** イベント追加日 */
    private String eventAddDate;

    /** 会場内マップ */
    private String mapInHall;

    /** 会場外マップ */
    private String mapOutOfHall;

    /** チケット情報 */
    private String ticketInfo;

    /** ユーザーID */
    private String userId;

    /** 合計支払額 */
    private int totalPayment;

    /**
     * デフォルトコンストラクタ
     */
    public Event() {
    }

    // ========== Getter / Setter ==========

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

    public String getHoldingDate() {
        return holdingDate;
    }

    public void setHoldingDate(String holdingDate) {
        this.holdingDate = holdingDate;
    }

    public void setHoldingDate(LocalDate holdingDate) {
        this.holdingDate = holdingDate != null ? holdingDate.toString() : null;
    }

    public String getHoldingTime() {
        return holdingTime;
    }

    public void setHoldingTime(String holdingTime) {
        this.holdingTime = holdingTime;
    }

    public void setHoldingTime(LocalTime holdingTime) {
        this.holdingTime = holdingTime != null ? holdingTime.toString() : null;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getEventHoldState() {
        return eventHoldState;
    }

    public void setEventHoldState(String eventHoldState) {
        this.eventHoldState = eventHoldState;
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

    public String getEventOverview() {
        return eventOverview;
    }

    public void setEventOverview(String eventOverview) {
        this.eventOverview = eventOverview;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public boolean isHasTicket() {
        return hasTicket;
    }

    public boolean getHasTicket() {
        return hasTicket;
    }

    public void setHasTicket(boolean hasTicket) {
        this.hasTicket = hasTicket;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getEventAddDate() {
        return eventAddDate;
    }

    public void setEventAddDate(String eventAddDate) {
        this.eventAddDate = eventAddDate;
    }

    public void setEventAddDate(LocalDate eventAddDate) {
        this.eventAddDate = eventAddDate != null ? eventAddDate.toString() : null;
    }

    public String getMapInHall() {
        return mapInHall;
    }

    public void setMapInHall(String mapInHall) {
        this.mapInHall = mapInHall;
    }

    public String getMapOutOfHall() {
        return mapOutOfHall;
    }

    public void setMapOutOfHall(String mapOutOfHall) {
        this.mapOutOfHall = mapOutOfHall;
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", holdingDate='" + holdingDate + '\'' +
                ", holdingTime='" + holdingTime + '\'' +
                ", address='" + address + '\'' +
                ", maxCount=" + maxCount +
                ", eventHoldState='" + eventHoldState + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", hasTicket=" + hasTicket +
                '}';
    }
}