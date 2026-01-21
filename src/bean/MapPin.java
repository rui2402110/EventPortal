package bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * MAP_PINテーブルに対応するModelクラス
 */
public class MapPin implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pinId;
    private Integer mapId;
    private String pinName;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
    private String pinColor;
    private String iconType;
    private Integer displayOrder;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // デフォルトコンストラクタ
    public MapPin() {
    }

    // 全フィールドコンストラクタ
    public MapPin(Integer pinId, Integer mapId, String pinName,
                  Double latitude, Double longitude, String address,
                  String description, String pinColor, String iconType,
                  Integer displayOrder, Timestamp createdAt, Timestamp updatedAt) {
        this.pinId = pinId;
        this.mapId = mapId;
        this.pinName = pinName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.pinColor = pinColor;
        this.iconType = iconType;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter/Setter
    public Integer getPinId() {
        return pinId;
    }

    public void setPinId(Integer pinId) {
        this.pinId = pinId;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public String getPinName() {
        return pinName;
    }

    public void setPinName(String pinName) {
        this.pinName = pinName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPinColor() {
        return pinColor;
    }

    public void setPinColor(String pinColor) {
        this.pinColor = pinColor;
    }

    public String getIconType() {
        return iconType;
    }

    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "MapPin{" +
                "pinId=" + pinId +
                ", mapId=" + mapId +
                ", pinName='" + pinName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", pinColor='" + pinColor + '\'' +
                ", iconType='" + iconType + '\'' +
                ", displayOrder=" + displayOrder +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
