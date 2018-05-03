package com.elitel.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.esri.arcgis.geoprocessing.GeoProcessor;

public class MyServletInheritJavaxServletContext implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	
		Timer timer = new Timer();
		timer.schedule(new MyTimerTask(), 10 * 1000);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			CustomerToolbox.getInstance();
		}
	}
	
}
