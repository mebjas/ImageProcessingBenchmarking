package xyz.minhazav.ipb.experiment;

import xyz.minhazav.ipb.data.ImageData;

public interface Experiment {
    /**
     * Start the experiment.
     *
     * @param experimentConfig configuration.
     * @param callback callbacks for experiment.
     */
    void start(ExperimentConfig experimentConfig, Callback callback);

    class ExperimentConfig {
        private final String name;
        private final int iterations;

        public ExperimentConfig(String name, int iterations) {
            this.name = name;
            this.iterations = iterations;
        }

        public String getName() {
            return name;
        }

        public int getIterations() {
            return iterations;
        }

        public ImageData getImageData() {
            return ImageData.getInstance();
        }
    }

    interface Callback {
        void onNewLog(String message);
        void onSuccess();
        void onFailure(Exception ex);
    }
}
