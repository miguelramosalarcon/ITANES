package pe.miguelramos.itanes.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pe.miguelramos.itanes.data.local.entity.FavoriteEntity;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntity favorite);

    @Query("DELETE FROM favorites WHERE placeId = :placeId")
    void deleteByPlaceId(int placeId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE placeId = :placeId)")
    boolean isFavorite(int placeId);

    @Query("SELECT placeId FROM favorites")
    List<Integer> getAllFavoriteIds();
}
