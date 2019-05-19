package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import me.everything.providers.android.media.Audio;
import me.everything.providers.android.media.MediaProvider;
import me.everything.providers.core.Data;

/**
 * Created by sromku
 */
public class AudiosFragment extends MediaFragment<Audio> {

    @Override
    protected String getTitle() {
        return "Audios";
    }

    @Override
    protected void bindEntity(Audio audio, TextView title, TextView details) {
        title.setText(audio.title);
        details.setText(audio.mimeType);
    }



    @Override
    protected GetCursorTask.DataFetcher<Audio> getFetcher() {
        return new GetCursorTask.DataFetcher<Audio>() {
            @Override
            public Data<Audio> getData() {
                MediaProvider provider = new MediaProvider(getApplicationContext());
                return provider.getAudios(isExternal ?
                        MediaProvider.Storage.EXTERNAL :
                        MediaProvider.Storage.INTERNAL);
            }
        };
    }


}
