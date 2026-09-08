package pe.miguelramos.itanes.ui.map;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.maplibre.android.MapLibre;
import org.maplibre.android.annotations.MarkerOptions;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.camera.CameraUpdateFactory;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.Style;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelramos.itanes.PlacesActivity;
import pe.miguelramos.itanes.R;
import pe.miguelramos.itanes.data.local.entity.PlaceEntity;
import pe.miguelramos.itanes.data.repository.PlaceRepository;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private PlaceRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private TextView textPlaceName;
    private TextView textPlaceAddress;

    private static final double MIN_ZOOM = 3.0;
    private static final double MAX_ZOOM = 19.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MapLibre debe inicializarse antes de setContentView si se usa en XML
        MapLibre.getInstance(this);

        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        textPlaceName = findViewById(R.id.textMapPlaceName);
        textPlaceAddress = findViewById(R.id.textMapPlaceAddress);

        findViewById(R.id.btnBackMap).setOnClickListener(v -> finish());
        setupZoomButtons();

        mapView.onCreate(savedInstanceState);
        repository = new PlaceRepository(getApplication());

        int placeId = getIntent().getIntExtra(PlacesActivity.EXTRA_PLACE_ID, -1);
        if (placeId == -1) {
            Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadPlaceAndSetupMap(placeId);
    }

    private void setupZoomButtons() {
        findViewById(R.id.btnZoomIn).setOnClickListener(v -> {
            if (mapLibreMap != null) {
                double currentZoom = mapLibreMap.getCameraPosition().zoom;
                if (currentZoom < MAX_ZOOM) {
                    mapLibreMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoom + 1));
                }
            }
        });

        findViewById(R.id.btnZoomOut).setOnClickListener(v -> {
            if (mapLibreMap != null) {
                double currentZoom = mapLibreMap.getCameraPosition().zoom;
                if (currentZoom > MIN_ZOOM) {
                    mapLibreMap.animateCamera(CameraUpdateFactory.zoomTo(currentZoom - 1));
                }
            }
        });
    }

    private void loadPlaceAndSetupMap(int id) {
        executor.execute(() -> {
            PlaceEntity place = repository.getPlaceById(id);
            runOnUiThread(() -> {
                if (place != null) {
                    setupMapWithPlace(place);
                } else {
                    Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });
    }

    private void setupMapWithPlace(PlaceEntity place) {
        textPlaceName.setText(place.getName());
        textPlaceAddress.setText(place.getAddress());

        double lat = place.getLatitude();
        double lon = place.getLongitude();

        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            Toast.makeText(this, R.string.error_invalid_coordinates, Toast.LENGTH_SHORT).show();
            return;
        }

        mapView.getMapAsync(map -> {
            this.mapLibreMap = map;
            map.setMinZoomPreference(MIN_ZOOM);
            map.setMaxZoomPreference(MAX_ZOOM);
            
            map.setStyle(new Style.Builder().fromUri("https://tiles.openfreemap.org/styles/liberty"), style -> {
                LatLng position = new LatLng(lat, lon);

                // Centrar cámara
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(position)
                        .zoom(14)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Agregar marcador
                map.addMarker(new MarkerOptions()
                        .position(position)
                        .title(place.getName()));
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        executor.shutdown();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
