package com.pukka.ydepg.launcher.http.hecaiyun.data;

/*
<userExtInfo>
<passID>112501417</passID>
<isRegWeibo>-1</isRegWeibo>
<>ODVBMEQ5RDZCQjcxMjQ1NTA2NTAyMjBCNTY3QzM5QTVBQTgxMEU1RTMyRjg4QTlDOTExREE5RjI4NDY0RkQ5OTo0NTIzNDQ=</accessToken>
</userExtInfo>
*/

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class UserExtInfo {

    @Element
    String passID;
    @Element
    String isRegWeibo;
    @Element
    String accessToken;
}
