package pe.miguelramos.itanes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import pe.miguelramos.itanes.data.local.seed.PlaceDataSeeder;
import pe.miguelramos.itanes.data.remote.api.ItanesApiService;
import pe.miguelramos.itanes.data.remote.dto.PlaceRemoteDto;
import pe.miguelramos.itanes.data.remote.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ITANES_API";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Iniciar carga inicial de datos en Room (si la tabla está vacía)
        PlaceDataSeeder.seed(getApplication());

        // Prueba de consumo de API REST
        testRemoteApi();

        Button buttonExplore = findViewById(R.id.buttonExplore);
        buttonExplore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void testRemoteApi() {
        ItanesApiService apiService = RetrofitClient.getApiService();
        apiService.getPlaces().enqueue(new Callback<List<PlaceRemoteDto>>() {
            @Override
            public void onResponse(Call<List<PlaceRemoteDto>> call, Response<List<PlaceRemoteDto>> response) {
                if (response.isSuccessful()) {
                    List<PlaceRemoteDto> places = response.body();
                    if (places != null) {
                        Log.d(TAG, "Respuesta recibida correctamente");
                        Log.d(TAG, "Total lugares: " + places.size());
                        for (PlaceRemoteDto place : places) {
                            Log.d(TAG, place.getId() + " - " + place.getName());
                        }
                    } else {
                        Log.e(TAG, "La respuesta es exitosa pero el body es null");
                    }
                } else {
                    Log.e(TAG, "Error en la respuesta HTTP: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PlaceRemoteDto>> call, Throwable t) {
                Log.e(TAG, "Error de conexión o red: " + t.getMessage());
            }
        });
    }
}