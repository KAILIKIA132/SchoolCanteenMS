package com.zk.dao;

import java.util.List;

import com.zk.exception.DaoException;

/**
 * Database Access Layer Interface
 * 
 * @author seiya
 *
 * @param <T>
 */
public interface IBaseDao<T> {

	/**
	 * Add new records to the database
	 * @param entity
	 * Data Objects
	 * @throws DaoException
	 */
	public void add(T entity) throws DaoException;

	/**
	 * Update Database Record
	 * @param entity
	 * @throws DaoException
	 */
	public void update(T entity) throws DaoException;

	/**
	 * Delete data objects by ID
	 * @param id
	 * @throws DaoException
	 */
	public void delete(int id) throws DaoException;
	
	/**
	 * Delete data objects by specific conditions
	 * @param cond
	 * @throws DaoException
	 */
	public void delete(String cond) throws DaoException;

	/**
	 * Get data objects by ID
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public T fatch(int id) throws DaoException;

	/**
	 * Get Data List object which Correct matching by specific conditions
	 * @param cond
	 * @return
	 * @throws DaoException
	 */
	public List<T> fatchList(String cond) throws DaoException;
	
	/**
	 * Clear database table data
	 * @return
	 * @throws DaoException
	 */
	public int truncate() throws DaoException;
}
