package com.zk.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.zk.dao.impl.AttPhotoDao;
import com.zk.exception.DaoException;
import com.zk.pushsdk.po.AttPhoto;
import com.zk.util.FileOperateUtil;
/**
 * AttPhoto information management class, can be used for database operation
 * @author seiya
 *
 */
public class AttPhotoManager {
	private static Logger logger = Logger.getLogger(AttPhotoManager.class);
	
	/**
	 * Add AttPhoto information to database
	 * @param list
	 * AttPhoto information list
	 * @return
	 * success: total number of photo numbers will be return; faile: -1 will be return;
	 */
	public int createAttPhoto(List<AttPhoto> list) {
		AttPhotoDao attPhotoDao = new AttPhotoDao();
		try {
			for (AttPhoto attPhoto : list) {
				attPhotoDao.add(attPhoto);
			}
			
			attPhotoDao.commit();
		} catch (DaoException e) {
			attPhotoDao.rollback();
			logger.error(e.toString());
			return -1;
		} finally {
			attPhotoDao.close();
		}
		return list.size();
	}
	
	/**
	 * Delete all the AttPhoto in database
	 * @return
	 * success: AttPhoto list will be return; fail: null will be return
	 */
	public List<AttPhoto> clearAllPhoto() {
		AttPhotoDao attPhotoDao = new AttPhotoDao();
		try {
			List<AttPhoto> attPhotos = attPhotoDao.fatchList("");
			attPhotoDao.delete("");
			attPhotoDao.commit();
			/**Delete attendance photos*/
			for (AttPhoto attPhoto : attPhotos) {
				FileOperateUtil.delete(attPhoto.getFilePath());
			}
			return attPhotos;
		} catch (DaoException e) {
			attPhotoDao.rollback();
			logger.error(e.toString());
		} finally {
			attPhotoDao.close();
		}
		return null;
	}
	
}
