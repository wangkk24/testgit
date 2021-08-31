# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/liukun/Developer/adt-bundle-mac-x86_64-20131030/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*

#保护反射
-keepattributes Signature
-keepattributes EnclosingMethod

#代码混淆不出现Unknown Source
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,SourceFile

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends me.imid.swipebacklayout.lib.app.SwipeBackActivity
-keep public class * extends com.huawei.rcs.RCSApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.pukka.ydepg.service.NtpTimeService
-keep public class com.pukka.ydepg.common.utils.uiutil.DensityUtil
-dontwarn com.pukka.ydepg.common.glide.**
-keep class com.pukka.ydepg.common.glide.** { *;}
-dontwarn skin.support.**
-keep class skin.support.** { *;}

#微信不混淆
-dontwarn com.tencent.mm.sdk.**
-keep class com.tencent.mm.sdk.** { *;}

#新浪不混淆
-dontwarn com.sina.weibo.sdk.**
-keep class com.sina.weibo.sdk.** { *;}

-keep public class com.pukka.ydepg.common.utils.JsonParse
-keep public class com.pukka.ydepg.common.utils.security.AntiHijackingUtil

# 保证gson一些对象不被混淆
-dontwarn com.pukka.ydepg.common.http.bean.node.**
-keep  class com.pukka.ydepg.common.http.bean.node.**{*;}
-dontwarn com.pukka.ydepg.common.http.bean.request.**
-keep  class com.pukka.ydepg.common.http.bean.request.**{*;}
-dontwarn com.pukka.ydepg.common.http.bean.response.**
-keep  class com.pukka.ydepg.common.http.bean.response.**{*;}

-dontwarn com.pukka.ydepg.common.http.v6bean.v6node.**
-keep  class com.pukka.ydepg.common.http.v6bean.v6node.**{*;}
-dontwarn com.pukka.ydepg.common.http.v6bean.v6request.**
-keep  class com.pukka.ydepg.common.http.v6bean.v6request.**{*;}
-dontwarn com.pukka.ydepg.common.http.v6bean.v6response.**
-keep  class com.pukka.ydepg.common.http.v6bean.v6response.**{*;}

-dontwarn com.pukka.ydepg.common.http.ystbean.ystcallback.**
-keep  class com.pukka.ydepg.common.http.ystbean.ystcallback.**{*;}
-dontwarn com.pukka.ydepg.common.http.ystbean.ystnode.**
-keep  class com.pukka.ydepg.common.http.ystbean.ystnode.**{*;}
-dontwarn com.pukka.ydepg.common.http.ystbean.**
-keep  class com.pukka.ydepg.common.http.ystbean.**{*;}
-dontwarn com.pukka.ydepg.common.http.entity.flow.**
-keep  class com.pukka.ydepg.common.http.entity.flow.**{*;}

-dontwarn com.pukka.ydepg.common.service.download.**
-keep  class com.pukka.ydepg.common.service.download.**{*;}

-dontwarn org.simpleframework.xml.**
-keep  class org.simpleframework.xml.**{*;}
-dontwarn org.simpleframework.xml.convert.**
-keep  class org.simpleframework.xml.convert.**{*;}
-dontwarn org.simpleframework.xml.core.**
-keep  class org.simpleframework.xml.core.**{*;}
-dontwarn org.simpleframework.xml.filter.**
-keep  class org.simpleframework.xml.filter.**{*;}
-dontwarn org.simpleframework.xml.strategy.**
-keep  class org.simpleframework.xml.strategy.**{*;}
-dontwarn org.simpleframework.xml.stream.**
-keep  class org.simpleframework.xml.stream.**{*;}
-dontwarn org.simpleframework.xml.transform.**
-keep  class org.simpleframework.xml.transform.**{*;}
-dontwarn org.simpleframework.xml.util.**
-keep  class org.simpleframework.xml.util.**{*;}

-dontwarn com.kenai.jbosh.**
-keep class com.kenai.jbosh.** {*; }
-dontwarn com.novell.sasl.client.**
-keep class com.novell.sasl.client.** {*; }
-dontwarn de.measite.smack.**
-keep class de.measite.smack.** {*; }
-dontwarn org.**
-keep class org.** {*; }


-dontwarn org.fourthline.cling.**
-keep class org.fourthline.cling.** {*; }
-dontwarn javax.servlet.**
-keep class javax.servlet.** {*; }
-dontwarn com.easier.ott.dlna2library.**
-keep class com.easier.ott.dlna2library.** {*; }
-dontwarn com.pukka.ydepg.moudule.dlna.**
-keep class com.pukka.ydepg.moudule.dlna.** {*; }

-dontwarn com.pukka.ydepg.moudule.player.bean.**
-keep  class com.pukka.ydepg.moudule.player.bean.**{*;}
-dontwarn com.pukka.ydepg.moudule.player.node.**
-keep  class com.pukka.ydepg.moudule.player.node.**{*;}
-dontwarn com.pukka.ydepg.moudule.multiscreeninteraction.deviceBean.**
-keep  class com.pukka.ydepg.moudule.multiscreeninteraction.deviceBean.**{*;}
-dontwarn com.easier.xmpp.entity.**
-keep  class com.easier.xmpp.entity.**{*;}
-dontwarn com.pukka.ydepg.moudule.homereally.view.**
-keep  class com.pukka.ydepg.moudule.homereally.view.**{*;}
-keep public class com.pukka.ydepg.common.AppConfig
-keep public class com.easier.xmpp.XMPPConnectUtil
-keep public class com.pukka.ydepg.moudule.multiscreeninteraction.XMPPPushInfo
-keep public class com.pukka.ydepg.common.http.session.Session
-keep public class com.pukka.ydepg.common.utils.XmlParse
-keep public class com.pukka.ydepg.common.utils.JsonParse
-keep public class com.pukka.ydepg.common.utils.FloatViewStatusListener
-keep public class com.pukka.ydepg.common.utils.DeviceInfo
-keep public class com.pukka.ydepg.common.utils.CrashHandler
-keep public class com.pukka.ydepg.common.utils.uiutil.EpgToast
-keep public class com.pukka.ydepg.common.utils.AbsFloatVideoStatusListener
-keep public class com.pukka.ydepg.wxapi.WXEntryActivity
-keep public class com.pukka.ydepg.wxapi.WBActivity
-keep public class com.pukka.ydepg.moudule.share.WXSDKCallBack
-keep public class com.pukka.ydepg.moudule.share.gifshare.activity.IGifMakeViewCallBack

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#忽略警告
-ignorewarning
#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################
################混淆保护自己项目的部分代码以及引用的第三方jar包library#########################
#-injars libs/jackson-annotations-2.5.0.jar

#不混淆jar
#-libraryjars libs/BaiduLBS_Android.jar
#-libraryjars libs/commons-io-2.4.jar
#-libraryjars libs/core-3.0.0.jar
#-libraryjars libs/DmpPlayer.jar
#-libraryjars libs/GetuiSDK2.10.2.0.jar
#-libraryjars libs/java-json.jar
#-libraryjars libs/libammsdk-3.1.1.jar
#-libraryjars libs/nineoldandroids-2.4.0.jar
#-libraryjars libs/obf-mlab.android.speedvideo.sdk_2.0.5.jar
#-libraryjars libs/sa_call_recording.jar
#-libraryjars libs/sa_svn_stg.jar
#-libraryjars libs/universal-image-loader-1.9.5.jar
#-libraryjars libs/winzip.jar
#-libraryjars libs/caas/HME_Audio.jar
#-libraryjars libs/caas/HME_Video.jar
#-libraryjars libs/caas/lalal.txt
#-libraryjars libs/caas/sa_aidl.jar
#-libraryjars libs/caas/sa_base.jar
#-libraryjars libs/caas/sa_call.jar
#-libraryjars libs/caas/sa_contact.jar
#-libraryjars libs/caas/sa_default_tls.jar
#-libraryjars libs/caas/sa_eab.jar
#-libraryjars libs/caas/sa_epipe.jar
#-libraryjars libs/caas/sa_group.jar
#-libraryjars libs/caas/sa_hme_audio.jar
#-libraryjars libs/caas/sa_hme_audio_tv.jar
#-libraryjars libs/caas/sa_hme_video.jar
#-libraryjars libs/caas/sa_hme_video_tv.jar
#-libraryjars libs/caas/sa_http.jar
#-libraryjars libs/caas/sa_im.jar
#-libraryjars libs/caas/sa_imb.jar
#-libraryjars libs/caas/sa_imbs.jar
#-libraryjars libs/caas/sa_media_rmt.jar
#-libraryjars libs/caas/sa_meeting.jar
#-libraryjars libs/caas/sa_nab.jar
#-libraryjars libs/caas/sa_nat_stg.jar
#-libraryjars libs/caas/sa_omp.jar
#-libraryjars libs/caas/sa_push.jar
#-libraryjars libs/caas/sa_socks5.jar
#-libraryjars libs/caas/sa_stun.jar
#-libraryjars libs/caas/sa_tsc_stg.jar
#-libraryjars libs/caas/sa_uportal.jar
#-libraryjars libs/caas/sa_uvc.jar
#-libraryjars libs/caas/sci_base.jar
#-libraryjars libs/caas/sci_call.jar
#-libraryjars libs/caas/sci_default_tls.jar
#-libraryjars libs/caas/sci_eab.jar
#-libraryjars libs/caas/sci_group.jar
#-libraryjars libs/caas/sci_hme_audio.jar
#-libraryjars libs/caas/sci_hme_audio_tv.jar
#-libraryjars libs/caas/sci_hme_video.jar
#-libraryjars libs/caas/sci_hme_video_tv.jar
#-libraryjars libs/caas/sci_im.jar
#-libraryjars libs/caas/sci_imb.jar
#-libraryjars libs/caas/sci_meeting.jar
#-libraryjars libs/caas/sci_nab.jar
#-libraryjars libs/caas/sci_pres.jar
#-libraryjars libs/caas/sci_push.jar
#-libraryjars libs/caas/sci_uportal.jar
#-libraryjars libs/caas/uspbase.jar
#-libraryjars libs/caas/uspdma.jar
#-libraryjars libs/caas/uspepipe.jar
#-libraryjars libs/caas/uspimb.jar
#-libraryjars libs/caas/uspimbs.jar
#-libraryjars libs/caas/usplogin.jar
#-libraryjars libs/caas/uspmediarmt.jar
#-libraryjars libs/caas/uspmqtt.jar
#-libraryjars libs/caas/uspomp.jar
#-libraryjars libs/caas/uspsocks5.jar
#-libraryjars libs/caas/uspstg.jar
#-libraryjars libs/caas/uspuportal.jar
#-libraryjars libs/caas/uspuvc.jar
#-libraryjars libs/caas/uspwatcher.jar


#忽略So库
-libraryjars ../jniso/playerso/armeabi/libc++_shared.so
-libraryjars ../jniso/playerso/armeabi/libcurl.so
-libraryjars ../jniso/playerso/armeabi/libdmpbase.so
-libraryjars ../jniso/playerso/armeabi/libdrmclient_android.so
-libraryjars ../jniso/playerso/armeabi/libeop.so
-libraryjars ../jniso/playerso/armeabi/libepp.so
-libraryjars ../jniso/playerso/armeabi/libGetQP.so
-libraryjars ../jniso/playerso/armeabi/libOttCaInterface.so
-libraryjars ../jniso/playerso/armeabi/libottnovel.so
-libraryjars ../jniso/playerso/armeabi/libOTTPR.so
-libraryjars ../jniso/playerso/armeabi/libottvmxca.so
-libraryjars ../jniso/playerso/armeabi/libottwidevine.so
-libraryjars ../jniso/playerso/armeabi/libpdc.so
-libraryjars ../jniso/playerso/armeabi/libPE.so
-libraryjars ../jniso/playerso/armeabi/libPEBase.so
-libraryjars ../jniso/playerso/armeabi/libPEDaa.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEDavi.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEDiag.so
-libraryjars ../jniso/playerso/armeabi/libPEDpem.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEHttpBase.so
-libraryjars ../jniso/playerso/armeabi/libPEJni.so
-libraryjars ../jniso/playerso/armeabi/libPEJrtpSegment.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEMCC.plg.so
-libraryjars ../jniso/playerso/armeabi/libPERtmp.plg.so
-libraryjars ../jniso/playerso/armeabi/libPESpem.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEWinDash.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEWinHss.plg.so
-libraryjars ../jniso/playerso/armeabi/libPEWudpSegment.plg.so
-libraryjars ../jniso/playerso/armeabi/libViewRightWebClient.so
-libraryjars ../jniso/playerso/armeabi/libwidevine.so
-libraryjars ../jniso/playerso/armeabi/libCDNSelector.so
-libraryjars ../jniso/playerso/armeabi/libPEPlayer.so
-libraryjars ../jniso/playerso/armeabi/libUnityMediaSurface.so
-libraryjars ../jniso/playerso/armeabi/libVr360.so
-libraryjars ../jniso/playerso/armeabi/libVrInterface.so


-libraryjars ../jniso/otherso/armeabi/libgenius_blur.so
-libraryjars ../jniso/otherso/armeabi/libsimcard.so
-libraryjars ../jniso/otherso/armeabi/libgetuiext2.so
-libraryjars ../jniso/otherso/armeabi/libstb_44.so
-libraryjars ../jniso/otherso/armeabi/libavcodec.so
-libraryjars ../jniso/otherso/armeabi/libhi37xx.so
-libraryjars ../jniso/otherso/armeabi/libswscale.so
-libraryjars ../jniso/otherso/armeabi/libavformat.so
-libraryjars ../jniso/otherso/armeabi/libim.so
-libraryjars ../jniso/otherso/armeabi/libuportal.so
-libraryjars ../jniso/otherso/armeabi/libavutil.so
-libraryjars ../jniso/otherso/armeabi/libjpegbither.so
-libraryjars ../jniso/otherso/armeabi/libbitherjni.so
-libraryjars ../jniso/otherso/armeabi/libnab.so
-libraryjars ../jniso/otherso/armeabi/libffmpeg_mediametadataretriever_jni.so
-libraryjars ../jniso/otherso/armeabi/libpres.so



# 百度地图相关不需要混淆
-dontwarn com.baidu**
-keep class com.baidu.** { *; }
-dontwarn vi.com.gdi.bgl.android.**
-keep class vi.com.gdi.bgl.android.**{*;}


#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#某些构造方法不能去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#greenDao
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}

-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**

-keep public class com.pukka.ydepg.database.MigrationDBHelper

-dontwarn com.pukka.ydepg.database.gen.**
-keep  class com.pukka.ydepg.database.gen.**{*;}
-dontwarn com.pukka.ydepg.database.greendao.**
-keep  class com.pukka.ydepg.database.greendao.**{*;}


#EventBus
-dontwarn org.greenrobot.eventbus.**
-keep class org.greenrobot.eventbus.** { *;}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keep class de.greenrobot.event.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#我是以libaray的形式引用了一个图片加载框架,如果不想混淆 keep 掉
-dontwarn android.support.**
-keep class android.support.** { *;}
-dontwarn com.google.**
-keep class com.google.** { *;}
-dontwarn com.digits.**
-keep class com.digits.** { *;}
-dontwarn edu.umd.**
-keep class edu.umd.** { *;}
-dontwarn com.crittercism.**
-keep class com.crittercism.** { *;}
-dontwarn crittercism.**
-keep class crittercism.** { *;}
-dontwarn com.fasterxml.**
-keep class com.fasterxml.** { *;}
-dontwarn javax.**
-keep class javax.** { *;}
-dontwarn com.jcraft.**
-keep class com.jcraft.** { *;}
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *;}
-dontwarn retrofit.**
-keep class retrofit.** { *;}
-dontwarn roboguice.**
-keep class roboguice.** { *;}
-dontwarn com.nostra13.**
-keep class com.nostra13.** { *;}
-dontwarn com.ott.**
-keep class com.ott.** { *;}
-dontwarn org.bouncycastle.**
-keep class org.bouncycastle.** { *;}
-dontwarn bolts.**
-keep class bolts.** { *;}
-dontwarn org.w3c.dom.bootstrap.**
-keep class org.w3c.dom.bootstrap.** { *;}
-dontwarn org.ietf.jgss.**
-keep class org.ietf.jgss.** { *;}
-dontwarn com.xtremelabs.**
-keep class com.xtremelabs.** { *;}
-dontwarn org.junit.runners.**
-keep class org.junit.runners.** { *;}

#易视腾
-dontwarn com.ysten.videoplus.**
-keep class com.ysten.videoplus.** { *;}
-dontwarn com.ysten.videoplus.client.**
-keep class com.ysten.videoplus.client.** { *;}
-dontwarn com.ysten.videoplus.client.network.**
-keep class com.ysten.videoplus.client.network.** { *;}
-dontwarn com.ysten.videoplus.client.network.model.**
-keep class com.ysten.videoplus.client.network.model.** { *;}
-keep public class com.ysten.videoplus.client.network.huawei.BaseCallBack


#rxjava+retrofit
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# 华为播放器不混淆 start
-keep class com.huawei.ott.sdk.** { *;}
-dontwarn com.huawei.ott.sdk.**
-keep class com.huawei.ott.vsp.** { *;}
-dontwarn com.huawei.ott.vsp.**

-keep class com.huawei.ott.model.** { *;}
-dontwarn com.huawei.ott.model.**

-keep class com.huawei.ca.** { *;}
-dontwarn com.huawei.ca.**
-keep class com.huawei.clientplayer.** { *;}
-dontwarn com.huawei.clientplayer.**
-keep class com.huawei.download.** { *;}
-dontwarn com.huawei.download.**
-keep class com.huawei.so.** { *;}
-dontwarn com.huawei.so.**
-keep class com.huawei.sqm.** { *;}
-dontwarn com.huawei.sqm.**
-keep class com.huawei.subtitle.** { *;}
-dontwarn com.huawei.subtitle.**
-keep class com.huawei.loadlibrary.** { *;}
-dontwarn com.huawei.loadlibrary.**
-keep class com.huawei.oneKey.** { *;}
-dontwarn com.huawei.oneKey.**
-keep class com.huawei.ott.eop.** { *;}
-dontwarn com.huawei.ott.eop.**
-keep class com.huawei.remoteplayer.** { *;}
-dontwarn com.huawei.remoteplayer.**
-keep class com.huawei.playerinterface.** { *;}
-dontwarn com.huawei.playerinterface.**
-keep class com.huawei.PEPlayerInterface.** { *;}
-dontwarn com.huawei.PEPlayerInterface.**
-keep class com.huawei.dmpbase.** { *;}

-keep class com.huawei.ott.service.dynamic.node.** { *;}

-keep class com.huawei.ott.gadget.extview.bean.** { *;}
-dontwarn com.huawei.ott.gadget.extview.bean.**

-keep class com.huawei.ott.composite.player.bean.** { *;}
-dontwarn com.huawei.ott.composite.player.bean.**

-keep class com.huawei.ott.module.player.cache.ReportRequest{ *;}
-dontwarn com.huawei.ott.module.player.cache.ReportRequest
-keep class com.huawei.ott.module.player.cache.PreviewHistory{ *;}
-dontwarn com.huawei.ott.module.player.cache.PreviewHistory

# 华为播放器不混淆 end

#如果引用了v4或者v7包
-dontwarn android.support.**
-keep class android.support.**{*;}

############混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 不混淆Glide内存配置
-keepnames class com.pukka.ydepg.common.glide.SuperGlideModule
# or more generally:
#-keep public class * implements com.bumptech.glide.module.GlideModule

# 不混淆个推
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }