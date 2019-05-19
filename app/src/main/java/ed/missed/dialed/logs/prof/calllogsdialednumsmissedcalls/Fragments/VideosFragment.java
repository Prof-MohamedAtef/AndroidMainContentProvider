package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import me.everything.providers.android.media.MediaProvider;
import me.everything.providers.android.media.Video;
import me.everything.providers.core.Data;

/**
 * Created by sromku
 */
public class VideosFragment extends MediaFragment<Video> {

    @Override
    protected String getTitle() {
        return "Videos";
    }

    @Override
    protected void bindEntity(Video video, TextView title, TextView details) {
        title.setText(video.title);
        details.setText(video.mimeType);
    }

    @Override
    protected GetCursorTask.DataFetcher<Video> getFetcher() {
        return new GetCursorTask.DataFetcher<Video>() {
            @Override
            public Data<Video> getData() {
                MediaProvider provider = new MediaProvider(getApplicationContext());
                return provider.getVideos(isExternal ?
                        MediaProvider.Storage.EXTERNAL :
                        MediaProvider.Storage.INTERNAL);
            }
        };
    }


}
