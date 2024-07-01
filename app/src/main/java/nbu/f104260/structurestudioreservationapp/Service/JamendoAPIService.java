package nbu.f104260.structurestudioreservationapp.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JamendoAPIService {
    @GET("tracks/")
    Call<JamendoResponse> getTracks(
            @Query("client_id") String clientId,
            @Query("format") String format,
            @Query("limit") int limit,
            @Query("fuzzytags") String fuzzyTags,
            @Query("speed") String speed,
            @Query("include") String include,
            @Query("groupby") String groupBy
    );
}
