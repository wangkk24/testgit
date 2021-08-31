package com.pukka.ydepg.launcher.bean.response;

import com.pukka.ydepg.common.http.v6bean.v6response.BaseResponse;
import com.pukka.ydepg.launcher.bean.node.Configuration;
import com.pukka.ydepg.launcher.bean.node.Parameters;
import com.google.gson.annotations.SerializedName;


/**
 * Login response body
 */
public class LoginResponse extends BaseResponse
{
    private static final String TAG = LoginResponse.class.getSimpleName();

    /**
     * the range of return code
     */
    public interface ReturnCode
    {
        /**
         * Argument illegal
         */
        String ARGUMENT_ILLEGAL = "125010001";
    }

    @SerializedName("vspURL")
    private String vspURL;


    @SerializedName("vspHttpsURL")
    private String vspHttpsURL;
    /**
     * Address of the root certificate
     */
    @SerializedName("rootCerAddr")
    private String rootCerAddr;

    /**
     * Domain of the upgrade server
     */
    @SerializedName("upgradeDomain")
    private String upgradeDomain;

    /**
     * Backup domain of the upgrade server
     */
    @SerializedName("upgradeDomainBackup")
    private String upgradeDomainBackup;

    /**
     * Domain of the management
     */
    @SerializedName("mgmtDomain")
    private String mgmtDomain;

    /**
     * Backup domain of the management
     */
    @SerializedName("mgmtDomainBackup")
    private String mgmtDomainBackup;

    /**
     * Domain of the NTP server
     */
    @SerializedName("NTPDomain")
    private String NTPDomain;

    /**
     * Backup domain of the NTP server
     */
    @SerializedName("NTPDomainBackup")
    private String NTPDomainBackup;


    /**
     * User-related system parameters
     */
    @SerializedName("parameters")
    private Parameters parameters;

    /**
     * Terminal system parameters
     */
    @SerializedName("terminalParm")
    private Configuration terminalParam;


    public String getVSPURL()
    {
        return vspURL;
    }

    public void setVSPURL(String vspURL)
    {
        this.vspURL = vspURL;
    }

    public String getVSPHTTPSURL()
    {
        return vspHttpsURL;
    }

    public void setVSPHTTPSURL(String vspHttpsURL)
    {
        this.vspHttpsURL = vspHttpsURL;
    }

    public String getRootCerAddr()
    {
        return rootCerAddr;
    }

    public void setRootCerAddr(String rootCerAddr)
    {
        this.rootCerAddr = rootCerAddr;
    }

    public String getUpgradeDomain()
    {
        return upgradeDomain;
    }

    public void setUpgradeDomain(String upgradeDomain)
    {
        this.upgradeDomain = upgradeDomain;
    }

    public String getUpgradeDomainBackup()
    {
        return upgradeDomainBackup;
    }

    public void setUpgradeDomainBackup(String upgradeDomainBackup)
    {
        this.upgradeDomainBackup = upgradeDomainBackup;
    }

    public String getMgmtDomain()
    {
        return mgmtDomain;
    }

    public void setMgmtDomain(String mgmtDomain)
    {
        this.mgmtDomain = mgmtDomain;
    }

    public String getMgmtDomainBackup()
    {
        return mgmtDomainBackup;
    }

    public void setMgmtDomainBackup(String mgmtDomainBackup)
    {
        this.mgmtDomainBackup = mgmtDomainBackup;
    }

    public String getNTPDomain()
    {
        return NTPDomain;
    }

    public void setNTPDomain(String NTPDomain)
    {
        this.NTPDomain = NTPDomain;
    }

    public String getNTPDomainBackup()
    {
        return NTPDomainBackup;
    }

    public void setNTPDomainBackup(String NTPDomainBackup)
    {
        this.NTPDomainBackup = NTPDomainBackup;
    }


    public Parameters getParameters()
    {
        return parameters;
    }

    public void setParameters(Parameters parameters)
    {
        this.parameters = parameters;
    }

    public Configuration getTerminalParam()
    {
        return terminalParam;
    }

    public void setTerminalParam(Configuration terminalParam)
    {
        this.terminalParam = terminalParam;
    }

    @Override
    public String toString()
    {
        return "LoginResponse{" + "vspURL='" + vspURL + '\'' + ", " +
                "vspHttpsURL='" + vspHttpsURL + '\'' + ", rootCerAddr='" +
                rootCerAddr + '\'' + ", upgradeDomain='" + upgradeDomain + '\'' + ", upgradeDomainBackup='" + upgradeDomainBackup + '\'' + ", mgmtDomain='" + mgmtDomain +
                '\'' + ", mgmtDomainBackup='" + mgmtDomainBackup+ '\'' + ", "
                + "NTPDomain='" + NTPDomain + '\'' + ", NTPDomainBackup='" +
                NTPDomainBackup + '\'' + ", parameters=" + parameters + ", " +
                "terminalParam=" + terminalParam + '}';
    }
}