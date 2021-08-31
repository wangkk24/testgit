package com.huawei.stb.xmpp;

interface IXMPPCallback
{
    String xmppConnectionStatus(String handler,String connectionStatus);
    String xmppMessageArrived(String handler, String jid, String subject,String body,String thread, String report, String delay);
}