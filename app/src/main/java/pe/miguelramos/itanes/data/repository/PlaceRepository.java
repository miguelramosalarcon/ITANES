package pe.miguelramos.itanes.data.repository;

import android.app.Application;

import java.util.List;

import pe.miguelramos.itanes.data.local.dao.PlaceDao;
import pe.miguelramos.itanes.data.local.database.AppDatabase;
import pe.miguelramos.itanes.data.local.entity.PlaceEntity;

public class PlaceRepository {

    private final PlaceDao placeDao;

    public PlaceRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        placeDao = db.placeDao();
    }

    public List<PlaceEntity> getAllPlaces() {
        return placeDao.getAllPlaces();
    }

    public PlaceEntity getPlaceById(int id) {
        return placeDao.getPlaceById(id);
    }

    public int getCount() {
        return placeDao.getCount();
    }

    public void insertAll(List<PlaceEntity> places) {
        placeDao.insertAll(places);
    }

    public void insert(PlaceEntity place) {
        place.setId(place.getId()); // ensure ID is set if logic needs it, though Room handles it.
        placeDao.insert(place);
    }

    public void deleteAll() {
        placeDao.deleteAll();
    }
}
