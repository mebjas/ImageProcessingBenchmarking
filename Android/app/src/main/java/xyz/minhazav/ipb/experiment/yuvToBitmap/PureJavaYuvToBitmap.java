package xyz.minhazav.ipb.experiment.yuvToBitmap;

import xyz.minhazav.ipb.experiment.AbstractExperiment;

public class PureJavaYuvToBitmap extends AbstractExperiment {

    @Override
    public void start(ExperimentConfig experimentConfig, Callback callback) {
        super.start(experimentConfig, callback);
        super.runInternal(new Runnable() {
            @Override
            public void run() {
                logic();
            }
        });
    }

    private void logic() {
        try {
            // TODO(mebjas) - add the correct logic here.
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            callback.onNewLog("Interrupted Exception occured.");
        }
    }
}
