package ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Fragments;

import android.widget.TextView;

import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.GetCursorTask;
import ed.missed.dialed.logs.prof.calllogsdialednumsmissedcalls.Base.RecycleViewCursorFragment;
import me.everything.providers.android.dictionary.DictionaryProvider;
import me.everything.providers.android.dictionary.Word;
import me.everything.providers.core.Data;

/**
 * Created by sromku
 */
public class WordsFragment extends RecycleViewCursorFragment<Word> {

    @Override
    protected String getTitle() {
        return "Words";
    }

    @Override
    protected void bindEntity(Word word, TextView title, TextView details) {
        title.setText(word.word);
        details.setText(word.locale);
    }

    @Override
    protected GetCursorTask.DataFetcher<Word> getFetcher() {
        return new GetCursorTask.DataFetcher<Word>() {
            @Override
            public Data<Word> getData() {
                DictionaryProvider provider = new DictionaryProvider(getApplicationContext());
                return provider.getWords();
            }
        };
    }


}
