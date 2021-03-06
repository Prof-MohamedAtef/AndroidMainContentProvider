package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base;

import android.os.AsyncTask;

import java.util.List;

import me.everything.providers.core.Data;
import me.everything.providers.core.Entity;

/**
 * Created by Prof-Mohamed Atef on 18/05/2019.
 */

public class GetEntitiesTask <T extends Entity> extends AsyncTask<Void, Void, List<T>> {

    private TaskListener mTaskListener;
    private DataFetcher<T> mFetcher;

    @Override
    protected List<T> doInBackground(Void... params) {
        Data<T> data = mFetcher.getData();
        return data.getList();
    }

    @Override
    protected void onPostExecute(List<T> entities) {
        mTaskListener.onComplete(entities);
    }

    public interface DataFetcher<T extends Entity> {
        Data<T> getData();
    }

    public interface TaskListener<T> {
        void onComplete(List<T> entities);
    }

    public static class Builder<T extends Entity> {

        private DataFetcher<T> mFetcher;
        private TaskListener<T> mCallback;

        public Builder setFetcher(DataFetcher<T> fetcher) {
            mFetcher = fetcher;
            return this;
        }

        public Builder setCallback(TaskListener<T> callback) {
            mCallback = callback;
            return this;
        }

        public GetEntitiesTask<T> build() {
            GetEntitiesTask<T> getEntitiesTask = new GetEntitiesTask();
            getEntitiesTask.mTaskListener = mCallback;
            getEntitiesTask.mFetcher = mFetcher;
            return getEntitiesTask;
        }
    }
}