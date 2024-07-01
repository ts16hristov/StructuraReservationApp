package nbu.f104260.structurestudioreservationapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchMusicUrlService extends Service {
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchMusicUrl();
        return START_NOT_STICKY;
    }

    private void fetchMusicUrl() {
        JamendoAPIService apiService = RetrofitClient.getRetrofitInstance().create(JamendoAPIService.class);
        Call<JamendoResponse> call = apiService.getTracks("b9a953c6", "jsonpretty", 1, "groove+rock", "high+veryhigh", "musicinfo", "artist_id");

        call.enqueue(new Callback<JamendoResponse>() {
            @Override
            public void onResponse(Call<JamendoResponse> call, Response<JamendoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().results;
                    if (!tracks.isEmpty()) {
                        // Assuming the first track's audio URL is what you want
                        String audioUrl = tracks.get(0).audio;
                        if (audioUrl != null && !audioUrl.isEmpty()) {
                            Intent serviceIntent = new Intent(FetchMusicUrlService.this, BackgroundMusicService.class);
                            serviceIntent.putExtra("music_url", audioUrl);
                            startService(serviceIntent);
                        }
                    }
                } else {
                    // Log or handle the case where the response is not successful
                    Log.e("API Call", "Response not successful or empty data");
                }
            }

            @Override
            public void onFailure(Call<JamendoResponse> call, Throwable t) {
                // Log or handle the API failure case
                Log.e("API Call", "Failed to fetch data: " + t.getMessage());
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}
