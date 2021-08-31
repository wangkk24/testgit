package com.pukka.ydepg.launcher.http.hecaiyun.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryResponse {

    private Result result;

    @SerializedName("boothCarouselCont")
    private List<BoothCarouselCont> listBoothCarouselCont;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<BoothCarouselCont> getListBoothCarouselCont() {
        return listBoothCarouselCont;
    }

    public void setListBoothCarouselCont(List<BoothCarouselCont> listBoothCarouselCont) {
        this.listBoothCarouselCont = listBoothCarouselCont;
    }
}
