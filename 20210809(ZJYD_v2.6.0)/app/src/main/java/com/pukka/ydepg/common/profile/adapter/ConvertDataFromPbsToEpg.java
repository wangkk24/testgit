package com.pukka.ydepg.common.profile.adapter;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.Dialect;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.ExtraData;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.Page;
import com.pukka.ydepg.common.http.v6bean.v6node.VOD;
import com.pukka.ydepg.common.profile.data.ControlInfo;
import com.pukka.ydepg.common.profile.data.ProfileCustomization;
import com.pukka.ydepg.common.profile.data.ProfileVOD;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.bean.node.SubjectVodsList;
import com.pukka.ydepg.launcher.ui.MainActivity;
import com.pukka.ydepg.launcher.util.Utils;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertDataFromPbsToEpg {

    private static final String TAG = ConvertDataFromPbsToEpg.class.getSimpleName();

    private static boolean isHadAdd = false;

    public static boolean isHadAddPbsEpg(){
        return isHadAdd;
    }

    public static void setIsHadAddPbsEpg(boolean isAdd){
        isHadAdd = isAdd;
    }

    public static ProfileCustomization mProfileCustomization = null;

    private static Navigate mOldNavigate = null;
    private static List<GroupElement> mOldGroupElementList = null;

    public static Navigate getNavigate(){
        return mOldNavigate;
    }
    public static List<GroupElement> getGroupElementList(){
        return mOldGroupElementList;
    }

    //爱看navId
    public final static String AIKAN_NAV_ID = "-10";

    public static void convertDataFromPbsToEpg(ProfileCustomization profileCustomization, LauncherService.OnQueryLauncherListener launcherListener,boolean isFresh){
        List<Navigate> navigateList = LauncherService.getInstance().getLauncher().getNavigateList();
        ArrayList<List<GroupElement>> groupElements = LauncherService.getInstance().getGroupElements();
        mProfileCustomization = profileCustomization;

        if (null == mProfileCustomization){
            if (isHadAdd){
                removeOldPbsEpgData(navigateList,groupElements);
                //此方法须保证在主线程执行
                //launcherListener.onVersionChange(false,isFresh,false);
                if (OTTApplication.getContext().getCurrentActivity() instanceof MainActivity){
                    ((MainActivity)OTTApplication.getContext().getCurrentActivity()).notifyDataSetChanged(0);
                }
            }else{
                OTTApplication.getContext().setIsFirstPower(false);
            }
            mOldNavigate = null;
            mOldGroupElementList = null;
            return;
        }

        //removeOldPbsEpgData(navigateList,groupElements);

        SuperLog.info2SD(TAG,"primaryAccount = " + mProfileCustomization.getPrimaryAccount());

        Navigate navigate = new Navigate();
        //List<Dialect> nameDialect = navigate.getNameDialect();

        //设置标题
        List<Dialect> nameDialect = new ArrayList<>();
        Dialect dialect = new Dialect();
        dialect.setLanguage("zh");
        dialect.setValue(mProfileCustomization.getPageName());
        nameDialect.add(dialect);
        navigate.setNameDialect(nameDialect);
        navigate.setId(AIKAN_NAV_ID);

        Map<String,String> extraDataNav = new HashMap<>();
        extraDataNav.put(Utils.FOCUS_COLOR,mProfileCustomization.getFocusColor());
        navigate.setExtraData(extraDataNav);

        //设置背景
        List<Page> pageList = new ArrayList<>();
        Page page = new Page();
        page.setBackground(mProfileCustomization.getBackground());
        pageList.add(page);
        navigate.setPageList(pageList);

        navigate.setIndex(String.valueOf(Integer.parseInt(navigateList.get(LauncherService.getInstance().getFirstIndexForNormal()).getIndex())+1));

        List<GroupElement> groupElementList = new ArrayList<>();
        if (null != mProfileCustomization.getControlInfos() && mProfileCustomization.getControlInfos().size() > 0){
            for (int i = 0;i < mProfileCustomization.getControlInfos().size();i++){
                ControlInfo controlInfo = mProfileCustomization.getControlInfos().get(i);

                List<SubjectVodsList> subjectVODLists = new ArrayList<>();
                List<VOD> vods = new ArrayList<>();
                SubjectVodsList subjectVodsList = new SubjectVodsList();
                GroupElement groupElement = new GroupElement();
                Group group = new Group();
                List<Element> elements = new ArrayList<>();

                if (null != controlInfo.getVodList() && controlInfo.getVodList().size() > 0){
                    for (int j = 0;j < controlInfo.getVodList().size();j++){
                        elements.add(new Element());
                    }
                }

                List<Dialect> nameDialect1 = new ArrayList<>();
                Dialect dialect1 = new Dialect();
                dialect1.setValue(controlInfo.getSubjectName());
                dialect1.setLanguage("zh");
                nameDialect1.add(dialect1);
                group.setNameDialect(nameDialect1);

                com.pukka.ydepg.common.http.v6bean.v6node.ControlInfo controlInfo1 = new com.pukka.ydepg.common.http.v6bean.v6node.ControlInfo();
                //controlInfo1.setControlId(controlInfo.getControlID());
                //列表类型 枚举值：0人工推荐，1智能推荐, 2观看记录
                if ("2".equalsIgnoreCase(controlInfo.getTypes())){//==2时，为多profile的观看记录，单独处理
                    controlInfo1.setControlId("PannelPoster_Profile_BookMark");
                }else{
                    controlInfo1.setControlId(controlInfo.getControlID());
                }
                group.setControlInfo(controlInfo1);
                //用于PHMAdapter匹配vod数据
                group.setCategoryCode(String.valueOf(i));
                ExtraData extraData = new ExtraData();
                extraData.setTitleColor(controlInfo.getTitleColor());
                group.setExtraData(extraData);

                if (null != controlInfo.getVodList() && controlInfo.getVodList().size() > 0){
                    for (ProfileVOD profileVOD : controlInfo.getVodList()){
                        if (null == profileVOD){
                            continue;
                        }
                        VOD vod = new VOD();
                        vod.setProfileVod(true);
                        vod.setPrimaryAccount(mProfileCustomization.getPrimaryAccount());
                        vod.setCode(profileVOD.getCode());
                        vod.setCpId(profileVOD.getCpId());
                        vod.setID(profileVOD.getId());
                        vod.setName(profileVOD.getName());
                        vod.setPicture(profileVOD.getPicture());
                        //profileVOD.getRangeTime();
                        vod.setSeriesType(profileVOD.getSeriesType());
                        vod.setVODType(profileVOD.getVodType());
                        vod.setCustomFields(profileVOD.getCustomFields());
                        vod.setBookmark(profileVOD.getBookmark());

                        vods.add(vod);
                    }
                    subjectVodsList.setVodList(vods);
                    subjectVodsList.setSubjectID(group.getCategoryCode());
                    subjectVODLists.add(subjectVodsList);
                }

                groupElement.setElement(elements);
                groupElement.setGroup(group);
                groupElement.setSubjectVODLists(subjectVODLists);
                groupElement.setType("-10");

                if (null != vods && vods.size() > 0){
                    groupElementList.add(groupElement);
                }
            }
        }
        if (groupElementList.size() > 0){
            isHadAdd = true;
            mOldNavigate = navigate;
            mOldGroupElementList = groupElementList;

            if (isHadAddPbsData(groupElements)){
                if (LauncherService.getInstance().getFirstIndexForNormal() + 1 < navigateList.size()){
                    Navigate navigate1 = navigateList.get(LauncherService.getInstance().getFirstIndexForNormal() + 1);
                    navigate1.setPageList(navigate.getPageList());
                }
                //如果已经存在爱看数据，
                groupElements.remove(LauncherService.getInstance().getFirstIndexForNormal()+1);
                groupElements.add(LauncherService.getInstance().getFirstIndexForNormal()+1,groupElementList);
                if (!isFresh){
                    OTTApplication.getContext().setIsRefreshPbsEpg(true);
                    OTTApplication.getContext().getMainActivity().setIsChangeTab(true);
                }

                launcherListener.onVersionChange(false,true,false);
                if (!isFresh){
                    //OTTApplication.getContext().setIsRefreshPbsEpg(true);
                    OTTApplication.getContext().getMainActivity().notifyDataSetChanged(2);
                }

            }else{
                //没有添加过爱看数据
                navigateList.add(0,navigate);
                groupElements.add(LauncherService.getInstance().getFirstIndexForNormal()+1,groupElementList);
                LauncherService.getInstance().updateFirstIndex();
                //此方法须保证在主线程执行
                //launcherListener.onVersionChange(false,isFresh,false);
                if (OTTApplication.getContext().getCurrentActivity() instanceof MainActivity){
                    ((MainActivity)OTTApplication.getContext().getCurrentActivity()).notifyDataSetChanged(1);
                }
            }

        }
    }

    private static boolean isHadAddPbsData(ArrayList<List<GroupElement>> groupElements){
        for (List<GroupElement> groupElements1 : groupElements){
            for (GroupElement groupElement : groupElements1){
                if (null != groupElement.getType() && groupElement.getType().equalsIgnoreCase("-10")){
                    return true;
                }
            }
        }
        return false;
    }

    public static void removeOldPbsEpgData(List<Navigate> navigateList,ArrayList<List<GroupElement>> groupElements){
        SuperLog.info2SD(TAG,"start removeOldPbsEpgData");

        isHadAdd = false;

        //删除数据中的爱看数据
        List<Navigate> temNavigateList = new ArrayList<>();
        for (Navigate navigates : navigateList){
            if (navigates.getId().equalsIgnoreCase(AIKAN_NAV_ID)){
                temNavigateList.add(navigates);
            }
        }
        for (Navigate navigate : temNavigateList){
            navigateList.remove(navigate);
        }

        ArrayList<List<GroupElement>> temGroupElements = new ArrayList<>();
        for (List<GroupElement> groupElements1 : groupElements){
            for (GroupElement groupElement : groupElements1){
                if (null != groupElement.getType() && groupElement.getType().equalsIgnoreCase("-10")){
                    temGroupElements.add(groupElements1);
                }
            }
        }
        for (List<GroupElement> groupElementList : temGroupElements){
            groupElements.remove(groupElementList);
        }

        LauncherService.getInstance().updateFirstIndex();
        SuperLog.info2SD(TAG,"removeOldPbsEpgData successful");
    }

}