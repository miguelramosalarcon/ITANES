package pe.miguelramos.itanes.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "favorites")
public class FavoriteEntity {

    @PrimaryKey
    private int placeId;

    @NotNull
    private String createdAt;

    public FavoriteEntity(int placeId, @NotNull String createdAt) {
        this.placeId = placeId;
        this.createdAt = createdAt;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    @NotNull
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull String createdAt) {
        this.createdAt = createdAt;
    }
}
