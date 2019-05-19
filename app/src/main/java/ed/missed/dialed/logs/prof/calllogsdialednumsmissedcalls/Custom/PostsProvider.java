package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Custom;

import android.content.Context;

import me.everything.providers.core.AbstractProvider;
import me.everything.providers.core.Data;

/**
 * Created by Prof-Mohamed Atef on 18/05/2019.
 */

public class PostsProvider extends AbstractProvider {

    public PostsProvider(Context context) {
        super(context);
    }

    /**
     * Get all posts
     */
    public Data<Post> getPosts() {
        Data<Post> posts = getContentTableData(Post.uri, Post.class);
        return posts;
    }
}