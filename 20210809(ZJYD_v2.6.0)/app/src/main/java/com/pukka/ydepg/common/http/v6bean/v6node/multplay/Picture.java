package com.pukka.ydepg.common.http.v6bean.v6node.multplay;

import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * 作者：panjw on 2021/6/11 15:43
 * <p>
 * 邮箱：panjw@easier.cn
 */
public class Picture {
/**
 * deflates : ["",""]
 * posters : ["",""]
 * stills : ["",""]
 * icons : ["",""]
 * titles : ["",""]
 * ads : ["",""]
 * drafts : ["",""]
 * backgrounds : ["",""]
 * channelPics : ["",""]
 * channelBlackWhites : ["",""]
 * channelNamePics : ["",""]
 * secondarys : ["",""]
 * logoBrightBgs : ["",""]
 * logoDarkBgs : ["",""]
 * mainWides : ["",""]
 * previews : ["",""]
 * secondaryWides : ["",""]
 * instantRestartLogos : ["",""]
 * channelFallbackWides : ["",""]
 * others : ["",""]
 * hcsSlaveAddrs : ["",""]
 * customizedPic1: ["",""]
 * customizedPic2: ["",""]
 * customizedPic3: ["",""]
 * customizedPic4: ["",""]
 * customizedPic5: ["",""]
 * customizedPic6: ["",""]
 * customizedPic7: ["",""]
 * customizedPic8: ["",""]
 * customizedPic9: ["",""]
 * customizedPic10: ["",""]
 * extensionFields :
 */


    /**
     * 缩略图绝对路径。（为HTTP URL，例如，http://ip:port/VSP/jsp/image/12.jpg）。
     */
    @SerializedName("deflates")
    private List<String> deflates;

    /**
     * 海报绝对路径。
     */
    @SerializedName("posters")
    private List<String> posters;

    /**
     * 剧照绝对路径。
     */
    @SerializedName("stills")
    private List<String> stills;

    /**
     * 图标绝对路径。
     */
    @SerializedName("icons")
    private List<String> icons;

    /**
     * 标题图绝对路径。
     */
    @SerializedName("titles")
    private List<String> titles;

    /**
     * 广告图绝对路径。
     */
    @SerializedName("ads")
    private List<String> ads;

    /**
     * 草图路径绝对路径。
     */
    @SerializedName("drafts")
    private List<String> drafts;

    /**
     * 背景图绝对路径。
     */
    @SerializedName("backgrounds")
    private List<String> backgrounds;

    @SerializedName("background")
    private String background;

    /**
     * 频道图片绝对路径。
     */
    @SerializedName("channelPics")
    private List<String> channelPics;

    /**
     * 频道黑白图片绝对路径。
     */
    @SerializedName("channelBlackWhites")
    private List<String> channelBlackWhites;

    /**
     * 频道名字图片绝对路径。
     */
    @SerializedName("channelNamePics")
    private List<String> channelNamePics;

    /**
     * secondary图片绝对路径。
     */
    @SerializedName("secondarys")
    private List<String> secondarys;

    /**
     * logoBrightBg图片绝对路径。
     */
    @SerializedName("logoBrightBgs")
    private List<String> logoBrightBgs;

    /**
     * logoDarkBg图片绝对路径。
     */
    @SerializedName("logoDarkBgs")
    private List<String> logoDarkBgs;

    /**
     * mainWide图片绝对路径。
     */
    @SerializedName("mainWides")
    private List<String> mainWides;

    /**
     * 拖动预览图片绝对路径。
     */
    @SerializedName("previews")
    private List<String> previews;

    /**
     * secondaryWide图片绝对路径。
     */
    @SerializedName("secondaryWides")
    private List<String> secondaryWides;

    /**
     * instantRestartLogo图片绝对路径。
     */
    @SerializedName("instantRestartLogos")
    private List<String> instantRestartLogos;

    /**
     * channelFallbackWide图片绝对路径。
     */
    @SerializedName("channelFallbackWides")
    private List<String> channelFallbackWides;

    /**
     * 其他图片的绝对路径。
     */
    @SerializedName("others")
    private List<String> others;
    /**
     自定义类型1的图片绝对路径。
     使用范围：
     Subject
     VOD
     Channel
     Playbill
     Cast
     */
    @SerializedName("customizedPic1")
    private List<String> customizedPic1;
    @SerializedName("customizedPic2")
    private List<String> customizedPic2;
    @SerializedName("customizedPic3")
    private List<String> customizedPic3;
    @SerializedName("customizedPic4")
    private List<String> customizedPic4;
    @SerializedName("customizedPic5")
    private List<String> customizedPic5;
    @SerializedName("customizedPic6")
    private List<String> customizedPic6;
    @SerializedName("customizedPic7")
    private List<String> customizedPic7;
    @SerializedName("customizedPic8")
    private List<String> customizedPic8;
    @SerializedName("customizedPic9")
    private List<String> customizedPic9;
    @SerializedName("customizedPic10")
    private List<String> customizedPic10;

    /**
     * HCS备用地址。
     */
    @SerializedName("hcsSlaveAddrs")
    private List<String> hcsSlaveAddrs;

    /**
     * 扩展信息。
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public List<String> getDeflates() {
        return deflates;
    }

    public void setDeflates(List<String> deflates) {
        this.deflates = deflates;
    }

    public List<String> getPosters() {
        return posters;
    }

    public void setPosters(List<String> posters) {
        this.posters = posters;
    }

    public List<String> getStills() {
        return stills;
    }

    public void setStills(List<String> stills) {
        this.stills = stills;
    }

    public List<String> getIcons() {
        return icons;
    }

    public void setIcons(List<String> icons) {
        this.icons = icons;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getAds() {
        return ads;
    }

    public void setAds(List<String> ads) {
        this.ads = ads;
    }

    public List<String> getDrafts() {
        return drafts;
    }

    public void setDrafts(List<String> drafts) {
        this.drafts = drafts;
    }

    public List<String> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(List<String> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public List<String> getChannelPics() {
        return channelPics;
    }

    public void setChannelPics(List<String> channelPics) {
        this.channelPics = channelPics;
    }

    public List<String> getChannelBlackWhites() {
        return channelBlackWhites;
    }

    public void setChannelBlackWhites(List<String> channelBlackWhites) {
        this.channelBlackWhites = channelBlackWhites;
    }

    public List<String> getChannelNamePics() {
        return channelNamePics;
    }

    public void setChannelNamePics(List<String> channelNamePics) {
        this.channelNamePics = channelNamePics;
    }

    public List<String> getSecondarys() {
        return secondarys;
    }

    public void setSecondarys(List<String> secondarys) {
        this.secondarys = secondarys;
    }

    public List<String> getLogoBrightBgs() {
        return logoBrightBgs;
    }

    public void setLogoBrightBgs(List<String> logoBrightBgs) {
        this.logoBrightBgs = logoBrightBgs;
    }

    public List<String> getLogoDarkBgs() {
        return logoDarkBgs;
    }

    public void setLogoDarkBgs(List<String> logoDarkBgs) {
        this.logoDarkBgs = logoDarkBgs;
    }

    public List<String> getMainWides() {
        return mainWides;
    }

    public void setMainWides(List<String> mainWides) {
        this.mainWides = mainWides;
    }

    public List<String> getPreviews() {
        return previews;
    }

    public void setPreviews(List<String> previews) {
        this.previews = previews;
    }

    public List<String> getSecondaryWides() {
        return secondaryWides;
    }

    public void setSecondaryWides(List<String> secondaryWides) {
        this.secondaryWides = secondaryWides;
    }

    public List<String> getInstantRestartLogos() {
        return instantRestartLogos;
    }

    public void setInstantRestartLogos(List<String> instantRestartLogos) {
        this.instantRestartLogos = instantRestartLogos;
    }

    public List<String> getChannelFallbackWides() {
        return channelFallbackWides;
    }

    public void setChannelFallbackWides(List<String> channelFallbackWides) {
        this.channelFallbackWides = channelFallbackWides;
    }

    public List<String> getOthers() {
        return others;
    }

    public void setOthers(List<String> others) {
        this.others = others;
    }

    public List<String> getCustomizedPic1() {
        return customizedPic1;
    }

    public void setCustomizedPic1(List<String> customizedPic1) {
        this.customizedPic1 = customizedPic1;
    }

    public List<String> getCustomizedPic2() {
        return customizedPic2;
    }

    public void setCustomizedPic2(List<String> customizedPic2) {
        this.customizedPic2 = customizedPic2;
    }

    public List<String> getCustomizedPic3() {
        return customizedPic3;
    }

    public void setCustomizedPic3(List<String> customizedPic3) {
        this.customizedPic3 = customizedPic3;
    }

    public List<String> getCustomizedPic4() {
        return customizedPic4;
    }

    public void setCustomizedPic4(List<String> customizedPic4) {
        this.customizedPic4 = customizedPic4;
    }

    public List<String> getCustomizedPic5() {
        return customizedPic5;
    }

    public void setCustomizedPic5(List<String> customizedPic5) {
        this.customizedPic5 = customizedPic5;
    }

    public List<String> getCustomizedPic6() {
        return customizedPic6;
    }

    public void setCustomizedPic6(List<String> customizedPic6) {
        this.customizedPic6 = customizedPic6;
    }

    public List<String> getCustomizedPic7() {
        return customizedPic7;
    }

    public void setCustomizedPic7(List<String> customizedPic7) {
        this.customizedPic7 = customizedPic7;
    }

    public List<String> getCustomizedPic8() {
        return customizedPic8;
    }

    public void setCustomizedPic8(List<String> customizedPic8) {
        this.customizedPic8 = customizedPic8;
    }

    public List<String> getCustomizedPic9() {
        return customizedPic9;
    }

    public void setCustomizedPic9(List<String> customizedPic9) {
        this.customizedPic9 = customizedPic9;
    }

    public List<String> getCustomizedPic10() {
        return customizedPic10;
    }

    public void setCustomizedPic10(List<String> customizedPic10) {
        this.customizedPic10 = customizedPic10;
    }

    public List<String> getHcsSlaveAddrs() {
        return hcsSlaveAddrs;
    }

    public void setHcsSlaveAddrs(List<String> hcsSlaveAddrs) {
        this.hcsSlaveAddrs = hcsSlaveAddrs;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
