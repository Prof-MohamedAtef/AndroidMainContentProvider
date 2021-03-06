package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetEntitiesTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewListFragment;
import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.core.Data;

public class CalendarsFragment extends RecycleViewListFragment<Calendar> {

    @Override
    protected String getTitle() {
        return "Calendar";
    }

    @Override
    protected void bindEntity(Calendar calendar, TextView title, TextView details) {
        title.setText(calendar.displayName);
        details.setText(calendar.ownerAccount);
    }

    @Override
    protected GetEntitiesTask.DataFetcher<Calendar> getFetcher() {
        return new GetEntitiesTask.DataFetcher<Calendar>() {
            @Override
            public Data<Calendar> getData() {
                CalendarProvider calendarProvider = new CalendarProvider(getApplicationContext());
                return calendarProvider.getCalendars();
            }
        };
    }

    @Override
    protected CharSequence[] getDialogItems() {
        CharSequence[] items = new CharSequence[] {
                "Show Calendar Events"
        };
        return items;
    }

    @Override
    protected void onDialogItemSelected(Calendar entity, int which) {
        switch (which) {
            case 0: // "Show Calendar Events"
                setFragment(EventsFragment.newInstance(entity.id));
                break;
        }
    }
}
