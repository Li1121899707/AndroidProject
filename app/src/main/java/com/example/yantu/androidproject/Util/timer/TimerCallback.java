package com.example.yantu.androidproject.Util.timer;

/**
 * A callback interface used by timer
 *
 * @author Ramen (lxdlam@gmail.com)
 * @date 2019.04.26
 */
public interface TimerCallback {
    /**
     * A function needs to be implemented
     * the remaining time will be passed
     *
     * @param hours   the remaining hours when it has been invoked
     * @param minutes the remaining minutes when it has been invoked
     * @param seconds the remaining seconds when it has been invoked
     */
    void run(long hours, long minutes, long seconds);
}
