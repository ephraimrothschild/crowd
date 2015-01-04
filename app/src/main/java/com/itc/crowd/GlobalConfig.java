package com.itc.crowd;

import com.spotify.sdk.android.playback.Config;

public final class GlobalConfig {
    private static Config _playerConfig;

    public static void setPlayerConfig(Config playerConfig) {
        _playerConfig = playerConfig;
    }

    public static Config getPlayerConfig() {
        return _playerConfig;
    }
}
