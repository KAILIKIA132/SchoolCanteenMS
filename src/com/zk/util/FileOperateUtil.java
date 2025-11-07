package com.zk.util;

import java.io.File;

public class FileOperateUtil {
	/** 
	  * Deletes file. you can delete file or folder
	  *  
	  * @param fileName 
	  *            the file name which you want to delete
	  * @return true and false
	  */  
	 public static boolean delete(String fileName) {  
	  File file = new File(fileName);  
	  if (!file.exists()) {  
	   System.out.println("Delete file failed:" + fileName + "non-existed!");  
	   return false;  
	  } else {  
	   if (file.isFile())  
	    return deleteFile(fileName);  
	   else  
	    return deleteDirectory(fileName);  
	  }  
	 }  
	  
	 /** 
	  * Deletes a single file
	  *  
	  * @param fileName 
	  *            the file name which you want to delete 
	  * @return true and false 
	  */  
	 public static boolean deleteFile(String fileName) {  
	  File file = new File(fileName);  
	// if the file is exist, it is is a file not folder, delete it.  
	  if (file.exists() && file.isFile()) {  
	   if (file.delete()) {  
	    System.out.println("Deletes a single file" + fileName + "successful!");  
	    return true;  
	   } else {  
	    System.out.println("Deletes a single file" + fileName + "failed!");  
	    return false;  
	   }  
	  } else {  
	   System.out.println("Deletes a single file:" + fileName + "non-existed!");  
	   return false;  
	  }  
	 }  
	  
	 /** 
	  * Delete folder and the file in the folder
	  *  
	  * @param dir 
	  *            the file directory
	  * @return true or false
	  */  
	 public static boolean deleteDirectory(String dir) {  
		// if the parameter is not be ended with file delimiter, add the delimiter.
	  if (!dir.endsWith(File.separator))  
	   dir = dir + File.separator;  
	  File dirFile = new File(dir);  
	  // if the parameter is not existed or it is not a directory
	  if ((!dirFile.exists()) || (!dirFile.isDirectory())) {  
	   System.out.println("Delete directory failed:" + dir + "non-existed!");  
	   return false;  
	  }  
	  boolean flag = true;  
	  // Deletes all the file in the directory. including all the sub directory
	  File[] files = dirFile.listFiles();  
	  for (int i = 0; i < files.length; i++) {  
	   // Deletes sub files
	   if (files[i].isFile()) {  
	    flag = FileOperateUtil.deleteFile(files[i].getAbsolutePath());  
	    if (!flag)  
	     break;  
	   }  
	   // Deletes sub directory
	   else if (files[i].isDirectory()) {  
	    flag = FileOperateUtil.deleteDirectory(files[i]  
	      .getAbsolutePath());  
	    if (!flag)  
	     break;  
	   }  
	  }  
	  if (!flag) {  
	   System.out.println("Deletes directory failed!");  
	   return false;  
	  }  
	  // Deletes current directory 
	  if (dirFile.delete()) {  
	   System.out.println("Deletes directory" + dir + "successful!");  
	   return true;  
	  } else {  
	   return false;  
	  }  
	 }  
}
