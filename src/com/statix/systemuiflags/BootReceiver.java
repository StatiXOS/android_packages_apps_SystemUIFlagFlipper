/*
 * Copyright (C) 2022 StatiXOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.statix.systemuiflags;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.android.systemui.flags.FlagManager;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "SystemUIFlagFlipper";

    @Override
    public void onReceive(Context context, Intent intent) {
        FlagManager manager = new FlagManager(context, new Handler(Looper.getMainLooper()));
        new Thread(() -> {
            Log.i(TAG, "Updating SystemUIFlags at boot");
            updateFlags(context, manager);
        }).start();
    }

    private void updateFlags(Context context, FlagManager flagManager) {
        updateConfig(context, R.array.flags, flagManager);
    }

    private void updateConfig(Context context, int configArray, FlagManager flagManager) {
        // Set current properties
        String[] rawProperties = context.getResources().getStringArray(configArray);
        for (String property : rawProperties) {
            // Format: namespace/key=value
            String[] kv = property.split("=");
            int id = Integer.parseInt(kv[0]);
            boolean enabled = Boolean.parseBoolean(kv[1]);
            Log.i(TAG, "Setting flag " + id + " to " + enabled);

            flagManager.setFlagValue(id, enabled);
        }
    }
}
