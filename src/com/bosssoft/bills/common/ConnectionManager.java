package com.bosssoft.bills.common;

import java.util.ArrayList;

/** 
 *
 * @ClassName   类名：ConnectionManager 
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
public class ConnectionManager {
	public static final int MAX_CONNECTIONS = 5;
	 
    /**  
     * 正在跑的线程列表  
     */
	private ArrayList<Runnable> active = new ArrayList<Runnable>();
	
	 /**  
     * 列队线程列表  
     */  
    private ArrayList<Runnable> queue = new ArrayList<Runnable>();

    private static ConnectionManager instance;

    public static ConnectionManager getInstance() {
         if (instance == null)
              instance = new ConnectionManager();
         return instance;
    }

    /**  
     * 添加一个线程到列队中  
     * @param runnable  
     */ 
    public void push(Runnable runnable) {
         queue.add(runnable);
         if (active.size() < MAX_CONNECTIONS)
              startNext();
    }

    /**  
     * 执行列队中下一个线程  
     */
    private void startNext() {
    	//列队不为空
         if (!queue.isEmpty()) {
              Runnable next = queue.get(0);
              queue.remove(0);
              active.add(next);

              Thread thread = new Thread(next);
              thread.start();
         }
    }

    /**  
     * 执行完成，将Runnable从列表中删除，并执行列队中下一个  
     * @param runnable  
     */  
    public void didComplete(Runnable runnable) {
         active.remove(runnable);
         startNext();
    }

}
