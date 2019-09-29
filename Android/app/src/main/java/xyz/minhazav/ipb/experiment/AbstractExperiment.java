package xyz.minhazav.ipb.experiment;

import android.os.AsyncTask;

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
    }
}
