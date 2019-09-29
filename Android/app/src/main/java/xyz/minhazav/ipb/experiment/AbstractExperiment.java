package xyz.minhazav.ipb.experiment;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;

public abstract class AbstractExperiment implements Experiment {

    protected ExperimentConfig experimentConfig;
    protected Callback callback;

    @Override
    public void start(ExperimentConfig experimentConfig, Callback callback) {
        this.experimentConfig = experimentConfig;
        this.callback = callback;
    }

    protected void runInternal(Runnable runnable) {
        AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
        asyncTaskRunner.execute(new Runnable[] {runnable});
    }

    private class AsyncTaskRunner extends AsyncTask<Runnable, String, String> {
        private final List<Long> timeValues = new ArrayList<>();

        @Nullable Exception failedWithException;

        @Override
        protected String doInBackground(Runnable... runnables) {
            try {
                Runnable runnable = runnables[0];
                for (int i = 0; i < experimentConfig.getIterations(); i++) {
                    final String progressPrefix =  String.format(
                            "Experiment: %s, Iteration: %d ", experimentConfig.getName(), i);
                    publishProgress(progressPrefix +"Started.");

                    final long startTime = System.currentTimeMillis();
                    runnable.run();
                    final long timeToRun = System.currentTimeMillis() - startTime;
                    timeValues.add(timeToRun);
                    publishProgress(progressPrefix +
                            String.format("Finished: %d ms.", timeToRun));
                }
            } catch (Exception ex) {
                failedWithException = ex;
                return null;
            }

            return experimentConfig.getName() +" Complete.";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            computeStatistics();
            if (failedWithException != null) {
                callback.onFailure(failedWithException);
            } else {
                callback.onSuccess();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            callback.onNewLog(values[0]);
        }

        private void computeStatistics() {
            DoubleSummaryStatistics statistics = timeValues.stream()
                    .collect(Collectors.summarizingDouble(a -> Double.valueOf(a)));

            callback.onNewLog("Summary");
            callback.onNewLog("--------------------------------");
            callback.onNewLog(String.format("Count: %d", statistics.getCount()));
            callback.onNewLog(String.format("Avg: %.3f ms", statistics.getAverage()));
            callback.onNewLog(String.format("Max: %.3f ms", statistics.getMax()));
            callback.onNewLog(String.format("Min: %.3f ms", statistics.getMin()));
            callback.onNewLog("--------------------------------");
        }
    }
}
