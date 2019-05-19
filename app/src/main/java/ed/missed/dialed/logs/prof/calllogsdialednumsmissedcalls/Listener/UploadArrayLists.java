package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Listener;

import java.util.ArrayList;
import java.util.List;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Entity.DialsEntity;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Prof-Mohamed Atef on 18/05/2019.
 */

public interface UploadArrayLists {
    @Multipart
    @POST("./")
    Call<String> uploadArrayLists(
            @Part List<MultipartBody.Part> numsList);
}
