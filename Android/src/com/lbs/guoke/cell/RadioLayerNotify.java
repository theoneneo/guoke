package com.lbs.guoke.cell;

public interface RadioLayerNotify {
    public void onStateChange(RadioData rd, int signal);
    public void onStateChange(CdmaData cdmaData, int signal);
}
