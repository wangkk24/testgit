package com.pukka.ydepg.common.utils.uiutil;

import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.common.utils.fileutil.PathManager;
import com.pukka.ydepg.moudule.player.node.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;


public class Colors {
    private static final String TAG = "Colors";
    private static final String LOAD_FLAG = "LoadFlag";
    private static final String LOADED = "Loaded";

    private static HashMap<String, String> colorHashMap = new HashMap<String, String>();

    private static Colors instance = null;

    public synchronized static Colors getInstance() {
        if (null == instance) {
            instance = new Colors();
        }

        return instance;
    }

    /**
     * Load resource.rc and parse color.
     */
    public void parse() {
        if (OTTApplication.getContext().isNeedLoadResource() && !LOADED.equals(colorHashMap.get
                (LOAD_FLAG))) {
            String colorFileName = PathManager.getPeelResourcePath() + File.separator + "light" +
                    Constant.RESOURCE_COLOR_PATH;
            String content = readFileByLines(colorFileName);
            if (null != content) {
                Resource resource = JsonParse.json2Object(content, Resource.class);
                if (null != resource) {
                    List<Resource.ColorEntity> colorList = resource.getColor();
                    for (Resource.ColorEntity colorEntity : colorList) {
                        colorHashMap.put(colorEntity.getName(), String.valueOf(Color.parseColor
                                (colorEntity.getValue())));
                    }

                    colorHashMap.put(LOAD_FLAG, LOADED);
                }
            }
        }
    }

    /**
     * Get the color by name
     *
     * @param name         color id
     * @param defaultColor color value
     * @return color value, not color id
     */
    private int getColor(String name, int defaultColor) {
        int color = defaultColor;

        if (!LOADED.equals(colorHashMap.get(LOAD_FLAG))) {
            parse();
        }

        if (colorHashMap.containsKey(name)) {
            color = Integer.parseInt(colorHashMap.get(name));
        }

        return color;
    }

    /**
     * Get the color by id
     *
     * @param res
     * @param colorId
     * @return color value, not color id
     */
    public int getColor(Resources res, int colorId) {
        String colorKeyN = "";
        int colorValue = res.getColor(colorId);
        if (OTTApplication.getContext().isNeedLoadResource()) {
            try {
                colorKeyN = res.getResourceEntryName(colorId);
            } catch (Exception e) {
                SuperLog.error(TAG, e);
            }

            if (!TextUtils.isEmpty(colorKeyN)) {
                colorValue = getColor(colorKeyN, res.getColor(colorId));
            }
        }
        return colorValue;
    }

    /**
     * Read the file by lines
     *
     * @param fileName
     * @return
     */
    public String readFileByLines(String fileName) {
        StringBuffer result = null;
        BufferedReader reader = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                reader = new BufferedReader(new FileReader(file));
                String line = null;
                result = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                SuperLog.debug(TAG, fileName + " is not exist.");
            }
        } catch (Exception e) {
            SuperLog.error(TAG, e);
            return null;
        } finally {
            FileUtil.closeReader(reader);
        }

        if (null != result && result.length() > 0) {
            return result.toString();
        } else {
            return null;
        }
    }
}
