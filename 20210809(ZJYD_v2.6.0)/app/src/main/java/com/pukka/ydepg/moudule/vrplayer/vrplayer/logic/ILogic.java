package com.pukka.ydepg.moudule.vrplayer.vrplayer.logic;

import android.content.Context;
import android.os.Message;

/**
 * 功能描述
 *
 * @author l00477311
 * @since 2020-08-06
 */
public interface ILogic {
    void init(Context context, Object object);

    void sendMessage(Message message);

    void sendEmptyMessage(int what);

    void sendMessageDelayed(Message message, long delay);

    void post(Runnable runnable);

    void removeMessage(int what);

    void removeCallbacks(Runnable runnable);

    void removeAllMessage();

    void destroy();
}
