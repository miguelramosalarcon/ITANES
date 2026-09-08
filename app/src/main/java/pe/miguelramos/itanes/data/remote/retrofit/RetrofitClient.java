package pe.miguelramos.itanes.data.remote.retrofit;

import pe.miguelramos.itanes.data.remote.api.ItanesApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // URL base conceptual
    private static final String BASE_URL = "https://raw.githubusercontent.com/miguelramosalarcon/ITANES/master/api/";
    private static Retrofit retrofit = null;

    public static ItanesApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ItanesApiService.class);
    }
}
