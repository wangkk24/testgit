package com.pukka.ydepg.launcher.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.Element;
import com.pukka.ydepg.common.http.v6bean.v6node.Group;
import com.pukka.ydepg.common.http.v6bean.v6node.Launcher;
import com.pukka.ydepg.common.http.v6bean.v6node.Navigate;
import com.pukka.ydepg.common.http.v6bean.v6node.Page;
import com.pukka.ydepg.common.utils.CollectionUtil;
import com.pukka.ydepg.common.utils.ConfigUtil;
import com.pukka.ydepg.common.utils.JsonParse;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.common.utils.fileutil.FileUtil;
import com.pukka.ydepg.launcher.bean.GroupElement;
import com.pukka.ydepg.launcher.mvp.contact.ChildLauncherContact;
import com.pukka.ydepg.launcher.util.ComparatorIndex;
import com.pukka.ydepg.launcher.util.TemplateFactory;
import com.pukka.ydepg.moudule.featured.view.LauncherService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 二级页面逻辑处理
 *
 * @FileName: com.pukka.ydepg.launcher.mvp.presenter.ChildLauncherPresenter.java
 * @author: luwm
 * @data: 2018-05-16 16:44
 * @Version V1.0 <描述当前版本功能>
 */
public class ChildLauncherPresenter extends AuthenticatePresenter<ChildLauncherContact.IChildLauncherView> implements ChildLauncherContact.IChildLauncherPresenter {
    private static final String TAG = "ChildLauncherPresenter";
    private String FILE_NAME = "";
    private static String SAVED_FILE = "";
    private String pageId;
    private Launcher mLauncher;
    private ArrayList<List<GroupElement>> mGroupElements = new ArrayList<>();


    /**
     * 判断YSLOG下是否存在launcher.json
     *
     * @return
     */
    public boolean isFileExist() {
        File file = new File(SAVED_FILE);
        return file.exists();
    }

    /**
     * get the content of launcher dynamic resource
     * from the json file every time.
     *
     * @return
     */
    public String getFileContent() {
        File file = new File(SAVED_FILE);
        String fileContent = "";
        if (file.exists()) {
            fileContent = FileUtil.readFileByLines(SAVED_FILE);
            SuperLog.debug(TAG, "file" + FileUtil.getCanonicalPath(file));
        } else {
            InputStream inputStream = null;
            try {
                inputStream = OTTApplication.getContext().getAssets().open(FILE_NAME);
                fileContent = FileUtil.getContent(inputStream);
            } catch (IOException e) {
                SuperLog.error(TAG, e);
            } finally {
                FileUtil.closeInputStream(inputStream);
            }
        }
        return fileContent;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
        if (!pageId.endsWith(".json")) {
            FILE_NAME = pageId + ".json";
        }
        SAVED_FILE = OTTApplication.getCachePath();
        SAVED_FILE = SAVED_FILE + FILE_NAME;
    }

    public String getSavedFilePath() {
        return SAVED_FILE;
    }

    /**
     * 解析模板
     */
    public void parseLauncher() {
        Launcher launcher = JsonParse.json2Object(getFileContent(), Launcher.class);
        LauncherService.getInstance().addChildLauncher(pageId, launcher);
    }


    public void setGroupElements(ArrayList<List<GroupElement>> groupElements) {
        this.mGroupElements = groupElements;
        LauncherService.getInstance().addChildGroupElements(pageId, groupElements);
    }

    /**
     * 获取桌面对象
     *
     * @return
     */
    public Launcher getLauncher() {
        mLauncher = LauncherService.getInstance().getChildLauncher(pageId);
        if (mLauncher == null) {
            parseLauncher();
            mLauncher = LauncherService.getInstance().getChildLauncher(pageId);
        }
        return mLauncher;
    }

    /**
     * 获取group
     *
     * @return
     */
    public ArrayList<List<GroupElement>> getGroupElements() {
        try {
            return mGroupElements;
        } finally {
        }
    }

    /**
     * 解析模板中的group和element数据
     */
    public void parseGroupElements() {

        ArrayList<List<GroupElement>> tempGroupElements = new ArrayList<>();
        Launcher launcher = getLauncher();
        if (launcher != null) {
            List<Navigate> navigates = launcher.getNavigateList();
            int navCount = navigates.size();
            for (int i = 0; i < navCount; i++) {
                List<Page> pageList = launcher.getNavigateList().get(i).getPageList();
                if (CollectionUtil.isEmpty(pageList)) {
                    tempGroupElements.add(new ArrayList<GroupElement>());
                    continue;
                }
                List<Element> elements = pageList.get(0).getElementList();
                List<Group> groups = launcher.getGroupList();
                if (CollectionUtil.isEmpty(elements) || CollectionUtil.isEmpty(groups)) {
                    tempGroupElements.add(new ArrayList<GroupElement>());
                    continue;
                }
                List<GroupElement> groupElementList = getGroupElements(elements, groups);
                tempGroupElements.add(groupElementList);
            }
            setGroupElements(tempGroupElements);
        }
    }

    /**
     * 动态生成GroupElements
     */
    private List<GroupElement> getGroupElements(List<Element> elements, List<Group> groups) {
        List<GroupElement> groupElements = new ArrayList<>();
        int count = groups.size();
        //获取该页面元素的groupid列表
        List<String> groupidList = new ArrayList<>();
        int elementSize = elements.size();
        for (int i = 0; i < elementSize; i++) {
            String groupid = elements.get(i).getGroupID();
            if (!groupidList.contains(groupid)) {
                groupidList.add(groupid);
            }
        }
        //遍历所有group 将数据组装到groupElement里面
        for (int j = 0; j < count; j++) {
            String groupid = groups.get(j).getId();
            if (groupidList.contains(groupid)) {
                GroupElement groupElement = new GroupElement();
                String controlInfo = groups.get(j).getControlInfo().getControlId();
                //过滤掉客户端不识别的模板
                //if (!TextUtils.isEmpty(controlInfo) && TemplateFactory.getsResourceIdMap().containsKey(controlInfo)) {
                groupElement.setGroup(groups.get(j));
                List<Element> elementsListTemp = new ArrayList<>();
                for (int i = 0; i < elementSize; i++) {
                    if (elements.get(i).getGroupID().equals(groups.get(j).getId())) {
                        elementsListTemp.add(elements.get(i));
                    }
                }
                Collections.sort(elementsListTemp, new ComparatorIndex());
                groupElement.setElement(elementsListTemp);
                groupElements.add(groupElement);
                //}
            }
        }
        return groupElements;
    }

    @Override
    public void loadLauncher() {
        if (null == LauncherService.getInstance().getChildGroupElement(pageId)) {
            parseGroupElements();
        }
        if (null != mView){
            mView.loadLauncherData(null == getLauncher() ? null : getLauncher().getNavigateList(), LauncherService.getInstance().getChildGroupElement(pageId));
        }
    }

    public void queryLauncher(String pageId, boolean isFirst, Context context,boolean isLoad) {
        updateLauncherLink();
        String launcherLink;
        if (!TextUtils.isEmpty(LauncherService.getInstance().getQueryPhmLauncher()) && LauncherService.getInstance().getQueryPhmLauncher().equalsIgnoreCase("1")){
            List<String> desktopIDs = new ArrayList<>();
            desktopIDs.add(pageId);
            LauncherService.getInstance().getChildLauncherLink(context, desktopIDs, new TabItemPresenter.OnQueryLauncherListener() {
                @Override
                public void onVersionChange(boolean change, String launcherName) {
                    if (change || !isFileExist()){
                        if (TextUtils.isEmpty(launcherName)){
                            SuperLog.info2SD(TAG,"child launcherlink is null,and don't to download json");
                            loadLauncher();
                            return;
                        }
                        downloadChildLauncher(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + launcherName,isFirst,isLoad);
                    }else{
                        if (!isFileExist() && !TextUtils.isEmpty(launcherName)){
                            downloadChildLauncher(ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + launcherName,isFirst,isLoad);
                        }else{
                            loadLauncher();
                        }
                    }
                }
            });
        }else if (TextUtils.isEmpty(LauncherService.getInstance().getQueryPhmLauncher()) || LauncherService.getInstance().getQueryPhmLauncher().equalsIgnoreCase("0")){
            if (pageId.toLowerCase().endsWith(".json")) {
                launcherLink = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + SharedPreferenceUtil.getInstance().getLauncherNewLink()
                        + "/pages/" + pageId;
            } else {
                launcherLink = ConfigUtil.getConfig(OTTApplication.getContext()).getEdsURL() + SharedPreferenceUtil.getInstance().getLauncherNewLink()
                        + "/pages/" + pageId + ".json";
            }
            downloadChildLauncher(launcherLink,isFirst,isLoad);
        }

    }

    private void downloadChildLauncher(String launcherLink,boolean isFirst,boolean isLoad){
        if (TextUtils.isEmpty(launcherLink) || launcherLink.contains("null")){
            return;
        }
        SuperLog.debug(TAG, "downloadChildLauncher Url:" + launcherLink + " , isFirst :" + isFirst);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(launcherLink)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    String content = response.body().string();
                    Launcher launcher = JsonParse.json2Object(content, Launcher.class);
                    if (null != launcher) {
                        File file = new File(getSavedFilePath());
                        if (!file.exists()) {
                            FileUtil.fileCreate(file);
                        }
                        FileUtil.saveContentToFile(getSavedFilePath(), content);
                        LauncherService.getInstance().addChildLauncher(pageId, launcher);
                        parseGroupElements();
                        if (isLoad){
                            if (isFirst) {
                                loadLauncher();
                            } else {
                                mView.updateLauncher();
                            }
                        }else{
                            mView.downLoadLauncher();
                        }
                    }
                }
            }
        });
    }

    private void updateLauncherLink(){
        SuperLog.info2SD(TAG,"getLauncherNewLink="+SharedPreferenceUtil.getInstance().getLauncherNewLink()+";getLauncherLink="+SharedPreferenceUtil.getInstance().getLauncherLink());
        if (!SharedPreferenceUtil.getInstance().getLauncherNewLink().equals(SharedPreferenceUtil.getInstance().PHM_LAUNCHER_LINK)
                && !SharedPreferenceUtil.getInstance().getLauncherNewLink().equals(SharedPreferenceUtil.getInstance().getLauncherLink()))
            SharedPreferenceUtil.getInstance().saveLauncherLink(SharedPreferenceUtil.getInstance().getLauncherNewLink());
    }
}