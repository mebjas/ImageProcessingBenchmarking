package xyz.minhazav.ipb;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import xyz.minhazav.ipb.data.ImageData;

public class Dashboard extends AppCompatActivity {

    private static final String TAG = Dashboard.class.getSimpleName();
    private static final int DEFAULT_ITERATIONS_VALUE = 10;
    private static final int ITERATIONS_MAX_VALUE = 50;

    private final List<View> listOfDisabledViews = new ArrayList<>();

    private TextView logContainerTextView;
    private ScrollView logScrollView;
    private Button testYuvToRgbPureJavaButton;
    private Button testYuvToRgbJniYuvlibButton;
    private Button testYuvToRgbJniOpencvButton;
    private Button testYuvToRgbRenderscriptButton;
    private EditText iterationsEditText;

    private boolean frameCaptured = false;
    private int iterations = DEFAULT_ITERATIONS_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logContainerTextView = findViewById(R.id.log_container);
        logScrollView = findViewById(R.id.log_section);
        testYuvToRgbPureJavaButton = findViewById(R.id.button_yuv_to_rgb_java_pure_java);
        testYuvToRgbJniYuvlibButton = findViewById(R.id.button_yuv_to_rgb_java_jni_libyuv);
        testYuvToRgbJniOpencvButton = findViewById(R.id.button_yuv_to_rgb_java_jni_opencv);
        testYuvToRgbRenderscriptButton = findViewById(R.id.button_yuv_to_rgb_java_renderscript);
        iterationsEditText = findViewById(R.id.edittext_iterations);
        listOfDisabledViews.add(testYuvToRgbPureJavaButton);
        listOfDisabledViews.add(testYuvToRgbJniYuvlibButton);
        listOfDisabledViews.add(testYuvToRgbJniOpencvButton);
        listOfDisabledViews.add(testYuvToRgbRenderscriptButton);
        listOfDisabledViews.add(iterationsEditText);

        iterationsEditText.addTextChangedListener(new IterationsTextWatcher());
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
        Log.d(TAG, message);
    }

    private void updateButtonEnabled(boolean isEnabled) {
        if (!frameCaptured) {
            throw new IllegalStateException("frame not captured yet.");
        }

        for (View v: listOfDisabledViews) {
            v.setEnabled(isEnabled);
        }

        log(String.format(
                "Test controls: %s", isEnabled ? "Enabled" : "Disabled"));

    }

    private final class IterationsTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No op
        }

        @Override
        public void afterTextChanged(Editable s) {
            final int newIterationsValue = Integer.valueOf(s.toString());
            if (newIterationsValue <= 0 || newIterationsValue > ITERATIONS_MAX_VALUE) {
                Toast.makeText(Dashboard.this, "value out of bounds", Toast.LENGTH_LONG).show();
                iterationsEditText.setText(String.valueOf(DEFAULT_ITERATIONS_VALUE));
                return;
            }

            iterations = newIterationsValue;
        }
    }
}
