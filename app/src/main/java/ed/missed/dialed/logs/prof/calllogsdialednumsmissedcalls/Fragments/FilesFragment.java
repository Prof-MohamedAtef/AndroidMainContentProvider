package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import me.everything.providers.android.media.File;
import me.everything.providers.android.media.MediaProvider;
import me.everything.providers.core.Data;


/**
 * Created by sromku
 */
public class FilesFragment extends MediaFragment<File> {

    @Override
    protected String getTitle() {
        return "Files";
    }

    @Override
    protected void bindEntity(File file, TextView title, TextView details) {
        title.setText(file.title);
        details.setText(file.mimeType);
    }



    @Override
    protected GetCursorTask.DataFetcher<File> getFetcher() {
        return new GetCursorTask.DataFetcher<File>() {
            @Override
            public Data<File> getData() {
                MediaProvider provider = new MediaProvider(getApplicationContext());
                return provider.getFiles( isExternal ?
                                MediaProvider.Storage.EXTERNAL :
                                MediaProvider.Storage.INTERNAL);
            }
        };
    }


}
