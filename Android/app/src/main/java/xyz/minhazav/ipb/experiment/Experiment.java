package xyz.minhazav.ipb.experiment;

import xyz.minhazav.ipb.data.ImageData;

public interface Experiment {
    /**
     * Prepare the experiment
     *
     * @param experimentConfig configuration.
     * @param logger Logger instance for logging.
     */
    void prepare(ExperimentConfig experimentConfig, Logger logger);

    /**
     * Start the experiment
     */
    void start();

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

    interface Logger {
        void log(String message);
    }
}
