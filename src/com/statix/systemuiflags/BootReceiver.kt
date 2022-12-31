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

package com.statix.systemuiflags

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.android.systemui.flags.FlagManager

class BootReceiver: BroadcastReceiver() {
    companion object {
        private const val TAG = "SystemUIFlagFlipper"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val manager = FlagManager(context, Handler(Looper.getMainLooper()))
        Thread({
            Log.i(TAG, "Updating SystemUIFlags at boot")
            updateFlags(context, manager)
        }).start()
    }

    private fun updateFlags(context: Context, flagManager: FlagManager) {
        updateConfig(context, R.array.flags, flagManager)
    }

    private fun updateConfig(context: Context, configArray: Int, flagManager: FlagManager) {
        // Set current properties
        val rawProperties = context.resources.getStringArray(configArray)
        for (property: String in rawProperties) {
            // Format: namespace/key=value
            val kv = property.split("=")
            val id = kv[0].toInt()
            val enabled = kv[1].toBoolean()
            Log.i(TAG, "Setting flag ${id} to ${enabled}")
            flagManager.setFlagValue(id, enabled)
        }
    }
}
