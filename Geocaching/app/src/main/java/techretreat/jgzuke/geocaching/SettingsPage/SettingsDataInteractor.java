package techretreat.jgzuke.geocaching.SettingsPage;

import android.content.Context;

public class SettingsDataInteractor {

    private Context context;

    private DataReceiver reciever;
    public interface DataReceiver {
    }

    public SettingsDataInteractor(Context context, DataReceiver reciever) {
        this.context = context;
        this.reciever = reciever;
    }
}