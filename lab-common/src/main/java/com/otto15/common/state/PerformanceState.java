package com.otto15.common.state;

import java.util.concurrent.atomic.AtomicBoolean;

public class PerformanceState {

    private final AtomicBoolean performanceStatus = new AtomicBoolean(true);

    public PerformanceState() {

    }

    public boolean getPerformanceStatus() {
        return performanceStatus.get();
    }


    public void switchPerformanceStatus() {
        performanceStatus.set(!performanceStatus.get());
    }

}
