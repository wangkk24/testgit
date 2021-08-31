package com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.huawei.ott.sdk.log.DebugLog;
import com.pukka.ydepg.moudule.multviewforstb.multiview.module.autoupdate.bean.ResultBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpConnection {
    private static int CONNECTIONTIMEOUT = 15000;
    private static int READTIMEOUT = 15000;

    /**
     * get请求封装
     */
    public static void getRequest(String url, Object object, ResultCallback callback) {
        getRequest(url, object, null, callback);
    }

    public static void getRequest(final String url, final Object object, final String filepath, final ResultCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb = new StringBuffer(url);
                sb.append("?");
                if (object != null) {
                    sb.append(object2Body(object));
                }
                if (callback != null) {
                    HttpURLConnection con = null;
                    OutputStream os = null;
                    try {
                        URL path = new URL(sb.toString());
                        if (path != null) {
                            con = (HttpURLConnection) path.openConnection();
                            con.setRequestMethod("GET");    //设置请求方式
                            con.setConnectTimeout(CONNECTIONTIMEOUT);    //链接超时15秒
                            con.setReadTimeout(READTIMEOUT);
                            con.setDoOutput(true);
                            con.setDoInput(true);
                            os = con.getOutputStream();
                            os.write(sb.toString().getBytes("utf-8"));
                            os.close();
                            if (con.getResponseCode() == 200) {
                                if (!TextUtils.isEmpty(filepath)) {
                                    onSuccessDownloadRespond(callback, con, filepath);
                                } else {
                                    onSuccessRespond(callback, con);
                                }
                            } else if (con.getResponseCode() == 302 || con.getResponseCode() == 301) {
                                onSuccessRedirect(callback, con);
                            } else {
                                onError(callback, "postRequest url" + url + ",  responseCode:" + con.getResponseCode());
                            }
                        }
                    } catch (Exception error) {
                        error.printStackTrace();
                        onError(callback, "getRequest url:" + url + "," + error.toString());
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                }
            }
        }).start();
    }

    public static void postRequest(String url, Object object, ResultCallback callback) {
        HttpConnection.postRequest(url, object, null, null, callback);
    }

    public static void postRequest(String url, Object object, String filepath, ResultCallback callback) {
        HttpConnection.postRequest(url, object, filepath, null, callback);
    }

    public static void postRequest(final String url, final Object object, final String filepath, final String encoding, final ResultCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String encode = null;
                if (encoding != null) {
                    encode = encoding;
                } else {
                    encode = "application/json; charset=utf-8";
                }
                StringBuffer sb = new StringBuffer();
                if (object != null) {
                    sb = sb.append(object2Body(object));
                }
                if (callback != null) {
                    HttpURLConnection con = null;
                    OutputStream outputStream = null;
                    try {
                        URL path = new URL(url);
                        if (path != null) {
                            con = (HttpURLConnection) path.openConnection();
                            con.setRequestMethod("POST");   //设置请求方法POST
                            con.setConnectTimeout(CONNECTIONTIMEOUT);
                            con.setReadTimeout(READTIMEOUT);
                            con.setDoOutput(true);
                            con.setDoInput(true);
                            con.setRequestProperty("Content-Type", encode);
                            byte[] bytes = sb.toString().getBytes("utf-8");
                            outputStream = con.getOutputStream();
                            outputStream.write(bytes);
                            outputStream.flush();
                            outputStream.close();
                            if (con.getResponseCode() == 200) {
                                if (!TextUtils.isEmpty(filepath)) {
                                    onSuccessDownloadRespond(callback, con, filepath);
                                } else {
                                    onSuccessRespond(callback, con);
                                }
                            } else if (con.getResponseCode() == 302 || con.getResponseCode() == 301) {
                                onSuccessRedirect(callback, con);
                            } else {
                                onError(callback, "postRequest url" + url + ",  responseCode:" + con.getResponseCode());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onError(callback, "postRequest url" + url + "," + e.toString());
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                    }
                }
            }
        }).start();
    }

    private static void onError(ResultCallback callback, String error) {
        AsyncRun.runError(callback, error);
    }

    private static void onSuccessRespond(ResultCallback callback, HttpURLConnection con) {
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            inputStream = con.getInputStream();
            baos = new ByteArrayOutputStream();//创建内存输出流
            int len = 0;
            byte[] bytes = new byte[1024];
            if (inputStream != null) {
                while ((len = inputStream.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                }
                String str = new String(baos.toByteArray());
                DebugLog.debug("[HTTPResponse]", "ResultBean:" + str);
                ResultBean response = new ResultBean();
                response.setStateCode(con.getResponseCode());
                response.setBody(str);
                AsyncRun.runSuccess(callback, response);
            }
        } catch (IOException e) {
            onError(callback, "request url:" + con.getURL() + "," + e.toString());
            DebugLog.error("[HTTPResponse]", "getResponse exception:" + e.toString());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    DebugLog.error("[HTTPResponse]", "inputStream.close():" + e1.toString());
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e2) {
                    DebugLog.error("[HTTPResponse]", "fos.close():" + e2.toString());
                }
            }
        }
    }

    private static void onSuccessDownloadRespond(ResultCallback callback, HttpURLConnection con, String filepath) {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            inputStream = con.getInputStream();
            File file = new File(filepath);
            fos = new FileOutputStream(file);//创建内存输出流
            byte[] bytes = new byte[2048];
            long sum = 0L;
            long total = con.getContentLength();
            if (inputStream != null) {
                int len;
                while ((len = inputStream.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                    sum += (long) len;
                    AsyncRun.runProgress(callback, total, sum);
                }
            }
            fos.flush();
            ResultBean response = new ResultBean();
            response.setStateCode(con.getResponseCode());
            response.setBody(filepath);
            AsyncRun.runSuccess(callback, response);
        } catch (IOException e) {
            onError(callback, "request url:" + con.getURL() + "," + e.toString());
            DebugLog.error("[HTTPResponse]", new String[]{"writetoDisk write localfile exception" + e.toString()});
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    DebugLog.error("[HTTPResponse]", "inputStream.close():" + e1.toString());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e2) {
                    DebugLog.error("[HTTPResponse]", "fos.close():" + e2.toString());
                }
            }
        }
    }

    /**
     * 302 重定向 success 返回url地址
     *
     * @param callback
     * @param con
     */
    private static void onSuccessRedirect(ResultCallback callback, HttpURLConnection con) {
        Map<String, List<String>> headerFields = con.getHeaderFields();
        ResultBean response = new ResultBean();
        try {
            response.setStateCode(con.getResponseCode());
            if (headerFields != null) {
                List<String> urlList = (List) headerFields.get("Location");
                response.setBody(urlList != null && !urlList.isEmpty() ? (String) urlList.get(0) : null);
            }
            AsyncRun.runSuccess(callback, response);
        } catch (IOException e) {
            e.printStackTrace();
            onError(callback, "request url:" + con.getURL() + "," + e.toString());
        }
    }

    public static String object2Body(Object object) {
        String body = null;
        if (object instanceof String) {
            body = (String) object;
        } else if (object instanceof Map) {
            body = map2Body((Map) object);
        } else {
            Gson gson = new Gson();
            body = gson.toJson(object);
        }
        return body;
    }

    public static String map2Body(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
