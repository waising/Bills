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
 * @ClassName   ������Tools 
 * @Description ����˵����
 * TODO
 ************************************************************************
 * @date        �������ڣ�2012-11-1
 * @author      �����ˣ�wwx
 * @version     �汾�ţ�V1.0
 *<p>
 ***************************�޶���¼*************************************
 * 
 *   2012-11-1   wwx   �������๦�ܡ�
 *
 ************************************************************************
 *</p>
 */
public class Tools {

	//У��url��ַ
	private static String returnUrl(String url,String method,Map m) throws UnsupportedEncodingException{
		
		StringBuilder params= new StringBuilder();
		StringBuilder reqUrl = new StringBuilder();
		
		String param ="",sign="&&";
		//ƴ�Ӳ��� ����encode
		 for (Iterator iter = m.keySet().iterator(); iter.hasNext();) {
			 if (iter.hasNext()){
				 params.append((m.size()>1) ? sign:"");
				 param = iter.next().toString();
				 params.append(param).append("=").append(URLEncoder.encode(m.get(param).toString(),"UTF-8"));
			 }
		 }
		
		 //ȫurl
		if (url.equals("")){
			reqUrl.append("url").append("?").append(params.toString());
		}else {
			reqUrl.append(url).append("?").append(params.toString());
		}
		
		return reqUrl.toString();
				
	}
	
	/**
	 * 
	 * �������ƣ�createFileDirectory 
	 * ����˵��������Ŀ¼
	 * ����˵����
	 * @param path
	 * @date   ����ʱ�䣺2012-11-8
	 * @author ���ߣ�wwx
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
     * �������ƣ�transDayToTime 
     * ����˵������������ת��Ϊ����
     * ����˵����
     * @param datCount
     * @return
     * @date   ����ʱ�䣺2012-11-8
     * @author ���ߣ�wwx
     */
    public static long transDayToTime(long datCount) {
        long time = datCount * 24 * 60 * 60 * 1000;
        return time;
    }

}
