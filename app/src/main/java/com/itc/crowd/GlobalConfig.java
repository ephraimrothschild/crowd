package com.itc.crowd;

import com.spotify.sdk.android.playback.Config;

public final class GlobalConfig {
    private static GlobalConfig _globalConfigInstance;
    private Config _playerConfig;

    private GlobalConfig()
    {
        // Hide default constructor
    }

    public static GlobalConfig getInstance()
    {
        if(_globalConfigInstance == null)
            _globalConfigInstance = new GlobalConfig();
        return _globalConfigInstance;
    }

    public void setPlayerConfig(Config playerConfig) {
        _playerConfig = playerConfig;
    }

    public Config getPlayerConfig() {
        return _playerConfig;
    }
}
