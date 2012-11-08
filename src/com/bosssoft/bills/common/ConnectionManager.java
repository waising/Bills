package com.bosssoft.bills.common;

import java.util.ArrayList;

/** 
 *
 * @ClassName   ������ConnectionManager 
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
public class ConnectionManager {
	public static final int MAX_CONNECTIONS = 5;
	 
    /**  
     * �����ܵ��߳��б�  
     */
	private ArrayList<Runnable> active = new ArrayList<Runnable>();
	
	 /**  
     * �ж��߳��б�  
     */  
    private ArrayList<Runnable> queue = new ArrayList<Runnable>();

    private static ConnectionManager instance;

    public static ConnectionManager getInstance() {
         if (instance == null)
              instance = new ConnectionManager();
         return instance;
    }

    /**  
     * ���һ���̵߳��ж���  
     * @param runnable  
     */ 
    public void push(Runnable runnable) {
         queue.add(runnable);
         if (active.size() < MAX_CONNECTIONS)
              startNext();
    }

    /**  
     * ִ���ж�����һ���߳�  
     */
    private void startNext() {
    	//�жӲ�Ϊ��
         if (!queue.isEmpty()) {
              Runnable next = queue.get(0);
              queue.remove(0);
              active.add(next);

              Thread thread = new Thread(next);
              thread.start();
         }
    }

    /**  
     * ִ����ɣ���Runnable���б���ɾ������ִ���ж�����һ��  
     * @param runnable  
     */  
    public void didComplete(Runnable runnable) {
         active.remove(runnable);
         startNext();
    }

}
