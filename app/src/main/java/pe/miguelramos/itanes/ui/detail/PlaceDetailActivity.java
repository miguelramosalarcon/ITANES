package pe.miguelramos.itanes.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pe.miguelramos.itanes.PlacesActivity;
import pe.miguelramos.itanes.R;
import pe.miguelramos.itanes.data.local.entity.FavoriteEntity;
import pe.miguelramos.itanes.data.local.entity.PlaceEntity;
import pe.miguelramos.itanes.data.repository.FavoriteRepository;
import pe.miguelramos.itanes.data.repository.PlaceRepository;
import pe.miguelramos.itanes.ui.map.MapActivity;

public class PlaceDetailActivity extends AppCompatActivity {

    private PlaceRepository repository;
    private FavoriteRepository favoriteRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private TextView textName;
    private TextView textShortDescription;
    private TextView textDescription;
    private TextView textAddress;
    private TextView textCoordinates;
    private ImageView imagePlace;
    private com.google.android.material.button.MaterialButton btnFavorite;
    private com.google.android.material.button.MaterialButton btnShare;
    private com.google.android.material.button.MaterialButton btnViewMap;

    private boolean isFavorite = false;
    private int currentPlaceId = -1;
    private PlaceEntity currentPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbarDetail);
        toolbar.setNavigationOnClickListener(v -> finish());

        initViews();
        repository = new PlaceRepository(getApplication());
        favoriteRepository = new FavoriteRepository(getApplication());

        currentPlaceId = getIntent().getIntExtra(PlacesActivity.EXTRA_PLACE_ID, -1);

        if (currentPlaceId == -1) {
            handleInvalidId();
            return;
        }

        loadPlaceDetails(currentPlaceId);
        checkFavoriteStatus(currentPlaceId);
        setupFavoriteButton();
        setupShareButton();
        setupViewMapButton();
    }

    private void initViews() {
        textName = findViewById(R.id.textPlaceNameDetail);
        textShortDescription = findViewById(R.id.textShortDescriptionDetail);
        textDescription = findViewById(R.id.textDescriptionDetail);
        textAddress = findViewById(R.id.textAddressDetail);
        textCoordinates = findViewById(R.id.textCoordinatesDetail);
        imagePlace = findViewById(R.id.imagePlaceDetail);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnShare = findViewById(R.id.btnShare);
        btnViewMap = findViewById(R.id.btnViewMap);
    }

    private void checkFavoriteStatus(int id) {
        executor.execute(() -> {
            isFavorite = favoriteRepository.isFavorite(id);
            runOnUiThread(this::updateFavoriteButtonUi);
        });
    }

    private void setupFavoriteButton() {
        btnFavorite.setOnClickListener(v -> {
            executor.execute(() -> {
                if (isFavorite) {
                    favoriteRepository.removeFavorite(currentPlaceId);
                    isFavorite = false;
                } else {
                    String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    favoriteRepository.addFavorite(new FavoriteEntity(currentPlaceId, timestamp));
                    isFavorite = true;
                }
                runOnUiThread(this::updateFavoriteButtonUi);
            });
        });
    }

    private void setupViewMapButton() {
        btnViewMap.setOnClickListener(v -> {
            Intent intent = new Intent(PlaceDetailActivity.this, MapActivity.class);
            intent.putExtra(PlacesActivity.EXTRA_PLACE_ID, currentPlaceId);
            startActivity(intent);
        });
    }

    private void setupShareButton() {
        btnShare.setOnClickListener(v -> {
            if (currentPlace == null) return;

            String shareText = currentPlace.getName() + "\n" +
                    currentPlace.getShortDescription() + "\n" +
                    getString(R.string.label_address) + " " + currentPlace.getAddress() + "\n\n" +
                    getString(R.string.share_footer, getString(R.string.app_name));

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, currentPlace.getName());
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            Intent chooser = Intent.createChooser(shareIntent, getString(R.string.share_chooser_title));

            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Toast.makeText(this, R.string.share_error_no_app, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteButtonUi() {
        if (isFavorite) {
            btnFavorite.setText(R.string.btn_favorite_remove);
        } else {
            btnFavorite.setText(R.string.btn_favorite_add);
        }
    }

    private void loadPlaceDetails(int id) {
        executor.execute(() -> {
            PlaceEntity place = repository.getPlaceById(id);
            runOnUiThread(() -> {
                if (place != null) {
                    currentPlace = place;
                    displayPlace(place);
                } else {
                    handlePlaceNotFound();
                }
            });
        });
    }

    private void displayPlace(PlaceEntity place) {
        textName.setText(place.getName());
        textShortDescription.setText(place.getShortDescription());
        textDescription.setText(place.getDescription());
        textAddress.setText(place.getAddress());

        com.bumptech.glide.Glide.with(this)
                .load(place.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .centerCrop()
                .into(imagePlace);

        String latLabel = getString(R.string.label_latitude);
        String longLabel = getString(R.string.label_longitude);
        String coordinates = String.format(Locale.getDefault(), "%s %.4f, %s %.4f",
                latLabel, place.getLatitude(), longLabel, place.getLongitude());
        textCoordinates.setText(coordinates);
    }

    private void handleInvalidId() {
        Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handlePlaceNotFound() {
        Toast.makeText(this, R.string.error_place_not_found, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
