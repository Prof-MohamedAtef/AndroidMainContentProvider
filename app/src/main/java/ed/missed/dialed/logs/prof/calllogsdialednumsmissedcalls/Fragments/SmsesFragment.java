package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;
import me.everything.providers.core.Data;

public class SmsesFragment extends RecycleViewCursorFragment<Sms> {

    @Override
    protected String getTitle() {
        return "SMS(es)";
    }

    @Override
    protected void bindEntity(Sms sms, TextView title, TextView details) {
        title.setText(sms.address);
        details.setText(sms.body);
    }

    @Override
    protected GetCursorTask.DataFetcher<Sms> getFetcher() {
        return new GetCursorTask.DataFetcher<Sms>() {
            @Override
            public Data<Sms> getData() {
                TelephonyProvider provider = new TelephonyProvider(getApplicationContext());
                return provider.getSms(TelephonyProvider.Filter.ALL);
            }
        };
    }

}
