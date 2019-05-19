package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Entity.DialsEntity;
import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.core.Data;


/**
 * Created by sromku.
 */
public class CallsFragment extends RecycleViewCursorFragment<Call> {

    private String[] mColumns = new String[] {
            CallLog.Calls.NUMBER,
            CallLog.Calls.TYPE
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Numbers=new ArrayList<DialsEntity>();
    }

    @Override
    protected String getTitle() {
        return "Calls";
    }

    ArrayList<DialsEntity> Numbers=null;
    DialsEntity dialsEntity;

    @Override
    protected void bindEntity(Call call, TextView title, TextView details) {
        title.setText(call.number);
        details.setText(call.type.toString());
    }

    @Override
    protected ArrayList<DialsEntity>  collectItems(Call entity) {
        dialsEntity=new DialsEntity(entity.number,entity.type.toString());
        Numbers.add(dialsEntity);
        return Numbers;
    }

    @Override
    protected String[] getProjectionColumns() {
        return mColumns;
    }

    @Override
    protected GetCursorTask.DataFetcher<Call> getFetcher() {
        return new GetCursorTask.DataFetcher<Call>() {
            @Override
            public Data<Call> getData() {
                CallsProvider callsProvider = new CallsProvider(getApplicationContext());
                return callsProvider.getCalls();
            }
        };
    }
}