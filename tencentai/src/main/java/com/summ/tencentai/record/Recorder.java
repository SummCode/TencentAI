package com.summ.tencentai.record;

import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ldm
 * @description 通过MediaRecorder录制声音(基于文件录音 ， 使用方便)并播放
 * @time 2017/2/9 9:02
 */
public class Recorder {

    //线程操作
    private ExecutorService mExecutorService;
    //录音API
    private MediaRecorder mMediaRecorder;
    //录音开始时间与结束时间
    private long startTime, endTime;
    //录音所保存的文件
    private File mAudioFile;
    //录音文件保存位置
    private String mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio/";
    //当前是否正在播放
    private volatile boolean isPlaying;
    //播放音频文件API
    private MediaPlayer mediaPlayer;

    public File getAudioFile() {
        this.stopRecord();
        return mAudioFile;
    }

    public Recorder() {
        //录音及播放要使用单线程操作
        this.mExecutorService = Executors.newSingleThreadExecutor();
    }

    /**
     * @description 开始进行录音
     */
    public void startRecord() {
        //异步任务执行录音操作
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                //播放前释放资源
                releaseRecorder();
                //执行录音操作
                recordOperation();
            }
        });
    }

    /**
     * @description 录音失败处理
     */
    private void recordFail() {
        deleteAudioFile(mAudioFile);
        mAudioFile = null;
    }

    /**
     * @description 录音操作
     */
    private void recordOperation() {
        //创建MediaRecorder对象
        mMediaRecorder = new MediaRecorder();
        //创建录音文件,.wav为PCM/WAVE音频标准的文件的扩展名
        mAudioFile = new File(mFilePath + System.currentTimeMillis() + ".arm");
        //创建父文件夹
        mAudioFile.getParentFile().mkdirs();
        try {
            //创建文件
            mAudioFile.createNewFile();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mMediaRecorder.setAudioSamplingRate(16000);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //设置编码频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            mMediaRecorder.setMaxDuration(1000 * 30);
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //记录开始录音时间
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
            recordFail();
        }
    }


    /**
     * @description 结束录音操作
     */
    public File stopRecord() {
        //停止录音
        mMediaRecorder.stop();
        //记录停止时间
        endTime = System.currentTimeMillis();
        //录音时间处理，比如只有大于2秒的录音才算成功
        int time = (int) ((endTime - startTime) / 1000);
        if (0 < time && time <= 30) {
            //录音成功,添加数据
            Log.i("Summ", "File path:" + mAudioFile.getAbsolutePath() + "  Size:" + mAudioFile.length());
        } else {
            deleteAudioFile(mAudioFile);
        }
        //录音完成释放资源
        releaseRecorder();
        return mAudioFile;
    }

    /**
     * 删除录音文件
     *
     * @param file
     */
    public void deleteAudioFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    /**
     * @description 翻放录音相关资源
     */
    private void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 关闭线程
     */
    public void onDestroy() {
        //页面销毁，线程要关闭
        mExecutorService.shutdownNow();
    }

    /**
     * @description 播放音频
     */
    public void playAudio(final File mFile) {
        if (null != mFile && !isPlaying) {
            isPlaying = true;
            mExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    startPlay(mFile);
                }
            });
        }
    }

    /**
     * @description 开始播放音频文件
     */
    private void startPlay(File file) {
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(file.getAbsolutePath());
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }
        Log.i("Summ", "" + file.length());
    }

    /**
     * @description 停止播放或播放失败处理
     * @author ldm
     * @time 2017/2/9 16:58
     */
    private void playEndOrFail(boolean isEnd) {
        isPlaying = false;


        if (isEnd) {

        } else {

        }


        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
