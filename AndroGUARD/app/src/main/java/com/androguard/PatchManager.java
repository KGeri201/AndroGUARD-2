package com.androguard;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;

import android.os.Handler;
import android.view.View;

import androidx.annotation.LayoutRes;

import com.a2p2.patch.OriginalMethods;
import com.a2p2.patch.lib.PatchClass;
import com.a2p2.patch.lib.PatchInstanceMethod;

import java.util.ArrayList;

/**
 * Class containing the patch to be applied to the Android
 * SensorManager class in order to obscure the builtin error.
 * @author  Gergö Kranz
 * @version 1.0
 * @since   03-08-2024
 */
public class PatchManager {
    private static final ArrayList<PatchListener> PATCH_MAPPINGS = new ArrayList<>();

    /**
     * Checks if a PatchListener is already associated with the SensorEventListener received.
     * Creates new one of not or binds additional sensors to the existing ones.
     * @param listener the original SensorEventListener instance.
     * @param sensor the Sensor the listener should receive values from.
     * @see SensorEventListener
     * @see Sensor
     * @see PatchListener
     * @return return custom PatchListener object containing the original listener.
     */
    private static SensorEventListener addListener(SensorEventListener listener, Sensor sensor) {
        PatchListener plistener = PATCH_MAPPINGS.stream().filter(pl -> pl.getListener().equals(listener)).findAny().orElse(new PatchListener(listener));

        plistener.sensors.add(sensor);

        PATCH_MAPPINGS.add(plistener);

        return plistener;
    }

    /**
     * Finds the PatchListener is already associated with the SensorEventListener
     * received and removes the sensor it is unregistering from.
     * Removes the PatchListener if no more Sensors are bound to it.
     * @param listener the original SensorEventListener object.
     * @param sensor the Sensor the listener should receive values from.
     * @see SensorEventListener
     * @see Sensor
     * @see PatchListener
     * @return return custom PatchListener object containing the original listener. If none found it returns the original listener
     */
    private static SensorEventListener removeListener(SensorEventListener listener, Sensor sensor) {
        PatchListener plistener = PATCH_MAPPINGS.stream().filter(pl -> pl.getListener().equals(listener)).findAny().orElse(null);

        if (plistener == null) return listener;

        if (sensor != null) plistener.sensors.remove(sensor);
        else plistener.sensors.clear();

        if (plistener.sensors.isEmpty()) return PATCH_MAPPINGS.remove(PATCH_MAPPINGS.indexOf(plistener));

        return plistener;
    }

    /**
     * Intercepts the function call to this function and
     * calls it with the custom PatchListener wrapper for the original.
     * @see SensorManager
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     * @return value from original registerListener method
     */
    @PatchClass("android.hardware.SensorManager")
    @PatchInstanceMethod
    public static boolean registerListener(SensorManager sm, SensorEventListener listener, Sensor sensor, int samplingPeriodUs) {
        return OriginalMethods.android_hardware_SensorManager.registerListener(sm, addListener(listener, sensor), sensor, samplingPeriodUs);
    }

    /**
     * Intercepts the function call to this function and
     * calls it with the custom PatchListener wrapper for the original.
     * @see SensorManager
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     * @return return value from original registerListener method
     */
    @PatchClass("android.hardware.SensorManager")
    @PatchInstanceMethod
    public static boolean registerListener(SensorManager sm, SensorEventListener listener, Sensor sensor, int samplingPeriodUs, int maxReportLatencyUs) {
        return OriginalMethods.android_hardware_SensorManager.registerListener(sm, addListener(listener, sensor), sensor, samplingPeriodUs, maxReportLatencyUs);
    }

    /**
     * Intercepts the function call to this function and
     * calls it with the custom PatchListener wrapper for the original.
     * @see SensorManager
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     * @return return value from original registerListener method
     */
    @PatchClass("android.hardware.SensorManager")
    @PatchInstanceMethod
    public static boolean registerListener(SensorManager sm, SensorEventListener listener, Sensor sensor, int samplingPeriodUs, Handler handler) {
        return OriginalMethods.android_hardware_SensorManager.registerListener(sm, addListener(listener, sensor), sensor, samplingPeriodUs, handler);
    }

    /**
     * Intercepts the function call to this function and
     * replaces the SensorEventListener object with the registered PatchListener
     * before calling the original function.
     * @see SensorManager
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     */
    @PatchClass("android.hardware.SensorManager")
    @PatchInstanceMethod
    public static void unregisterListener(SensorManager sm, SensorEventListener listener) {
        OriginalMethods.android_hardware_SensorManager.unregisterListener(sm, removeListener(listener, null));
    }

    /**
     * Intercepts the function call to this function and
     * replaces the SensorEventListener object with the registered PatchListener
     * before calling the original function.
     * @see SensorManager
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     */
    @PatchClass("android.hardware.SensorManager")
    @PatchInstanceMethod
    public static void unregisterListener(SensorManager sm, SensorEventListener listener, Sensor sensor) {
        OriginalMethods.android_hardware_SensorManager.unregisterListener(sm, removeListener(listener, sensor), sensor);
    }

//    /**
//     * ...
//     * @see OriginalMethods
//     * @see PatchClass
//     * @see PatchInstanceMethod
//     */
//    @PatchClass("android.app.Activity")
//    @PatchInstanceMethod
//    public static void setContentView(Activity thiz, View view, ViewGroup.LayoutParams params) {
//        OriginalMethods.android_app_Activity.setContentView(thiz, view, params);
//        Calibration.showDialog(thiz.getApplicationContext());
//    }

    /**
     * ...
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     */
    @PatchClass("android.app.Activity")
    @PatchInstanceMethod
    public static void setContentView(Activity thiz, View view) {
        OriginalMethods.android_app_Activity.setContentView(thiz, view);
        PatchCalibration.run(thiz);
    }

    /**
     * ...
     * @see OriginalMethods
     * @see PatchClass
     * @see PatchInstanceMethod
     */
    @PatchClass("android.app.Activity")
    @PatchInstanceMethod
    public static void setContentView(Activity thiz, @LayoutRes int layoutResID) {
        OriginalMethods.android_app_Activity.setContentView(thiz, layoutResID);
        PatchCalibration.run(thiz);
    }
}
