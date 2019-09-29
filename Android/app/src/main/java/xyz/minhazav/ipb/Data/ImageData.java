package xyz.minhazav.ipb.Data;

import android.util.Log;
import java.nio.ByteBuffer;

import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;

/** Singleton {@link ImageProxy} container. */
public class ImageData {
    private static final String TAG = ImageData.class.getSimpleName();
    private static final ImageData ourInstance = new ImageData();

    @Nullable private byte[] imageArray;
    private int width;
    private int height;
    private int imageFormat;
    private long conversionTimeInMs;
    private boolean isSet = false;

    private ImageData() {
    }

    public static ImageData getInstance() {
        return ourInstance;
    }

    public void setImageProxy(ImageProxy imageProxy) {
        this.width = imageProxy.getWidth();
        this.height = imageProxy.getHeight();
        this.imageFormat = imageProxy.getFormat();

        final long startTime = System.currentTimeMillis();
        this.imageArray = convertImageProxyToByteArray(imageProxy);
        conversionTimeInMs = System.currentTimeMillis() - startTime;

        isSet = true;
    }

    public boolean getIsSet() {
        return isSet;
    }

    public long getConversionTimeInMs() {
        return conversionTimeInMs;
    }

    public byte[] getImageArray() {
        return imageArray;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getImageFormat() {
        return imageFormat;
    }

    private byte[] convertImageProxyToByteArray(ImageProxy imageProxy) {
        final ImageProxy.PlaneProxy[] planeList = imageProxy.getPlanes();
        ByteBuffer y_buffer = planeList[0].getBuffer();
        ByteBuffer u_buffer = planeList[1].getBuffer();
        ByteBuffer v_buffer = planeList[2].getBuffer();
        byte[] byteArr = new byte[
                y_buffer.capacity() + u_buffer.capacity() + v_buffer.capacity()];

        return convertYUV420ImageToPackedNV21(imageProxy, byteArr);
    }

    /**
     * Converts the YUV420_888 Image into a packed NV21 of a single byte array,
     * suitable for JPEG compression by the method convertNv21toJpeg. Creates a
     * memory block with the y component at the head and interleaves the u,v
     * components following the y component. Caller is responsible to allocate a
     * large enough buffer for results.
     *
     * @param img image to be converted
     * @param dataCopy buffer to write NV21 packed image
     * @return byte array of NV21 packed image
     */
    private byte[] convertYUV420ImageToPackedNV21(ImageProxy img, byte[] dataCopy) {
        // Get all the relevant information and then release the image.
        final int w = img.getWidth();
        final int h = img.getHeight();
        final ImageProxy.PlaneProxy[] planeList = img.getPlanes();
        final ByteBuffer y_buffer = planeList[0].getBuffer();
        final ByteBuffer u_buffer = planeList[1].getBuffer();
        final ByteBuffer v_buffer = planeList[2].getBuffer();
        final int color_pixel_stride = planeList[1].getPixelStride();
        final int y_size = y_buffer.capacity();
        final int u_size = u_buffer.capacity();
        final int data_offset = w * h;

        for (int i = 0; i < y_size; i++) {
            dataCopy[i] = (byte) (y_buffer.get(i) & 255);
        }

        for (int i = 0; i < u_size / color_pixel_stride; i++) {
            dataCopy[data_offset + 2 * i] = v_buffer.get(i * color_pixel_stride);
            dataCopy[data_offset + 2 * i + 1] = u_buffer.get(i * color_pixel_stride);
        }

        return dataCopy;
    }
}
