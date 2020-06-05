package com.fx.commons.hiberantedao.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.pagingcom.Page;

/**
 * 业务逻辑基类 实现类
 * @param <T> 类名
 * @param <PK> 类序列号
 */
public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {
	public abstract ZBaseDaoImpl<T, PK> getDao();
	
	@Override
	public Session getOpenSession(){
		return getDao().openSession();
	}
	
	@Override
	public void closeOpenSession(Session session){
		getDao().closeSession(session);
	}
	
	@Override
	public void save(T entity) {
		getDao().save(entity);
	}
	
	@Override
	public void update(T entity) {
		getDao().update(entity);
	}
	
	@Override
	public void delete(T entity) {
		getDao().delete(entity);
	}
	
	@Override
	public void delete(PK id) {
		getDao().delete(id);
	}
	
	@Override
	public int batchExecute(String hql, Object... values) {
		return getDao().batchExecute(hql, values);
	}
	
	@Override
	public int batchExecute(String hql, Map<String, ?> values) {
		return getDao().batchExecute(hql, values);
	}
	
	@Override
	public T get(PK id) {
		return getDao().get(id);
	}
	
	@Override
	public T find(PK id) {
		return getDao().find(id);
	}
	
	@Override
	public T findByField(String fieldName, Object fieldValue) {
		return getDao().findByField(fieldName, fieldValue);
	}
	
	@Override
	public <X> X findObj(String hql, Object... values) {
		return getDao().findObj(hql, values);
	}
	
	@Override
	public <X> X find(String hql, Map<String, ?> values) {
		return getDao().find(hql, values);
	}
	
	@Override
	public List<T> findListByField(String fieldName, Object fieldValue) {
		return getDao().findListByField(fieldName, fieldValue);
	}
	
	@Override
	public List<T> findListByField(String fieldName, List<String> values) {
		return getDao().findListByField(fieldName, values);
	}
	
	@Override
	public <X> List<X> findhqlList(String hql, Object... values) {
		return getDao().findhqlList(hql, values);
	}
	
	@Override
	public <X> List<X> findList(String hql, Map<String, ?> values) {
		return getDao().findList(hql, values);
	}
	
	@Override
	public List<T> hqlListFirstMax(String hql, Object... values) {
		return getDao().hqlListFirstMax(hql, values);
	}
	
	@Override
	public Page<T> findPage(Page<T> pageData) {
		return getDao().findPage(pageData);
	}
	
	@Override
	public Page<T> findPageByOrders(Page<T> pageData) {
		return getDao().findPageByOrders(pageData);
	}
	
	@Override
	public long findPageCount(Page<T> pageData) {
		return getDao().findPageCount(pageData);
	}
	
	@Override
	public void flush() {
		getDao().flush();
	}
	
	@Override
    public List<T> findListIns(String hql, Object... values) {
    	return getDao().findListIns(hql, values);
    }
	
	@Override
	public int batchExecuteIns(String hql, Object... values) {
		return getDao().batchExecuteIns(hql, values);
	}
	
}