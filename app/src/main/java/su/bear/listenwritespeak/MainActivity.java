package su.bear.listenwritespeak;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //переменная для проверки возможности распознавания голоса в телефоне
    private static final int VR_REQUEST=999;

    //ListView для отображения распознанных слов
    private ListView wordList;

    //переменные для работы TTS
    //переменная для проверки данных для TTS
    private int MY_DATA_CHECK_CODE=0;

    //Text To Speech интерфейс
    private TextToSpeech repeatTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //вызов суперкласса
        super.onCreate(savedInstanceState);
        //установка контекста вывода
        setContentView(R.layout.activity_main);
        //переменные для работы с кнопкой и списком распознанных слов
        Button speechBtn = (Button) findViewById(R.id.speech_btn);
        wordList = (ListView) findViewById(R.id.word_list);
        //проверяем, поддерживается ли распознование речи
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> intActivities = packManager.queryIntentActivities(new
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if(intActivities.size()!=0)
        {
            // распознавание поддерживается, будем отслеживать событие щелчка по кнопке
            speechBtn.setOnClickListener((OnClickListener) this);
        }
        else
        {
            // распознавание не работает. Заблокируем кнопку и выведем соответствующее предупреждение.
            speechBtn.setEnabled(false);
            Toast.makeText(this,R.string.no_speech, Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.speech_btn)
        {
            // отслеживаем результат
            listenToSpeech();
        }
    }

    private void listenToSpeech()
    {
        //запускаем интент, распознающий речь и передаем ему требуемые данные
        Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //указываем пакет
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        //В процессе распознования выводим сообщение
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.say_word);
        //устанавливаем модель речи
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //указываем число результатов, которые могут быть получены
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        //начинаем прослушивание
        startActivityForResult(listenIntent, VR_REQUEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
