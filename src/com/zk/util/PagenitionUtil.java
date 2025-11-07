package com.zk.util;

import com.zk.pushsdk.util.Constants;

/**
 * Page Paging operation process
 * @author seiya
 *
 */
public class PagenitionUtil {
	private static int pageSize = 10;
	
	/**
	 * Gets the page size from the configuration file. the default value is 10. 
	 */
	static {
		String itemPerPageStr = ConfigUtil.getInstance()
		.getValue(Constants.OPTION_PAGE_SIZE);
		try {
			pageSize = Integer.valueOf(itemPerPageStr);
			if (pageSize <= 0) {
				pageSize = 10;
			}
		} catch (Exception e) {
			pageSize = 10;
		}
	}
	
	public static int getPageSize() {
		return pageSize;
	}
	
	/**
	 * Gets the page count by page size and the count of record.
	 * @param recCount
	 * the count of record
	 * @return
	 * the page count. if it is 0 return 1, other return the value.
	 */
	public static int getPageCount (int recCount) {
		int pageCount = 0;
		if (recCount % pageSize > 0) {
			pageCount = recCount / pageSize + 1;
		} else {
			pageCount = recCount / pageSize;
		}
		return 0 == pageCount ? 1 : pageCount;
	}
	
	/**
	 * Gets current page by page up/page down action.
	 * 
	 * @param inCurPage
	 * Current page
	 * @param pageCount
	 * the count of page
	 * @param act
	 * Action
	 * <li>next：Page down
	 * <li>previous：Page up
	 * @return
	 * Current page
	 */
	private static int getCurPageByAct (int inCurPage, int pageCount, String act) {
		int curPage = inCurPage;
		if ("next".equals(act)) {
			if (curPage + 1 <= pageCount) {
				curPage ++;
			} else {
				curPage = pageCount;
			}
		} else if ("previous".equals(act)){
			if (curPage - 1 <= 0) {
				curPage = 1;
			} else {
				curPage --;
			}
		}
		return curPage;
	}
	
	/**
	 * Gets current page by parameters and page information
	 * @param jumpPage
	 * Jump page
	 * @param act
	 * Action
	 * @param pageCount
	 * Page count
	 * @param curPage
	 * Current page
	 * @return
	 * Current page after action
	 */
	public static int getCurPage(String jumpPage, String act, int pageCount, int curPage) {
		if (null != jumpPage) {
			try {
				curPage = Integer.valueOf(jumpPage);
				if (curPage > pageCount) {
					curPage = pageCount;
				}
			} catch (Exception e) {
			}
		} else if (null != act) {
			curPage = getCurPageByAct(curPage, pageCount, act);
		} else {
			curPage = 1;
		}
		
		return curPage;
	}
}
