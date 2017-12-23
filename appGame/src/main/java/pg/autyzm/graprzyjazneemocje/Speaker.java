package pg.autyzm.graprzyjazneemocje;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Paulina on 2015-03-08.
 */
public class Speaker {
    private final TextToSpeech textToSpeech;
    private final Context currentContext;
    private static Speaker instance;

    public Speaker(Context context) {
        this.currentContext = context;

        textToSpeech = new TextToSpeech(currentContext, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });
    }

    public static Speaker getInstance(Context context) {
        if (instance == null) {
            instance = new Speaker(context);
        }

        return instance;
    }

    public void close(){
        if(textToSpeech != null) {

            textToSpeech.stop();
            textToSpeech.shutdown();

            instance = null;
        }
    }

    public void speak(CharSequence text){
        textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);//TODO: change to not deprecated method!!!!
    }
}
