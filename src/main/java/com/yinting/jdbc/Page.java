package com.yinting.jdbc;

import java.io.Serializable;

/**
 * ormbase中定义的分页对象.
 * 
 * @author botao.liao
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 1L;
	// 记录总数
	private Integer totalRecord = new Integer(0);
	// 页码
	private Integer targetPage = new Integer(1);
	// 默认每页显示记录数
	private Integer pageSize = new Integer(20);

	/**
	 * @return Returns the page_size.
	 */
	public int getPageSize() {
		return pageSize.intValue();
	}

	/**
	 * @param pageSize
	 *            The pageSize to set.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = new Integer(pageSize);
	}

	/**
	 * @return Returns the targetPage.
	 */
	public int getTargetPage() {
		return targetPage.intValue();
	}

	/**
	 * @param targetPage
	 *            The targetPage to set.
	 */
	public void setTargetPage(int targetPage) {
		this.targetPage = new Integer(targetPage);
	}

	/**
	 * 根据targetPage和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 * 
	 * @return
	 */
	public int getStartRow() {
		return (targetPage - 1) * pageSize;
	}

	/**
	 * 根据targetPage和pageSize计算当前页最后一条记录在总结果集中的位置, 序号从1开始.
	 * 
	 * @return
	 */
	public int getEndRow() {
		return pageSize * targetPage - 1;
	}

	/**
	 * @return Returns the totalPage.
	 */
	public int getTotalPage() {
		if (totalRecord.intValue() <= 0) {
			return 0;
		} else {
			return ((totalRecord.intValue() - 1) / pageSize.intValue()) + 1;
		}
	}

	/**
	 * @return Returns the totalRecord.
	 */
	public int getTotalRecord() {
		return totalRecord.intValue();
	}

	/**
	 * @param totalRecord
	 *            The totalRecord to set.
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = new Integer(totalRecord);
	}

}
