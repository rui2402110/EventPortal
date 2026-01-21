package bean;

import java.io.Serializable;
import java.sql.Timestamp;
public class Map implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer mapId;
    private String mapName;
    private String description;
    private Double defaultLat;
    private Double defaultLng;
    private Integer defaultZoom;
    private String createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // デフォルトコンストラクタ
    public Map() {
    }

    // 全フィールドコンストラクタ
    public Map(Integer mapId, String mapName, String description,
               Double defaultLat, Double defaultLng, Integer defaultZoom,
               String createdBy, Timestamp createdAt, Timestamp updatedAt) {
        this.mapId = mapId;
        this.mapName = mapName;
        this.description = description;
        this.defaultLat = defaultLat;
        this.defaultLng = defaultLng;
        this.defaultZoom = defaultZoom;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter/Setter
    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDefaultLat() {
        return defaultLat;
    }

    public void setDefaultLat(Double defaultLat) {
        this.defaultLat = defaultLat;
    }

    public Double getDefaultLng() {
        return defaultLng;
    }

    public void setDefaultLng(Double defaultLng) {
        this.defaultLng = defaultLng;
    }

    public Integer getDefaultZoom() {
        return defaultZoom;
    }

    public void setDefaultZoom(Integer defaultZoom) {
        this.defaultZoom = defaultZoom;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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
        return "Map{" +
                "mapId=" + mapId +
                ", mapName='" + mapName + '\'' +
                ", description='" + description + '\'' +
                ", defaultLat=" + defaultLat +
                ", defaultLng=" + defaultLng +
                ", defaultZoom=" + defaultZoom +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
