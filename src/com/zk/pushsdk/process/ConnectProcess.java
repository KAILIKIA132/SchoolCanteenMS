package com.zk.pushsdk.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

	public void registry(HttpServletRequest request, HttpServletResponse response) {
		try {
			String sn = request.getParameter("SN");
			if (sn == null || sn.isEmpty()) {
				response.getWriter().write("registry=fail");
				return;
			}

			// Check if device exists
			com.zk.pushsdk.po.DeviceInfo device = com.zk.manager.ManagerFactory.getDeviceManager().getDeviceInfoBySn(sn);
			
			if (device == null) {
				// Create new device
				device = new com.zk.pushsdk.po.DeviceInfo();
				device.setDeviceSn(sn);
				device.setDeviceName(request.getParameter("Introduction")); // Device name often sent as Introduction
				if (device.getDeviceName() == null || device.getDeviceName().isEmpty()) {
					device.setDeviceName("Auto-Reg-" + sn);
				}
				device.setIpAddress(request.getRemoteAddr());
				device.setState("Online");
				device.setTransInterval(1);
				
				com.zk.manager.ManagerFactory.getDeviceManager().createDeviceInfo(device);
				System.out.println("Auto-registered new device: " + sn);
			} else {
				// Update IP if changed
				device.setIpAddress(request.getRemoteAddr());
				device.setState("Online");
				com.zk.manager.ManagerFactory.getDeviceManager().updateDeviceInfo(device);
			}

			response.getWriter().write("registry=ok");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().write("registry=fail");
			} catch (java.io.IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void login(HttpServletRequest request, HttpServletResponse response) {
		try {
			// Basic login response to allow device to connect
			response.getWriter().write("Result=OK");
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
}
