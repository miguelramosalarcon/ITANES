package pe.miguelramos.itanes.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pe.miguelramos.itanes.data.local.dao.PlaceDao;
import pe.miguelramos.itanes.data.local.entity.PlaceEntity;

@Database(entities = {PlaceEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract PlaceDao placeDao();

    public static AppDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "itanes_database"
                    ).build();
                }
            }
        }
        return instance;
    }
}
