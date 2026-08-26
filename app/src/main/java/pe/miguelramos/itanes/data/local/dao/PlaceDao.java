package pe.miguelramos.itanes.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pe.miguelramos.itanes.data.local.entity.PlaceEntity;

@Dao
public interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PlaceEntity> places);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaceEntity place);

    @Query("SELECT * FROM places ORDER BY orderNumber ASC")
    List<PlaceEntity> getAllPlaces();

    @Query("SELECT * FROM places WHERE id = :id")
    PlaceEntity getPlaceById(int id);

    @Query("DELETE FROM places")
    void deleteAll();
}
