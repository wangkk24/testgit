/*
 *Copyright (C) 2017 广州易杰科技, Inc.
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.pukka.ydepg.common.http.v6bean.v6response;

import com.pukka.ydepg.common.http.v6bean.v6node.Profile;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * File description
 *
 * @author fuqiang Email： fuqiang@easier.cn
 * @version : 1.0
 * @Title: QueryProfileResponse
 * @Package com.pukka.ydepg.common.http.v6bean.v6response
 * @date 2017/10/26 17:30
 */
public class QueryProfileResponse extends BaseResponse{

  /**
   * result : {"retCode":"000000000","retMsg":"Successfully"}
   * userToken : 3sqvf3sqvf1uhzxrdTx7iaJ9E6caaGO1
   * total : 1
   * profiles : [{"isNeedSubscribePIN":"1","isSendSMSForReminder":"0","profilePINEnable":"1","hasCollectUserPreference":"0","isDisplayInfoBar":"0","isShowMessage":"0","isReceiveSMS":"1","pushStatus":"1","subscriberID":"easier1","leadTimeForSendReminder":"5","profileType":"0","multiscreenEnable":"0","isFilterLevel":"0","purchaseEnable":"1","ratingName":"NC-17","loginName":"easier1","quota":"-1","ratingID":"4","name":"Admin","ID":"easier1","receiveADType":"1","lang":"en","reminderInterval":"5"}]
   * subscriberID : easier1
   */
  @SerializedName("userToken")
  private String userToken;
  @SerializedName("total")
  private String total;
  @SerializedName("subscriberID")
  private String subscriberID;
  @SerializedName("profiles")
  private List<Profile> profiles;

  public String getUserToken() {
    return userToken;
  }

  public void setUserToken(String userToken) {
    this.userToken = userToken;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getSubscriberID() {
    return subscriberID;
  }

  public void setSubscriberID(String subscriberID) {
    this.subscriberID = subscriberID;
  }

  public List<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<Profile> profiles) {
    this.profiles = profiles;
  }

  public static class ResultBean {
    /**
     * retCode : 000000000
     * retMsg : Successfully
     */
    @SerializedName("retCode")
    private String retCode;
    @SerializedName("retMsg")
    private String retMsg;

    public String getRetCode() {
      return retCode;
    }

    public void setRetCode(String retCode) {
      this.retCode = retCode;
    }

    public String getRetMsg() {
      return retMsg;
    }

    public void setRetMsg(String retMsg) {
      this.retMsg = retMsg;
    }
  }

  /*public static class ProfilesBean {
    *//**
     * isNeedSubscribePIN : 1
     * isSendSMSForReminder : 0
     * profilePINEnable : 1
     * hasCollectUserPreference : 0
     * isDisplayInfoBar : 0
     * isShowMessage : 0
     * isReceiveSMS : 1
     * pushStatus : 1
     * subscriberID : easier1
     * leadTimeForSendReminder : 5
     * profileType : 0
     * multiscreenEnable : 0
     * isFilterLevel : 0
     * purchaseEnable : 1
     * ratingName : NC-17
     * loginName : easier1
     * quota : -1
     * ratingID : 4
     * name : Admin
     * ID : easier1
     * receiveADType : 1
     * lang : en
     * reminderInterval : 5
     *//*
    @SerializedName("isNeedSubscribePIN")
    private String isNeedSubscribePIN;
    @SerializedName("isSendSMSForReminder")
    private String isSendSMSForReminder;
    @SerializedName("profilePINEnable")
    private String profilePINEnable;
    @SerializedName("hasCollectUserPreference")
    private String hasCollectUserPreference;
    @SerializedName("isDisplayInfoBar")
    private String isDisplayInfoBar;
    @SerializedName("isShowMessage")
    private String isShowMessage;
    @SerializedName("isReceiveSMS")
    private String isReceiveSMS;
    @SerializedName("pushStatus")
    private String pushStatus;
    @SerializedName("subscriberID")
    private String subscriberID;
    @SerializedName("leadTimeForSendReminder")
    private String leadTimeForSendReminder;
    @SerializedName("profileType")
    private String profileType;
    @SerializedName("multiscreenEnable")
    private String multiscreenEnable;
    @SerializedName("isFilterLevel")
    private String isFilterLevel;
    @SerializedName("purchaseEnable")
    private String purchaseEnable;
    @SerializedName("ratingName")
    private String ratingName;
    @SerializedName("loginName")
    private String loginName;
    @SerializedName("quota")
    private String quota;
    @SerializedName("ratingID")
    private String ratingID;
    @SerializedName("name")
    private String name;
    @SerializedName("ID")
    private String ID;
    @SerializedName("receiveADType")
    private String receiveADType;
    @SerializedName("lang")
    private String lang;
    @SerializedName("reminderInterval")
    private String reminderInterval;

    public String getIsNeedSubscribePIN() {
      return isNeedSubscribePIN;
    }

    public void setIsNeedSubscribePIN(String isNeedSubscribePIN) {
      this.isNeedSubscribePIN = isNeedSubscribePIN;
    }

    public String getIsSendSMSForReminder() {
      return isSendSMSForReminder;
    }

    public void setIsSendSMSForReminder(String isSendSMSForReminder) {
      this.isSendSMSForReminder = isSendSMSForReminder;
    }

    public String getProfilePINEnable() {
      return profilePINEnable;
    }

    public void setProfilePINEnable(String profilePINEnable) {
      this.profilePINEnable = profilePINEnable;
    }

    public String getHasCollectUserPreference() {
      return hasCollectUserPreference;
    }

    public void setHasCollectUserPreference(String hasCollectUserPreference) {
      this.hasCollectUserPreference = hasCollectUserPreference;
    }

    public String getIsDisplayInfoBar() {
      return isDisplayInfoBar;
    }

    public void setIsDisplayInfoBar(String isDisplayInfoBar) {
      this.isDisplayInfoBar = isDisplayInfoBar;
    }

    public String getIsShowMessage() {
      return isShowMessage;
    }

    public void setIsShowMessage(String isShowMessage) {
      this.isShowMessage = isShowMessage;
    }

    public String getIsReceiveSMS() {
      return isReceiveSMS;
    }

    public void setIsReceiveSMS(String isReceiveSMS) {
      this.isReceiveSMS = isReceiveSMS;
    }

    public String getPushStatus() {
      return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
      this.pushStatus = pushStatus;
    }

    public String getSubscriberID() {
      return subscriberID;
    }

    public void setSubscriberID(String subscriberID) {
      this.subscriberID = subscriberID;
    }

    public String getLeadTimeForSendReminder() {
      return leadTimeForSendReminder;
    }

    public void setLeadTimeForSendReminder(String leadTimeForSendReminder) {
      this.leadTimeForSendReminder = leadTimeForSendReminder;
    }

    public String getProfileType() {
      return profileType;
    }

    public void setProfileType(String profileType) {
      this.profileType = profileType;
    }

    public String getMultiscreenEnable() {
      return multiscreenEnable;
    }

    public void setMultiscreenEnable(String multiscreenEnable) {
      this.multiscreenEnable = multiscreenEnable;
    }

    public String getIsFilterLevel() {
      return isFilterLevel;
    }

    public void setIsFilterLevel(String isFilterLevel) {
      this.isFilterLevel = isFilterLevel;
    }

    public String getPurchaseEnable() {
      return purchaseEnable;
    }

    public void setPurchaseEnable(String purchaseEnable) {
      this.purchaseEnable = purchaseEnable;
    }

    public String getRatingName() {
      return ratingName;
    }

    public void setRatingName(String ratingName) {
      this.ratingName = ratingName;
    }

    public String getLoginName() {
      return loginName;
    }

    public void setLoginName(String loginName) {
      this.loginName = loginName;
    }

    public String getQuota() {
      return quota;
    }

    public void setQuota(String quota) {
      this.quota = quota;
    }

    public String getRatingID() {
      return ratingID;
    }

    public void setRatingID(String ratingID) {
      this.ratingID = ratingID;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getID() {
      return ID;
    }

    public void setID(String ID) {
      this.ID = ID;
    }

    public String getReceiveADType() {
      return receiveADType;
    }

    public void setReceiveADType(String receiveADType) {
      this.receiveADType = receiveADType;
    }

    public String getLang() {
      return lang;
    }

    public void setLang(String lang) {
      this.lang = lang;
    }

    public String getReminderInterval() {
      return reminderInterval;
    }

    public void setReminderInterval(String reminderInterval) {
      this.reminderInterval = reminderInterval;
    }
  }*/
}
