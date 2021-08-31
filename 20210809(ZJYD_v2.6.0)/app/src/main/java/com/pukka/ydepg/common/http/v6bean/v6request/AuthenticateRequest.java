package com.pukka.ydepg.common.http.v6bean.v6request;

import com.pukka.ydepg.common.http.v6bean.v6node.AuthenticateDevice;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthenticateTolerant;
import com.google.gson.annotations.SerializedName;
import com.pukka.ydepg.common.http.v6bean.v6node.AuthenticateBasic;
import com.pukka.ydepg.common.http.v6bean.v6node.NamedParameter;

import java.util.List;

/**
 * Created by wgy on 2017/4/24.
 */

public class AuthenticateRequest {

    /**
     * authenticateBasic :
     * authenticateDevice :
     * authenticateTolerant :
     * extensionFields :
     */

    /**
     *基本认证参数
     */
    @SerializedName("authenticateBasic")
    private AuthenticateBasic authenticateBasic;

    /**
     *设备相关认证参数
     */
    @SerializedName("authenticateDevice")
    private AuthenticateDevice authenticateDevice;

    /**
     *用户容灾相关认证参数
     */
    @SerializedName("authenticateTolerant")
    private AuthenticateTolerant authenticateTolerant;

    /**
     *扩展信息
     */
    @SerializedName("extensionFields")
    private List<NamedParameter> extensionFields;

    public AuthenticateBasic getAuthenticateBasic() {
        return authenticateBasic;
    }

    public void setAuthenticateBasic(AuthenticateBasic authenticateBasic) {
        this.authenticateBasic = authenticateBasic;
    }

    public AuthenticateDevice getAuthenticateDevice() {
        return authenticateDevice;
    }

    public void setAuthenticateDevice(AuthenticateDevice authenticateDevice) {
        this.authenticateDevice = authenticateDevice;
    }

    public AuthenticateTolerant getAuthenticateTolerant() {
        return authenticateTolerant;
    }

    public void setAuthenticateTolerant(AuthenticateTolerant authenticateTolerant) {
        this.authenticateTolerant = authenticateTolerant;
    }

    public List<NamedParameter> getExtensionFields() {
        return extensionFields;
    }

    public void setExtensionFields(List<NamedParameter> extensionFields) {
        this.extensionFields = extensionFields;
    }
}
