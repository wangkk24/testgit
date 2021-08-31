package com.pukka.ydepg.launcher.bean.node;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liudo on 2018/10/22.
 */

public class VoiceBean
{

    @SerializedName("_scene")
    private String scene;

    @SerializedName("_commands")
    private Map<String, List<String>> commands = new LinkedHashMap<>();

    @SerializedName("_fuzzy_words")
    private Map<String, List<String>> fuzzyWords = new LinkedHashMap<>();


    public VoiceBean(String scene,List<String> keys, List<String> directives, List<String> fuzzys)
    {
        if (null != keys && null != directives && null != fuzzys && keys.size() == directives
            .size() && directives.size() == fuzzys.size())
        {

            for (int i = 0; i < keys.size(); i++)
            {
                List<String> order = new ArrayList<>();
                StringBuffer buffer=new StringBuffer();
                buffer.append("$W(");
                buffer.append(directives.get(i));
                buffer.append(")");
                order.add(buffer.toString());
                commands.put(keys.get(i), order);
                List<String> fuzzstrs = new ArrayList<>();
                fuzzstrs.add(fuzzys.get(i));
                fuzzyWords.put(directives.get(i),fuzzstrs);
            }



        }
        this.scene=scene;

    }

}
