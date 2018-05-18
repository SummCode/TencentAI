package com.summ.certificaterecognition;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.summ.imageselector.entity.ImageSelectorIntentData;
import com.summ.network.response.body.ResponseResult;
import com.summ.tencentai.entity.param.TencentAiASRParam;
import com.summ.tencentai.entity.param.TencentAiBusinessLicenseParam;
import com.summ.tencentai.entity.param.TencentAiCreditCardParam;
import com.summ.tencentai.entity.param.TencentAiIdentityCardParam;
import com.summ.tencentai.entity.param.TencentAiVehicleLicenseParam;
import com.summ.tencentai.entity.result.TencentAiASRResult;
import com.summ.tencentai.entity.result.TencentAiBusinessLicenseResult;
import com.summ.tencentai.entity.result.TencentAiCreditCardResult;
import com.summ.tencentai.entity.result.TencentAiIdentityCardResult;
import com.summ.tencentai.entity.result.TencentAiPrint;
import com.summ.tencentai.entity.result.TencentAiVehicleLicenseResult;
import com.summ.tencentai.image.ImageCompress;
import com.summ.tencentai.mvp.presenter.TencentAiPersenter;
import com.summ.tencentai.mvp.view.ITencentAiView;
import com.summ.tencentai.record.Recorder;
import com.summ.tool.Base64Utils;
import com.summ.tool.FileUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ITencentAiView {

    public final static String TAG = "Summ";

    private final static int APP_ID = 1106852268;
    private final static String APP_KEY = "FRFtla1WoLP7E6DF";
    public static final int REQUEST_CODE_CAMERA = 1000;
    public static final int REQUEST_CODE_PICTURE = 1001;
    public static final int ID_CARD_RESULT_CODE = 10001;
    public static final int VEHICLE_LICENSE_RESULT_CODE = 10002;
    public static final int DRIVE_LICENSE_RESULT_CODE = 10003;
    public static final int BUSINESS_LICENSE_RESULT_CODE = 10004;
    public static final int CREDIT_CARD_RESULT_CODE = 10005;


    private Recorder mRecorder;
    private Button btnStartRecord;
    private Button btnStopRecord;
    private TextView tvShowResult;
    private RadioGroup rgPraseType;
    private RadioButton rbPrase1;
    private RadioButton rbPrase2;
    private RadioButton rbPrase3;
    private RadioButton rbPrase4;
    private RadioButton rbPrase5;
    private Button btnTakeCamera;
    private Button btnTakePicture;

    private TencentAiPersenter mPersenter;
    private int mPraseType = ID_CARD_RESULT_CODE;

    private File mFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnTakePicture = (Button) findViewById(R.id.app_btn_take_picture);
        this.btnTakeCamera = (Button) findViewById(R.id.app_btn_take_camera);
        this.btnStopRecord = (Button) findViewById(R.id.app_btn_stop_record);
        this.btnStartRecord = (Button) findViewById(R.id.app_btn_start_record);
        this.tvShowResult = (TextView) findViewById(R.id.app_tv_show_result);
        this.rgPraseType = (RadioGroup) findViewById(R.id.app_rg_prase_type);
        this.rbPrase1 = (RadioButton) findViewById(R.id.app_rb_prase_1);
        this.rbPrase2 = (RadioButton) findViewById(R.id.app_rb_prase_2);
        this.rbPrase3 = (RadioButton) findViewById(R.id.app_rb_prase_3);
        this.rbPrase4 = (RadioButton) findViewById(R.id.app_rb_prase_4);
        this.rbPrase5 = (RadioButton) findViewById(R.id.app_rb_prase_5);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            requestPermissions(permissions, 1);
        }

        mRecorder = new Recorder();
        mPersenter = new TencentAiPersenter(MainActivity.this);

        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowResult.setText("录音…");
                mRecorder.startRecord();
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowResult.setText("识别…");
                File file = mRecorder.stopRecord();
                if (file == null) {
                    Toast.makeText(MainActivity.this, "音频文件出错", Toast.LENGTH_LONG).show();
                }
                file.getParentFile().mkdirs();
                String fileStr = Base64Utils.encodeBase64File(file);
                TencentAiASRParam tencentAIASRParam = new TencentAiASRParam(APP_ID, 3, fileStr);
                mPersenter.onParseVoice(tencentAIASRParam, APP_KEY);
            }
        });

        rgPraseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tvShowResult.setText("");
                switch (checkedId) {
                    case R.id.app_rb_prase_1:
                        mPraseType = ID_CARD_RESULT_CODE;
                        break;
                    case R.id.app_rb_prase_2:
                        mPraseType = VEHICLE_LICENSE_RESULT_CODE;
                        break;
                    case R.id.app_rb_prase_3:
                        mPraseType = DRIVE_LICENSE_RESULT_CODE;
                        break;
                    case R.id.app_rb_prase_4:
                        mPraseType = BUSINESS_LICENSE_RESULT_CODE;
                        break;
                    case R.id.app_rb_prase_5:
                        mPraseType = CREDIT_CARD_RESULT_CODE;
                        break;
                    default:
                }
            }
        });

        btnTakeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowResult.setText("");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/" + System.currentTimeMillis() + ".jpg");
                mFile.getParentFile().mkdirs();

                Uri uri = FileUtils.getUriForFile(v.getContext(), mFile, getPackageName() + ".fileprovider");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvShowResult.setText("");
                ImageSelectorIntentData.setIntentData(MainActivity.this, new ImageSelectorIntentData(1, 0, null));
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        tvShowResult.setText("识别…");

        File file = null;
        if (requestCode == REQUEST_CODE_CAMERA) {
            file = ImageCompress.getInstance().compress(mFile.getAbsolutePath());
        } else {
            ImageSelectorIntentData imageSelector = ImageSelectorIntentData.getIntentData(data);

            if (imageSelector != null) {
                String filePath = imageSelector.getCheckedImages().get(0).getPath();
                file = ImageCompress.getInstance().compress(filePath);
            }

        }

        if (file == null) {
            return;
        }

        file = ImageCompress.getInstance().compress(file.getAbsolutePath());

        String fileStr = Base64Utils.encodeBase64File(file);

        switch (mPraseType) {
            case ID_CARD_RESULT_CODE:
                TencentAiIdentityCardParam tencentAiIdentityCardParam = new TencentAiIdentityCardParam(APP_ID, fileStr, 0);
                mPersenter.onParseIdentityCard(tencentAiIdentityCardParam, APP_KEY);
                break;
            case VEHICLE_LICENSE_RESULT_CODE:
            case DRIVE_LICENSE_RESULT_CODE:
                TencentAiVehicleLicenseParam tencentAiVehicleLicenseParam = new TencentAiVehicleLicenseParam(APP_ID, fileStr, 0);
                mPersenter.onParseVehicleLicense(tencentAiVehicleLicenseParam, APP_KEY);
                break;
            case BUSINESS_LICENSE_RESULT_CODE:
                TencentAiBusinessLicenseParam tencentAiBusinessLicenseParam = new TencentAiBusinessLicenseParam(APP_ID, fileStr);
                mPersenter.onParseBusinessLicense(tencentAiBusinessLicenseParam, APP_KEY);
                break;
            case CREDIT_CARD_RESULT_CODE:
                TencentAiCreditCardParam tencentAiCreditCardParam = new TencentAiCreditCardParam(APP_ID, fileStr);
                mPersenter.onParseCreditCard(tencentAiCreditCardParam, APP_KEY);
                break;
            default:
        }
    }


    @Override
    public void parseFailedResult(ResponseResult result) {

        if (result.getCode() < 0) {
            tvShowResult.setText("系统繁忙");
        } else {
            tvShowResult.setText("识别失败，请重试");
        }

    }

    @Override
    public void parseVoiceResult(TencentAiASRResult result) {
        tvShowResult.setText(result.getText());
    }

    @Override
    public void parseIdcardocrResult(TencentAiIdentityCardResult result) {
        tvShowResult.setText(result.onShowContent());
    }

    @Override
    public void parseVehicleLicense(List<TencentAiVehicleLicenseResult> list) {
        tvShowResult.setText(getString(list));
    }

    @Override
    public void parseBusinessLicense(List<TencentAiBusinessLicenseResult> list) {
        tvShowResult.setText(getString(list));
    }

    @Override
    public void parseCreditCard(List<TencentAiCreditCardResult> list) {
        tvShowResult.setText(getString(list));
    }

    private String getString(List<? extends TencentAiPrint> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (TencentAiPrint item : list) {
            stringBuilder.append(item.onShowContent());
        }
        return stringBuilder.toString();
    }
}
