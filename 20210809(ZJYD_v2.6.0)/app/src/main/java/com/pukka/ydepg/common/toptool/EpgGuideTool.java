package com.pukka.ydepg.common.toptool;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.pukka.ydepg.OTTApplication;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;
import com.pukka.ydepg.common.http.v6bean.v6node.Subject;
import com.pukka.ydepg.common.http.v6bean.v6node.SubjectVODList;
import com.pukka.ydepg.common.utils.LogUtil.SuperLog;
import com.pukka.ydepg.common.utils.datautil.SharedPreferenceUtil;
import com.pukka.ydepg.launcher.mvp.presenter.TabItemPresenter;
import com.pukka.ydepg.moudule.featured.view.LauncherService;
import com.pukka.ydepg.moudule.mytv.presenter.view.EPGGuideDialog;

import java.util.List;

/**
 * Created by Eason on 10-Jun-20.
 */
public class EpgGuideTool {

    private static final String TAG = EpgGuideTool.class.getSimpleName();
    private static final String EPG_GUIDE_SUBJECT_ID = "epg_guide_subjectid";//Example: catauto2000046866
    private TabItemPresenter tabItemPresenter = new TabItemPresenter();

    //节点指引图片
    private List<String> epgGuideBgs;
    private String mEpgGuideVersion = "-1";

    private OnGuideDismissListener onDismissListener;

    public void showGuidePicture(Context context,OnGuideDismissListener onDismissListener){
        this.onDismissListener = onDismissListener;
        if (null != tabItemPresenter){
            try{
                String guideSubjectId = LauncherService.getInstance().getLauncher().getExtraData().get(EPG_GUIDE_SUBJECT_ID);
                if (!TextUtils.isEmpty(guideSubjectId)) {
                    tabItemPresenter.queryEpgTopFunctionMenuList(context,guideSubjectId,new QueryEPGGuideListener());
                } else {
                    this.onDismissListener.onDismiss();
                }
            } catch (Exception e){
                SuperLog.info2SD(TAG,"PHM extra data has not configured [Guide SubjectID]");
                this.onDismissListener.onDismiss();
            }
        }
    }

    //查询Epg Guide picture
    private class QueryEPGGuideListener implements EpgTopFunctionMenu.OnQueryEPGFunctionListener {
        @Override
        public void getEPGFunctionData(List<SubjectVODList> subjectVODLists) {
            if (null != subjectVODLists && subjectVODLists.size() > 0 && null != subjectVODLists.get(0).getSubject()
                    && null != subjectVODLists.get(0).getSubject().getPicture()
                    && null != subjectVODLists.get(0).getSubject().getPicture().getBackgrounds() && subjectVODLists.get(0).getSubject().getPicture().getBackgrounds().size() > 0){
                Subject subject = subjectVODLists.get(0).getSubject();
                epgGuideBgs = subject.getPicture().getBackgrounds();
                if (null != subject.getCustomFields() && subject.getCustomFields().size() > 0){
                    for (NamedParameter namedParameter : subject.getCustomFields()) {
                        if ("version".equalsIgnoreCase(namedParameter.getKey())){
                            mEpgGuideVersion = namedParameter.getFistItemFromValue();
                            SuperLog.debug(TAG,"epgGuideVersion="+mEpgGuideVersion);
                        }
                    }
                }
            }
            //没有配置开机指引图片，不走后续profile检测流程
            showGuideDialog();
        }

        @Override
        public void getEPGFunctionDataFail() {
            showGuideDialog();
        }
    }

    private boolean isToShowGuideView(){
        if (null != epgGuideBgs && epgGuideBgs.size() > 0){
            return true;
        }else{
            SuperLog.error(TAG,"PHM extra data [Guide SubjectID] has not configured [Guide picture]");
            return false;
        }
    }

    private void showGuideDialog(){
        String epgGuideVersion = SharedPreferenceUtil.getInstance().getEpgGuideVersion();
        if (isToShowGuideView() && (TextUtils.isEmpty(epgGuideVersion) || !epgGuideVersion.equalsIgnoreCase(mEpgGuideVersion))){
            SharedPreferenceUtil.getInstance().saveEpgGuideVersion(mEpgGuideVersion);
            EPGGuideDialog epgGuideDialog = new EPGGuideDialog(OTTApplication.getContext().getMainActivity(), ()->onDismissListener.onDismiss());
            epgGuideDialog.setEpgGuideBgs(epgGuideBgs);
            epgGuideDialog.show();
            //解决开机节点指引关闭后仍能看到一瞬间开机等待页面
            OTTApplication.getContext().getMainActivity().mWelcomeRelativeLayiout.setVisibility(View.GONE);
        } else {
            //无指引图片,结束指引图片展示流程
            onDismissListener.onDismiss();
        }
    }

    public interface OnGuideDismissListener{
        void onDismiss();
    }
}