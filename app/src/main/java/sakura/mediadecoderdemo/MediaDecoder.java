package sakura.mediadecoderdemo;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import wseemann.media.FFmpegMediaMetadataRetriever;

import static android.R.attr.duration;

/**
 * sakura.sakura_video_wd
 *
 * @author 赵磊
 * @date 2018/1/17
 * 功能描述：
 */
public class MediaDecoder {

    //当前
    private Bitmap frameAtTime;
    private MediaMetadataRetriever mmr1;
    private int width;
    private int height;
    private long l;
    private HashMap<Long, Bitmap> datas = new HashMap<>();
    private long aLong;

    public HashMap<Long, Bitmap> getMediaDecoder(String file, long time) {
        try {
            if (mmr1 == null) {
                mmr1 = new MediaMetadataRetriever();
                mmr1.setDataSource(file);
                String duration = mmr1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                l = Long.parseLong(duration);
            }
            for (int i = 0; i < 10; i++) {
                aLong = (time + (i * 100));
                Log.e("MainActivity", "time + (i * 100):" + aLong);
                frameAtTime = mmr1.getFrameAtTime(aLong * 1_000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                width = frameAtTime.getWidth();
                height = frameAtTime.getHeight();
                // 计算缩放比例
                Bitmap mbitmap = Bitmap.createBitmap(frameAtTime, 0, 0, width / 2, height);
                datas.put(aLong, mbitmap);
            }
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<Long, Bitmap> getMediaDecoderNext(String file, long time) {
        try {
            if (l < time) {
                return datas;
            }
            for (int i = 0; i < 5; i++) {
                aLong = (time + (i * 100));
                Log.e("MainActivity", "time + (i * 66):" + aLong);
                frameAtTime = mmr1.getFrameAtTime(aLong * 1_000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
                width = frameAtTime.getWidth();
                height = frameAtTime.getHeight();
                // 计算缩放比例
                Bitmap mbitmap = Bitmap.createBitmap(frameAtTime, 0, 0, width / 2, height);
                datas.put(aLong, mbitmap);
            }
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}