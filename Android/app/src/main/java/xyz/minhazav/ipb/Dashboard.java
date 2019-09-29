package xyz.minhazav.ipb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import xyz.minhazav.ipb.Data.ImageData;

public class Dashboard extends AppCompatActivity {

    private final List<Button> listOfDisabledButtons = new ArrayList<>();

    private TextView logContainerTextView;
    private ScrollView logScrollView;
    private Button buttonCapturePicture;
    private Button testYuvToRgbPureJavaButton;
    private Button testYuvToRgbJniYuvlibButton;
    private Button testYuvToRgbJniOpencvButton;
    private Button testYuvToRgbRenderscriptButton;

    private boolean frameCaptured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logContainerTextView = findViewById(R.id.log_container);
        logScrollView = findViewById(R.id.log_section);
        buttonCapturePicture = findViewById(R.id.button_capture_picture);
        testYuvToRgbPureJavaButton = findViewById(R.id.button_yuv_to_rgb_java_pure_java);
        testYuvToRgbJniYuvlibButton = findViewById(R.id.button_yuv_to_rgb_java_jni_libyuv);
        testYuvToRgbJniOpencvButton = findViewById(R.id.button_yuv_to_rgb_java_jni_opencv);
        testYuvToRgbRenderscriptButton = findViewById(R.id.button_yuv_to_rgb_java_renderscript);
        listOfDisabledButtons.add(testYuvToRgbPureJavaButton);
        listOfDisabledButtons.add(testYuvToRgbJniYuvlibButton);
        listOfDisabledButtons.add(testYuvToRgbJniOpencvButton);
        listOfDisabledButtons.add(testYuvToRgbRenderscriptButton);

        log("Application Created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ImageData.getInstance().getIsSet()) {
            ImageData imageData = ImageData.getInstance();
            final int imageWidth = imageData.getWidth();
            final int imageHeight = imageData.getHeight();
            log("Image Frame Available.");
            log(String.format("Image Resolution: %d X %d", imageWidth, imageHeight));
            log(String.format(
                    "Size of array: %d", imageData.getImageArray().length));
            log(String.format("Conversion Time: %d ms", imageData.getConversionTimeInMs()));

            frameCaptured = true;
            updateButtonEnabled(true);
        }
    }

    public void onCaptureButtonClicked(View v) {
        log("Capture button clicked.");
        Intent openCaptureActivity = new Intent(this, CameraActivity.class);
        startActivity(openCaptureActivity);
    }

    public void onTestYuvToRgbPureJavaButtonClicked(View v) {
        log("Test Yuv To Rgb PureJava Button Clicked.");
    }

    public void onTestYuvToRgbJniYuvlibButtonClicked(View v) {
        log("Test Yuv To Rgb Jni Yuv lib Button Clicked.");
    }

    public void onTestYuvToRgbJniOpencvButtonClicked(View v) {
        log("Test Yuv To Rgb Jni OpenCV Button Clicked.");
    }

    public void onTestYuvToRgbRenderscriptButtonClicked(View v) {
        log("Test Yuv To Rgb Renderscript Button Clicked.");
    }

    public void log(String message) {
        logContainerTextView.append(message +"\n");
        logScrollView.post(new Runnable() {
            @Override
            public void run() {
                logScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void updateButtonEnabled(boolean isEnabled) {
        if (!frameCaptured) {
            throw new IllegalStateException("frame not captured yet.");
        }

        for (View v: listOfDisabledButtons) {
            v.setEnabled(isEnabled);
        }

        log(String.format(
                "Test controls: %s", isEnabled ? "Enabled" : "Disabled"));

    }
}
