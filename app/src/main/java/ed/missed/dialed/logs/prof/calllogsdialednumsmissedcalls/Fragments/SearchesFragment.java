package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import me.everything.providers.android.browser.BrowserProvider;
import me.everything.providers.android.browser.Search;
import me.everything.providers.core.Data;

/**
 * Created by sromku
 */
public class SearchesFragment extends RecycleViewCursorFragment<Search> {

    @Override
    protected String getTitle() {
        return "Searches";
    }

    @Override
    protected void bindEntity(Search search, TextView title, TextView details) {
        title.setText(search.search);
        details.setText(search.date + "");
    }

    @Override
    protected GetCursorTask.DataFetcher<Search> getFetcher() {
        return new GetCursorTask.DataFetcher<Search>() {
            @Override
            public Data<Search> getData() {
                BrowserProvider provider = new BrowserProvider(getApplicationContext());
                return provider.getSearches();
            }
        };
    }


}
