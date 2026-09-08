package pe.miguelramos.itanes.data.repository;

import android.app.Application;

import pe.miguelramos.itanes.data.local.dao.FavoriteDao;
import pe.miguelramos.itanes.data.local.database.AppDatabase;
import pe.miguelramos.itanes.data.local.entity.FavoriteEntity;

public class FavoriteRepository {

    private final FavoriteDao favoriteDao;

    public FavoriteRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        favoriteDao = db.favoriteDao();
    }

    public void addFavorite(FavoriteEntity favorite) {
        favoriteDao.insert(favorite);
    }

    public void removeFavorite(int placeId) {
        favoriteDao.deleteByPlaceId(placeId);
    }

    public boolean isFavorite(int placeId) {
        return favoriteDao.isFavorite(placeId);
    }
}
