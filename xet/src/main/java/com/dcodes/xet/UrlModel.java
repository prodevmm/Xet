package com.dcodes.xet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UrlModel {

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("sdUrl")
@Expose
private String sdUrl;
@SerializedName("hdUrl")
@Expose
private String hdUrl;
@SerializedName("message")
@Expose
private String message;

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
}

public String getSdUrl() {
return sdUrl;
}

public void setSdUrl(String sdUrl) {
this.sdUrl = sdUrl;
}

public String getHdUrl() {
return hdUrl;
}

public void setHdUrl(String hdUrl) {
this.hdUrl = hdUrl;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

}