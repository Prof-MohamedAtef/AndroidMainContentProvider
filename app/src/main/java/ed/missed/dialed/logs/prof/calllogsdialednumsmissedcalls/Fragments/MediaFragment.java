package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.os.Bundle;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import me.everything.providers.core.Entity;


/**
 * Created by sromku
 */
public abstract class MediaFragment<T extends Entity> extends RecycleViewCursorFragment<T> {

    public static final int MEDIA_EXTERNAL = 1;

    protected boolean isExternal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        isExternal = arguments != null && arguments.getInt("param") == MEDIA_EXTERNAL;
    }
}
