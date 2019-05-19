package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import me.everything.providers.android.telephony.Conversation;
import me.everything.providers.android.telephony.TelephonyProvider;
import me.everything.providers.core.Data;

/**
 * Created by sromku
 */
public class ConversationsFragment extends RecycleViewCursorFragment<Conversation> {

    @Override
    protected String getTitle() {
        return "Conversations";
    }

    @Override
    protected void bindEntity(Conversation conversation, TextView title, TextView details) {
        title.setText(conversation.snippet);
        details.setText("Num of messages: " + conversation.messageCount);
    }


    @Override
    protected GetCursorTask.DataFetcher<Conversation> getFetcher() {
        return new GetCursorTask.DataFetcher<Conversation>() {
            @Override
            public Data<Conversation> getData() {
                TelephonyProvider telephonyProvider = new TelephonyProvider(getApplicationContext());
                return telephonyProvider.getConversations();
            }
        };
    }


}
