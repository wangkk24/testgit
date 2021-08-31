package com.huawei.stb.xmpp;

import com.huawei.stb.xmpp.IXMPPCallback;

interface IXMPPService
{
    String initialXMPPConnection(String packageName, String pushServerUrl, String JID, String saslID, String saslPasswd, String accessToken, String impAuthType);

    String deInitialXMPPConnection(String handler);

    String checkConnection(String handler);

    String sendXMPPMessage(String handler,String JID,String message);

    String registerXMPPCallback(String handler, IXMPPCallback xmppCallback);

    String unregisterXMPPCallback(String handler, IXMPPCallback xmppCallback);

    String updateAccessToken(String handler,String JID,String accessToken);

    // APP通过XMPP Service更新STB状态
    String setXmppRosterRemove(String handler, String showPresence, String statusPresence);

    // APP通过XMPP Service查询用户状态
    String getXmppUserStatus(String handler, String queryXmlString);
}