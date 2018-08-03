import javax.swing.*;

public class TimeDigitalDisplay extends DigitalDisplay {
    private long startTime;
    private long currentTime;

    private TimerThread timerThread = new TimerThread();

    public TimeDigitalDisplay(JPanel refreshPanel, int displayPadding) {
        super(refreshPanel, displayPadding);
    }

    public void start() {
        startTime = System.currentTimeMillis();
        timerThread.start();
    }

    public void stop() {
        timerThread.setStopped(true);
        currentTime = System.currentTimeMillis();
        setValue((int)((currentTime - startTime) / 1000));
    }

    public void initRefresh() {
        timerThread.setStopped(true);
        startTime = 0;
        currentTime = 0;
        setValue(0);
        timerThread = new TimerThread();
    }

    public long getTime() {
        return (currentTime - startTime);
    }

    private class TimerThread extends Thread {
        private boolean isStopped = false;

        public TimerThread() {
            super();
            setDaemon(true);
        }

        @Override
        public void run() {
            while(!isStopped) {
                currentTime = System.currentTimeMillis();
                setValue((int)((currentTime - startTime) / 1000));
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {

                }
            }
        }

        public void setStopped(boolean isStopped) {
            this.isStopped = isStopped;
        }
    }
}
