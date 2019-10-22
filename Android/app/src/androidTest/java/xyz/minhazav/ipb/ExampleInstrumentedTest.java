package xyz.minhazav.ipb;

import androidx.benchmark.BenchmarkState;
import androidx.benchmark.junit4.BenchmarkRule;
import androidx.test.runner.AndroidJUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {
    @Rule
    public BenchmarkRule benchmarkRule = new BenchmarkRule();

    @Test
    public void myBenchmark() {
        final BenchmarkState state = benchmarkRule.getState();
        while (state.keepRunning()) {
            int k = 0;
            for (int i = 0; i < 100000; i++) {
                int l = k;
                l = l + 1;
                k = l;
            }
        }
    }
}
