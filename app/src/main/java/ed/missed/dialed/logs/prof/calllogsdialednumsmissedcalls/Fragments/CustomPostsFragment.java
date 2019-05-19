package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetEntitiesTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewListFragment;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Custom.Post;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Custom.PostsProvider;
import me.everything.providers.core.Data;

public class CustomPostsFragment extends RecycleViewListFragment<Post> {

    @Override
    protected String getTitle() {
        return "Custom - Posts";
    }

    @Override
    protected void bindEntity(Post post, TextView title, TextView details) {
        title.setText(post.title);
        details.setText("Id: " + post.id);
    }

    @Override
    protected GetEntitiesTask.DataFetcher<Post> getFetcher() {
        return new GetEntitiesTask.DataFetcher<Post>() {
            @Override
            public Data<Post> getData() {
                PostsProvider provider = new PostsProvider(getApplicationContext());
                return provider.getPosts();
            }
        };
    }

}
