package com.pukka.ydepg.common.utils.uiutil;

import android.content.res.Resources;
import android.util.Xml;


import com.pukka.ydepg.common.utils.datautil.SettingLanguageUtil;
import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.constant.Constant;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.common.utils.fileutil.PathManager;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Static utility class used for reading colors from xml and storing the names
 */
public class Strings
{
    private static final String TAG = "Strings";
    private static final String STRINGS_NAME = "string_%1$s.xml";
    private static final String LOAD_FLAG = "LoadFlag";
    private static final String LOADED = "Loaded";

    private static HashMap<String, String> stringsHashMap = new HashMap<String, String>();
    private static HashMap<String, String> needTransferHashMap = new HashMap<String, String>();
    private static Strings instance = null;

    private Resources resources = null;

    public synchronized static Strings getInstance()
    {
        if (null == instance)
        {
            instance = new Strings();
        }

        return instance;
    }

    public Strings()
    {
        resources = OTTApplication.getContext().getResources();
        needTransferHashMap.put("&amp;", "&");
        needTransferHashMap.put("&lt;", "<");
        needTransferHashMap.put("&gt;", ">");
        needTransferHashMap.put("&quot;", "\"");
        needTransferHashMap.put("&apos;", "'");
        needTransferHashMap.put("\\n", System.getProperty("line.separator"));
    }

    /**
     * Load string_XX.xml and parse it.
     *
     * @param lang
     * @param isFromLangSwitch
     */
    public void parse(String lang, boolean isFromLangSwitch)
    {
        if (OTTApplication.getContext().isNeedLoadResource() && !LOADED.equals(stringsHashMap.get
                (LOAD_FLAG + "#" + lang)) || isFromLangSwitch)
        {
            String stringsName = String.format(STRINGS_NAME, lang);
            String stringsPath = PathManager.getPeelResourcePath() +File.separator+"light"+
                    Constant.RESOURCE_STRINGS_PATH + stringsName;
            File stringsFile = new File(stringsPath);
            if (stringsFile.exists())
            {
                InputStream is = null;
                try
                {
                    is = new FileInputStream(stringsFile);
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(is, null);
                    parser.nextTag();

                    int depth = parser.getDepth();
                    while (parser.getEventType() != XmlPullParser.END_TAG || parser.getDepth() >
                            depth)
                    {
                        parser.next();

                        if (parser.getEventType() == XmlPullParser.START_TAG)
                        {
                            String name = parser.getAttributeValue(null, "name");
                            parser.next();

                            String value = parser.getText();
                            Set set = needTransferHashMap.keySet();
                            for (Iterator it = set.iterator(); it.hasNext(); )
                            {
                                String key = (String) it.next();
                                if (value.contains(key))
                                {
                                    value = value.replace(key, (String) needTransferHashMap.get
                                            (key));
                                    break;
                                }
                            }

                            stringsHashMap.put(name + "#" + lang, value);
                        }
                    }

                    stringsHashMap.put(LOAD_FLAG + "#" + lang, LOADED);
                }
                catch (Exception e)
                {
                    SuperLog.error(TAG, e);
                }
                finally
                {
                    FileUtil.closeInputStream(is);
                }
            }
        }
    }

    /**
     * Get the string by name
     *
     * @param name
     * @param defaultStr
     * @return
     */
    public String getString(String name, String defaultStr)
    {
        if (OTTApplication.getContext().isNeedLoadResource())
        {
            String lang = SettingLanguageUtil.getLanguage();
            if (!LOADED.equals(stringsHashMap.get(LOAD_FLAG + "#" + lang)))
            {
                parse(lang, false);
            }

            String key = name + "#" + lang;
            if (stringsHashMap.containsKey(key))
            {
                return stringsHashMap.get(key);
            }
        }
        return defaultStr;
    }

    public String getString(int resId)
    {
        String name = resources.getResourceEntryName(resId);
        String value = getString(name, resources.getString(resId));
        return value;
    }

    public String getString(int resId, Object... formatArgs)
    {
        String name = resources.getResourceEntryName(resId);
        String value = getString(name, resources.getString(resId));
        String formattedValue = String.format(value, formatArgs);
        return formattedValue;
    }
}
