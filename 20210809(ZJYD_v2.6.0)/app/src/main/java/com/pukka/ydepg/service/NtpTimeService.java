package com.pukka.ydepg.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.session.SessionService;
import com.pukka.ydepg.util.PlayUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class NtpTimeService extends Service {

  /**
   * 认证成功后根据Login接口返回的NTP主备地址依次访问，
   * 如果主NTP服务器连接失败或者10秒无响应，则访问备NTP服务器，
   * 如果连接备NTP服务器也失败或10秒无响应，则每隔5分钟后发起一次NTP同步直到同步成功；
   * 如果时间同步成功，则后续每隔1小时发起一次NTP同步。
   * 禁止客户端自行缩小以上定义的同步周期。
   */
  private static final Object lock = new Object();

  private Timer timer = new Timer("ntp timer");

  private Timer tryTimer = new Timer();

  private static final int PERIOD_10s = 10 * 1000;
  private static final int PERIOD_5m = 5 * 60 * 1000;
  private static final int PERIOD_1h = 60 * 60 * 1000;

  private static long lastNtpTime;

  private static long lastRequestTime = 0;

  private static final String TAG = "NtpTimeService";

  // If the task has been in scheduling
  private boolean isScheduled = false;

  private int tryTimes = 2;

  private TimerTask updateNtpTimeTask = new TimerTask() {

    public void run() {
      try {
        doGetNtpTime();
      } catch (Exception e) {
        SuperLog.error(TAG, e);
      }
    }
  };

  private CountDownTimer countDownTimer = new CountDownTimer(PERIOD_5m, 60 * 1000) {
    @Override public void onTick(long millisUntilFinished) {
      //SuperLog.debug(TAG, "[onTick] millisUntilFinished = " + millisUntilFinished);
    }

    @Override public void onFinish() {
      try {
        doGetNtpTime();
      } catch (Exception e) {
        SuperLog.error(TAG, "[onFinish] :" + e);
      }
    }
  };

  public void doGetNtpTime() throws ClassNotFoundException, InstantiationException,
          IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Class<?> clazz = Class.forName("android.net.SntpClient");
    Object sntpClient = clazz.newInstance();
    Method requestTime = clazz.getMethod("requestTime", String.class, int.class);
    boolean success = false;

    String ntpDomain = SessionService.getInstance().getSession().getNTPDomain();
    String ntpDomainBackup = SessionService.getInstance().getSession().getNTPDomainBackup();

    //SuperLog.info2SD(TAG, "ntpDomain =" + ntpDomain + " |ntpDomainBackup=" + ntpDomainBackup);
    if (!TextUtils.isEmpty(ntpDomain)) {
      success = (Boolean) requestTime.invoke(sntpClient, ntpDomain, PERIOD_10s);
      SuperLog.info2SD(TAG, "ntpDomain success = " + success);
    }
    //back up ntp address
    if (!success) {
      if (!TextUtils.isEmpty(ntpDomainBackup)) {
        success = (Boolean) requestTime.invoke(sntpClient, ntpDomainBackup, PERIOD_10s);
        SuperLog.info2SD(TAG, "[doGetNtpTime] ntpDomainBackup success = " + success);
      }
    }

    if (success) {
      tryTimer.cancel();
      Method getNtpTime = clazz.getMethod("getNtpTime");
      long ntpTime = (Long) getNtpTime.invoke(sntpClient);
      Method getNtpTimeReference = clazz.getMethod("getNtpTimeReference");
      long ntpTimeReference = (Long) getNtpTimeReference.invoke(sntpClient);
      synchronized (lock) {
        lastRequestTime = SystemClock.elapsedRealtime();
        lastNtpTime = ntpTime + lastRequestTime - ntpTimeReference;
        PlayUtil.setNtpTime(lastNtpTime);
        PlayUtil.setLastElapsedRealtime(lastRequestTime);
        SuperLog.info2SD(TAG, "[updateNtpTimeTask.run], lastNtpTime= "
            + lastNtpTime
            + ", "
            + "lastRequestTime="
            + lastRequestTime);
      }
    } else {
      if (countDownTimer != null) {
        countDownTimer.start();
      }
    }
  }

  /**
   * @return NTP time corrected in milliseconds, returns 0 errors
   */
  public static long queryNtpTime() {
    synchronized (lock) {
      if (lastNtpTime == 0) {
        SuperLog.debug(TAG, "queryNtpTime, lastNtpTime is 0, get system time");
        return System.currentTimeMillis();
      } else {

        long offsetTime = SystemClock.elapsedRealtime() - lastRequestTime;
        long currentNtpTime = lastNtpTime + offsetTime;
        return currentNtpTime;
      }
    }
  }

  private void init() {
    if (!isScheduled) {
      tryTimes = 0;
      SuperLog.debug(TAG, "start timertasks schedule...");
      timer.schedule(updateNtpTimeTask, 0, PERIOD_1h);
      isScheduled = true;
    }
  }

  @Override public IBinder onBind(Intent intent) {
    SuperLog.debug(TAG, "onBind");
    init();
    return new NtpBinder();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    if (null != intent) {
      super.onStartCommand(intent, flags, startId);
    }
    SuperLog.debug(TAG, "onStartCommand");
    init();
    return Service.START_NOT_STICKY;
  }

  @Override public void onDestroy() {
    SuperLog.debug(TAG, "onDestroy()");
    if (updateNtpTimeTask != null) {
      updateNtpTimeTask.cancel();
    }
    if (timer != null) {
      timer.cancel();
    }
    if (countDownTimer != null) {
      countDownTimer.cancel();
    }
    isScheduled = false;
    super.onDestroy();
  }

  public class NtpBinder extends Binder {
    /**
     *  * Get the current instance of Service
     *
     * @return NtpTimeService ServiceExamples
     */
    public NtpTimeService getService() {
      return NtpTimeService.this;
    }
  }
}
