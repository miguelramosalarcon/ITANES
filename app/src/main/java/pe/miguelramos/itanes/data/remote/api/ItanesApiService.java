package pe.miguelramos.itanes.data.remote.api;

import java.util.List;

import pe.miguelramos.itanes.data.remote.dto.PlaceRemoteDto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ItanesApiService {

    // Endpoint conceptual: places.json
    @GET("places.json")
    Call<List<PlaceRemoteDto>> getPlaces();
}
