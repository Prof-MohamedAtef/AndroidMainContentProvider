package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import me.everything.providers.android.telephony.*;
import me.everything.providers.android.telephony.Thread;
import me.everything.providers.core.Data;

/**
 * Created by sromku
 */
public class ThreadsFragment extends RecycleViewCursorFragment<Thread> {

    @Override
    protected String getTitle() {
        return "Threads";
    }

    @Override
    protected void bindEntity(me.everything.providers.android.telephony.Thread thread, TextView title, TextView details) {
        title.setText(thread.snippet);
        details.setText("Num of messages: " + thread.messageCount);
    }

    @Override
    protected GetCursorTask.DataFetcher<me.everything.providers.android.telephony.Thread> getFetcher() {
        return new GetCursorTask.DataFetcher<me.everything.providers.android.telephony.Thread>() {
            @Override
            public Data<me.everything.providers.android.telephony.Thread> getData() {
                TelephonyProvider telephonyProvider = new TelephonyProvider(getApplicationContext());
                return telephonyProvider.getThreads();
            }
        };
    }


}
