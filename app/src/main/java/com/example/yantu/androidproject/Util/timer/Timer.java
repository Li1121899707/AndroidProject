package com.example.yantu.androidproject.Util.timer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A simple timer which is a proxy to ScheduledExecutorService
 *
 * @author Ramen (lxdlam@gmail.com)
 * @date 2019.04.26
 * @see java.util.concurrent.ScheduledExecutorService
 */
public class Timer {
    /**
     * The time counter object
     */
    private TimeCounter counter;

    /**
     * The run counter object
     */
    private TimeCounter runCounter;

    /**
     * The executor object
     */
    private ScheduledExecutorService executor;

    /**
     * The callbacks
     * Will be invoked in the register order
     * For post callback, the time is meaningless
     */
    private List<TimerCallback> callbacks = new LinkedList<>();
    private TimerCallback postCallback, interruptCallback;

    /**
     * Default constructor
     */
    public Timer() {
        counter = new TimeCounter();
    }

    /**
     * Detailed constructor
     *
     * @param hours   hours should be delayed
     * @param minutes minutes should be delayed
     * @param seconds seconds shoulbe be delayed
     * @throws IllegalArgumentException if the arguments is invalid
     */
    public Timer(long hours, long minutes, long seconds) throws IllegalArgumentException {
        counter = new TimeCounter(hours, minutes, seconds);
    }

    /**
     * Build the executor and start the timer
     *
     * @return whether successfully started the timer, false if the timer has already running
     */
    public synchronized boolean start() {
        if (executor != null) return false;

        executor = new ScheduledThreadPoolExecutor(1);

        final Timer timer = this;
        runCounter = new TimeCounter(counter);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!runCounter.consume()) {
                    timer.stop(false);
                    return;
                }
                for (TimerCallback e : callbacks) {
                    e.run(runCounter.getHours(), runCounter.getMinutes(), runCounter.getSeconds());
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        return true;
    }

    /**
     * A delegate to private method stop.
     *
     * @return @see stop
     */
    public synchronized boolean stop() {
        return stop(true);
    }

    /**
     * Stop the timer
     * A stop event is either a expired event or interrupt event
     * For both events, call the corresponding callback
     *
     * @param interrupt represents the event type, true for interrupt event
     * @return whether successfully stopped the timer, false for failed
     */
    private synchronized boolean stop(boolean interrupt) {
        if (executor == null) return false;

        if (interrupt) {
            if (interruptCallback != null)
                interruptCallback.run(runCounter.getHours(), runCounter.getMinutes(), runCounter.getSeconds());
        } else {
            if (postCallback != null)
                postCallback.run(runCounter.getHours(), runCounter.getMinutes(), runCounter.getSeconds());
        }

        executor.shutdownNow();
        executor = null;
        runCounter = null;

        return true;
    }

    /**
     * Register sequenced callbacks
     *
     * @param e the callback object
     */
    public void registerCallback(TimerCallback e) {
        callbacks.add(e);
    }

    /**
     * Register post run callback
     *
     * @param e the post run callback object
     */
    public void registerPostCallback(TimerCallback e) {
        postCallback = e;
    }

    /**
     * Register interrupt callback
     *
     * @param e the interrupt callback object
     */
    public void registerInterruptCallback(TimerCallback e) {
        interruptCallback = e;
    }

    /**
     * Clear the callbacks
     */
    public void resetCallbacks() {
        callbacks.clear();
        postCallback = null;
        interruptCallback = null;
    }

    /**
     * Check if the timer is running
     *
     * @return if the timer is running
     */
    public boolean isRunning() {
        return executor != null;
    }
}
