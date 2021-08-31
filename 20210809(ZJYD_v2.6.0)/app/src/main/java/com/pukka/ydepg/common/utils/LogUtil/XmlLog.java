package com.pukka.ydepg.common.utils.LogUtil;

import android.util.Log;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XmlLog {

    public static void printXml(String tag, String xml, String headString) {

        if (xml != null) {
            xml = XmlLog.formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + SuperLog.NULL_TIPS;
        }

        Util.printLine(tag, true);
        String[] lines = xml.split(SuperLog.NEW_LINE);
        for (String line : lines) {
            if (!Util.isEmpty(line)) {
                Log.d(tag, "║ " + line);
            }
        }
        Util.printLine(tag, false);
    }

    public static String formatXML(String inputXML) {
        try {
            StreamSource xmlInput  = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();


            // ========== 安全整改 Start,如有问题删除 ==========
            // 转换器工厂设置黑名单，禁用一些不安全的方法，类似XXE防护
            tf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            // 去掉<?xml version="1.0" encoding="UTF-8"?>
            transformer.setOutputProperty("omit-xml-declaration","yes");
            // ========== 安全整改 End  ,如有问题删除 ==========


            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            Log.e("XmlLog","formatXML failed",e);
            return inputXML;
        }
    }
}