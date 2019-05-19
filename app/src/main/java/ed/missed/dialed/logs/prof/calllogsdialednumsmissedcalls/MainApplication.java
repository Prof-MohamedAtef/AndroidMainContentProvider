package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Custom.Post;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Custom.PostsProvider;
import me.everything.providers.core.Data;
import me.everything.providers.stetho.ProvidersStetho;

/**
 * Created by Prof-Mohamed Atef on 18/05/2019.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = this;
        ProvidersStetho providersStetho = new ProvidersStetho(context);
        providersStetho.enableDefaults();

        // register custom provider if you want - this is sample one
        providersStetho.registerProvider("provider-custom", "posts", new ProvidersStetho.QueryExecutor<Post>() {
            @Override
            public Data<Post> onQuery(String query) {
                PostsProvider provider = new PostsProvider(getApplicationContext());
                return provider.getPosts();
            }
        });

        // stetho init
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                        .enableWebKitInspector(providersStetho.defaultInspectorModulesProvider())
                        .build());
    }

}
