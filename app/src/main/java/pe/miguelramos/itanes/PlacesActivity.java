package pe.miguelramos.itanes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelramos.itanes.data.local.entity.PlaceEntity;
import pe.miguelramos.itanes.data.repository.PlaceRepository;
import pe.miguelramos.itanes.ui.detail.PlaceDetailActivity;

public class PlacesActivity extends AppCompatActivity {

    public static final String EXTRA_PLACE_ID = "pe.miguelramos.itanes.EXTRA_PLACE_ID";
    private PlaceRepository repository;
    private PlaceAdapter adapter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarPlaces);
        toolbar.setNavigationOnClickListener(v -> finish());

        repository = new PlaceRepository(getApplication());
        setupRecyclerView();
        loadPlaces();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlaces);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAdapter();
        adapter.setOnPlaceClickListener(place -> {
            Intent intent = new Intent(PlacesActivity.this, PlaceDetailActivity.class);
            intent.putExtra(EXTRA_PLACE_ID, place.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadPlaces() {
        executor.execute(() -> {
            List<PlaceEntity> places = repository.getAllPlaces();
            runOnUiThread(() -> adapter.setPlaces(places));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
