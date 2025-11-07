package com.zk.manager;


/**
 * factory management class, offering the single instance object for management class
 * @author seiya
 *
 */
public class ManagerFactory {

	private static DeviceManager deviceManager = null;
	private static AttLogManager attLogManager = null;
	private static UserInfoManager userInfoManager = null;
	private static BioTemplateManager bioTemplateManager = null;
	private static DeviceCommandManager commandManager = null;
	private static DeviceLogManager logManager = null;
	private static AttPhotoManager attPhotoManager = null;
	private static MessageManager messageManager = null;
	private static DeviceAttrsManager attrsManager = null;
	private static MeetInfoManager meetInfoManager = null;
	private static AdvManager advManager = null;
	
	/**
	 * Get an instance object of device management class
	 * @return
	 */
	public static DeviceManager getDeviceManager() {
		if (null == deviceManager) {
			deviceManager = new DeviceManager();
		}
		return deviceManager;
	}
	
	/**
	 * Get an instance object of AttLog management class
	 * @return
	 */
	public static AttLogManager getAttLogManager() {
		if (null == attLogManager) {
			attLogManager = new AttLogManager();
		}
		return attLogManager;
	}
	
	/**
	 * Get an instance object of user information management class
	 * @return
	 */
	public static UserInfoManager getUserInfoManager() {
		if (null == userInfoManager) {
			userInfoManager = new UserInfoManager();
		}
		return userInfoManager;
	}
	
	/**
	 * Get an instance object of biometrics management class
	 * @return
	 */
	public static BioTemplateManager getBioTemplateManager () {
		if (null == bioTemplateManager) {
			bioTemplateManager = new BioTemplateManager();
		}
		return bioTemplateManager;
	}
	
	/**
	 * Get an instance object of device command management class
	 * @return
	 */
	public static DeviceCommandManager getCommandManager () {
		if (null == commandManager) {
			commandManager = new DeviceCommandManager();
		}
		
		return commandManager;
	}
	
	/**
	 * Get an instance object of device operation log management class
	 * @return
	 */
	public static DeviceLogManager getDeviceLogManager() {
		if (null == logManager) {
			logManager = new DeviceLogManager();
		}
		
		return logManager;
	}
	
	/**
	 * Get an instance object of AttPhoto management class
	 * @return
	 */
	public static AttPhotoManager getAttPhotoManager() {
		if (null == attPhotoManager) {
			attPhotoManager = new AttPhotoManager();
		}
		
		return attPhotoManager; 
	}
	
	/**
	 * Get an instance object of SMS management class
	 * @return
	 */
	public static MessageManager getMessageManager() {
		if (null == messageManager) {
			messageManager = new MessageManager();
		}
		
		return messageManager; 
	}
	
	public static DeviceAttrsManager getAttrsManager() {
		if (null == attrsManager) {
			attrsManager = new DeviceAttrsManager();
		}
		
		return attrsManager;
	}
	
	/**
	 * Get an instance object of MeetInfo management class
	 * @return
	 */
	public static MeetInfoManager getMeetInfoManager() {
		if (null == meetInfoManager) {
			meetInfoManager = new MeetInfoManager();
		}
		
		return meetInfoManager; 
	}

	/**
	 * Get an instance object of Adv management class
	 * @return
	 */
	public static AdvManager getAdvManager() {
		if (null == advManager) {
			advManager = new AdvManager();
		}
		
		return advManager;
	}
}
