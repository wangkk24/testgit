package com.pukka.ydepg.launcher.session;

import android.text.TextUtils;

import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.moudule.mytv.utils.PathManager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * the entrance of UI get the session data
 * the data will be serialize and store in DB.
 */
public class SessionService {

    private String path = PathManager.getVSPCachePath() + "Session";

    private Session session = null;

    private static SessionService gInstance = new SessionService();

    public static SessionService getInstance()
    {
        return gInstance;
    }


    //store the session data
    public void commitSession() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                if (null != session) {
                    String encryptSession = JsonParse.object2String(session);
                    FileUtil.saveContentToFile(path, encryptSession);
                }
            }
        }, "SessionSaveToDBThread.");
    }

    public Session getSession() {
        if (null == session) {
            try {
                String encryptSession = FileUtils.readFileToString(new File(path));
                String decryptSession = encryptSession;
                if (!TextUtils.isEmpty(decryptSession)) {
                    session = JsonParse.json2Object(decryptSession,Session.class);
                }
            } catch (IOException e) {

            }

            if (null == session) {
                session = new Session();
            }
        }
        return session;
    }

    public void destroySession() {
        FileUtil.deleteFile(path);
        session = null;
    }
}