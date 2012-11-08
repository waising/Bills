package com.bosssoft.bills.tools;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/** 
 *
 * @ClassName   类名：Tools 
 * @Description 功能说明：
 * TODO
 ************************************************************************
 * @date        创建日期：2012-11-1
 * @author      创建人：wwx
 * @version     版本号：V1.0
 *<p>
 ***************************修订记录*************************************
 * 
 *   2012-11-1   wwx   创建该类功能。
 *
 ************************************************************************
 *</p>
 */
public class Tools {

	//校验url地址
	private static String returnUrl(String url,String method,Map m) throws UnsupportedEncodingException{
		
		StringBuilder params= new StringBuilder();
		StringBuilder reqUrl = new StringBuilder();
		
		String param ="",sign="&&";
		//拼接参数 并且encode
		 for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
			 if (iter.hasNext()){
				 params.append((m.size()>1) ? sign:"");
				 param = iter.next().toString();
				 params.append(param).append("=").append(URLEncoder.encode(m.get(param).toString(),"UTF-8"));
			 }
		 }
		
		 //全url
		if (url.equals("")){
			reqUrl.append("url").append("?").append(params.toString());
		}else {
			reqUrl.append(url).append("?").append(params.toString());
		}
		
		return reqUrl.toString();
				
	}
	
	/**
	 * 
	 * 函数名称：createFileDirectory 
	 * 功能说明：创建目录
	 * 参数说明：
	 * @param path
	 * @date   创建时间：2012-11-8
	 * @author 作者：wwx
	 */
    public static void createFileDirectory(String path) {
        if (path != null) {
            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
        }
    }
    
    /**
     * 
     * 函数名称：transDayToTime 
     * 功能说明：将毫秒数转换为天数
     * 参数说明：
     * @param datCount
     * @return
     * @date   创建时间：2012-11-8
     * @author 作者：wwx
     */
    public static long transDayToTime(long datCount) {
        long time = datCount * 24 * 60 * 60 * 1000;
        return time;
    }

}
