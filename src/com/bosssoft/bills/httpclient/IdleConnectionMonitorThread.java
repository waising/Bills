package com.bosssoft.bills.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {
	private final ClientConnectionManager connMgr;
	private volatile boolean shutdown;

	public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
		super();
		this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(5000);
					// 关闭过期连接
					connMgr.closeExpiredConnections();
					// 可选地，关闭空闲超过30秒的连接
					connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
				}
			}
		} catch (InterruptedException ex) {
			// 终止
		}
	}

	public void shutdown() {
		shutdown = true;
		synchronized (this) {
			notifyAll();
		}
	}
}
