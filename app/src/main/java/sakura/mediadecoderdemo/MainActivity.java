package sakura.mediadecoderdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private long time = 1;
    private long nowtime = 1;

    private HashMap<Long, Bitmap> datas;
    private MediaDecoder mediaDecoder;

    private long l;
    private MediaMetadataRetriever mmr1;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView img = (ImageView) findViewById(R.id.img);
        mediaDecoder = new MediaDecoder();
        mmr1 = new MediaMetadataRetriever();
        mmr1.setDataSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/wilddog/wilddog_test.mp4");
        String duration = mmr1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        l = Long.parseLong(duration);
        executorService = Executors.newFixedThreadPool(128);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                datas = mediaDecoder.getMediaDecoder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/wilddog/wilddog_test.mp4", time);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img.setBackground(new BitmapDrawable(datas.get(nowtime)));
                        time = time + 1000;
                    }
                });
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowtime = nowtime + 100;
                if (l < nowtime) {
                    nowtime = nowtime - 100;
                }
                Log.e("MainActivity", "nowtime:" + nowtime);
                Bitmap bitmap = datas.get(nowtime);
                if (bitmap == null) {
                    return;
                }
                img.setBackground(new BitmapDrawable(datas.get(nowtime)));

                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        time = time + (100 * 5);
                        datas = mediaDecoder.getMediaDecoderNext(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/wilddog/wilddog_test.mp4", time);
                    }
                });
            }
        });
    }
}
