package com.fx.commons.hiberantedao.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.fx.commons.hiberantedao.pagingcom.Page;

/**
 * 数据接口基类
 * @param <T> 类名
 * @param <PK> 类序列号
 */
public abstract interface ZBaseDao<T, PK extends Serializable> {
	
	/**
	  * 托管给spring 需要引入一个配置
	 * @return
	 */
	public Session getCurrentSession();
	
	/**
	  * 开启一个session
	  * 需要手动开关session
	 * @return session
	 */
	public Session openSession();
	
	/**
	  * 关闭openSession
	 * @param session
	 */
	public void closeSession(Session session);
	
	/**
	  * 增加
	 * @param entity 欲增加的实体
	 */
	public void save(T entity);
	
	/**
	  * 修改
	 * @param entity 欲修改的实体
	 */
	public void update(T entity);
	
	
	/**
	  * 删除
	 * @param entity 欲删除的实体
	 */
	public void delete(T entity);
	
	/**
	 * 根据主键删除对象
	 * @param id 欲删除的主键id
	 */
	public void delete(PK id);
	
	/**
	 * 执行HQL进行【批量】修改/删除操作
	 * @param hql "from Users where name=? and password=?"
	 * @param values Map类型命名参数,按名称绑定
	 * @return 更新记录数
	 */
	public int batchExecute(String hql, Map<String, ?> values);
	
	/**
	 * 根据主键ID获取对象，用hibernate的get方法加载
	 * @param id 主键id
	 * @return 实体对象
	 */
	public T get(PK id);
	
	/**
	 * 根据主键ID获取对象，用hibernate的load方法加载
	 * @param id 主键id
	 * @return 实体对象
	 */
	public T find(PK id);
	
	/**
	 * 按属性查找唯一对象,匹配方式为相等
	 * @param fieldName 实体字段名
	 * @param fieldValue 实体属性值
	 * @return 如果匹配到则返回匹配到的实体，反之返回空
	 */
	public T findByField(String fieldName, Object fieldValue);
	
	/**
	 * 按HQL查询唯一对象
	 * @param hql "from Users where name=? and password=?"
	 * @param values 数量可变的参数,按顺序绑定
	 * @return 单个对象/字段
	 */
	public <X> X findObj(String hql, Object... values);
	
	/**
	 * 按HQL查询唯一对象
	 * @param hql "from Users where name=:name and password=:password"
	 * @param values Map类型命名参数,按名称绑定
	 * @return 单个对象/字段
	 */
	public <X> X find(String hql, Map<String, ?> values);
	
	/**
	 * 按属性查找对象列表,匹配方式为相等
	 * @param fieldName 实体字段名
	 * @param fieldValue 实体字段要匹配的值
	 * @return 与该实体值相等的实体列表
	 */
	public List<T> findListByField(String fieldName, Object fieldValue);
	
	/**
	 * 根据指定字段包含的部分值获取列表
	 * 如：获取name字段值在“aaa”,“bbb”,“ccc”之间的列表 
	 * @param fieldName 字段名称
	 * @param values 值列表
	 * @return 列表
	 */
	public List<T> findListByField(String fieldName, List<String> values);
	
	/**
	 * 按HQL查询对象列表
	 * @param hql "from Users where name=? and password=?"
	 * @param values 数量可变的参数,按顺序绑定
	 * @return 对象列表
	 */
	public <X> List<X> findhqlList(String hql, Object... values);
	
	/**
	  * 按HQL查询对象列表
	 * @param hql "from Users where name=:name and password=:password"
	 * @param values Map类型命名参数,按名称绑定
	 * @return 对象列表
	 */
	public <X> List<X> findList(String hql, Map<String, ?> values);
	
	/**
	  *  获取指定条数的指定数量的数据列表
	 * @param hql hql语句
	 * @param values 参数数组。 前两个参数必须是：开始条数、数据条数 
	  *  如：0,10,... 意思是从0条开始，查询数量是10条
	 * @return 数据列表
	 */
	public List<T> hqlListFirstMax(String hql, Object... values);
	
	/**
	  *  获取数据列表（hql中包含in的语句）
	 * @param hql 		hql语句（占位符使用 :v0,:v1...）
	 * @param values 	参数数组
	 * @return 数据列表
	 */
	public <X> List<X> findListIns(String hql, Object... values);
	
	/**
	 * 执行HQL进行【批量】修改/删除操作
	 * @param hql "from Users where name=? and password=?"
	 * @param values 数量可变的参数,按顺序绑定.
	 * @return 更新记录数
	 */
	public int batchExecute(String hql, Object... values);
	
	/**
	 * 批量修改数据（hql中包含in的语句）
	 * @param hql 		hql语句（占位符使用 :v0,:v1...）
	 * @param values 	参数数组
	 * @return 受影响行数
	 */
	public int batchExecuteIns(String hql, Object... values);
	
	/**
	 * 分页查询
	 * @param pageData 分页对象
	 * @return 分页对象
	 */
	public Page<T> findPage(Page<T> pageData);
	
	/**
	 * 多条件排序查询
	 * @param pageData 分页对象
	 * @return 分页对象
	 */
	public Page<T> findPageByOrders(Page<T> pageData);
	
	/**
	 * 多条件排序查询数据数量
	 * @param pageData 分页对象
	 * @return 分页对象（数据列表为空）
	 */
	public long findPageCount(Page<T> pageData);
	
	/**
	 * 刷新当前事物，提交数据
	 */
	public void flush();
	
}