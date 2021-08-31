package com.pukka.ydepg.launcher.http.hecaiyun.data;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/*
<serverinfo>
<rifurl>http://ose.caiyun.feixin.10086.cn:80/richlifeApp</rifurl>
<calURL>http://ose.caiyun.feixin.10086.cn/richlifeApp</calURL>
<testTermConnectURL>http://aas.caiyun.feixin.10086.cn/tellin/usr/puc/ispace/testTermConnect.do</testTermConnectURL>
</serverinfo>
*/

@Root
public class ServerInfo {
    @Element
    String rifurl;
    @Element
    String calURL;
    @Element
    String testTermConnectURL;

    @Element(required = false)
    String AASThreadPoolMonitorInterval;

}
