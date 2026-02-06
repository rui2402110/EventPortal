package bean;

import java.io.Serializable;

/**
 * イベントBean
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

    public String getHoldingTime() {
        return holdingTime;
    }

    public void setHoldingTime(String holdingTime) {
        this.holdingTime = holdingTime;
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

    /**
     * チケット所持状態を取得
     * @return true:チケット所持, false:未所持
     */
    public boolean isHasTicket() {
        return hasTicket;
    }

    /**
     * チケット所持状態を取得（JSP用）
     * @return true:チケット所持, false:未所持
     */
    public boolean getHasTicket() {
        return hasTicket;
    }

    /**
     * チケット所持状態を設定
     * @param hasTicket チケット所持状態
     */
    public void setHasTicket(boolean hasTicket) {
        this.hasTicket = hasTicket;
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
                ", hasTicket=" + hasTicket +
                '}';
    }
}