package com.fx.commons.hiberantedao.pagingcom;

import java.util.Date;
/**
 * 过滤条件
 */
public class Filtration{
	public static final String OR = "_OR_";
	public static final String AND = "_AND_";//因为or使用比较广泛，and功能暂时没有实现

	private String[] fieldNames = null;
	private Object fieldValue = null;
//	private Class<?> fieldType = null;
	private MatchType matchType = null;

	/**
	 * 构造方法(主要用于页面传值，并由WebUtils调用)
	 * @param filterName 比较属性字符串,含待比较的比较类型、属性值类型及属性列表. 如： LIKES_NAME_OR_EMAIL
	 * @param filterValue 待比较的值.
	 */
//	public Filtration(String filterName, String filterValue){
//		String condition = StringUtils.substringBefore(filterName, "_");//条件
//		String matchTypeCode = condition.substring(0, condition.length() - 1);//获取比较类型
//		String fieldTypeCode = condition.substring(condition.length() - 1, condition.length());//获取值的类型
//
//		matchType = Enum.valueOf(MatchType.class, matchTypeCode);
//		fieldType = Enum.valueOf(FiledType.class, fieldTypeCode).getValue();
//
//		String fieldNameList = StringUtils.substringAfter(filterName, "_");
//
//		fieldNames = fieldNameList.split(Filtration.OR);
//		fieldValue = ConvertUtils.convert(filterValue, fieldType);//转换字符串到相应类型.
//	}
	/**
	 * 构造方法(主要用于自定义过滤条件)
	 * @param matchType 比较类型
	 * @param fieldValue 待比较的值.
	 * @param fieldNames 字段名称
	 */
	public Filtration(MatchType matchType, Object fieldValue, String... fieldNames){
		this.matchType = matchType;
		this.fieldValue = fieldValue;
		this.fieldNames = fieldNames;
	}
	
	/**
	 * 是否比较多个属性
	 */
	public boolean isMultiFilter(){
		return (fieldNames.length > 1);
	}
	
	/**
	 * 获取多个字段名称
	 * @return
	 */
	public String[] getFieldNames(){
		return fieldNames;
	}
	
	/**
	 * 获取唯一字段名称.
	 */
	public String getFieldName(){
		if (fieldNames.length > 1){
			throw new IllegalArgumentException("不仅有一个属性");
		}
		return fieldNames[0];
	}
	
	/**
	 * 获取比较类型
	 * @return
	 */
	public MatchType getMatchType(){
		return matchType;
	}
	
	/**
	 * 获取字段值
	 * @return
	 */
	public Object getFieldValue(){
		return fieldValue;
	}
	
	/**
	 * 比较类型
	 */
	public enum MatchType{
		/**
		 * 获取指定字段【是null】的数据
		 * sql语句自动拼接：is null
		 * fieldValue：""或者null
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.ISNULL, "", "refName"));
		 */
		ISNULL,
		/**
		 * 获取指定字段【不是null】的数据
		 * sql语句自动拼接：is not null
		 * fieldValue：""或者null
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.ISNOTNULL, "", "refName"));
		 */
		ISNOTNULL,
		/**
		 * 获取指定字段【等于】的数据
		 * sql语句自动拼接：=
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.EQ, "33", "refName"));
		 */
		EQ,
		/**
		 * 获取指定字段【不等于】的数据
		 * sql语句自动拼接：<>
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.NE, "33", "refName"));
		 */
		NE,
		/**
		 * 获取指定字段【相似】的数据
		 * sql语句自动拼接：like
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.LIKE, "33", "refName"));
		 */
		LIKE,
		/**
		 * 排除不需要的关键字模糊查询
		 * sql语句自动拼接：not like
		 * fieldValue："%33"/"33%"/"%33%"
		 * fieldNames："ref_name"该值必须是数据库中的字段名称，而不是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.NOTLIKE, "'%33'", "ref_name"));
		 */
		NOTLIKE, 
		/**
		 * 获取指定字段【相似】的数据，可自由组合（A%或%A或%A%或A或者_）
		 * sql语句自动拼接：like
		 * fieldValue："'%33'"/"'33%'"/"'%33%'"
		 * fieldNames："ref_name"该值必须是数据库中的字段名称，而不是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.LIKE_, "'%33'", "ref_name"));
		 */
		LIKE_,
		/**
		 * 获取指定字段【包含】的数据
		 * sql语句自动拼接：in ()
		 * fieldValue："33,44,55"
		 * fieldNames：是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.IN, "33,44,55", "refName"));
		 */
		IN,
		/**
		 * 获取指定字段【包含】的数据
		 * sql语句自动拼接：not in ()
		 * fieldValue："33,44,55"
		 * fieldNames：是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.NOTIN, "33,44,55", "refName"));
		 */
		NOTIN,
		/**
		 * 获取指定字段【小于】的数据
		 * sql语句自动拼接：<
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.LT, 33, "refName"));
		 */
		LT,
		/**
		 * 获取指定字段【小于且等于】的数据
		 * sql语句自动拼接：<=
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.LE, 33, "refName"));
		 */
		LE,
		/**
		 * 获取指定字段【大于】的数据
		 * sql语句自动拼接：>
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.GT, 33, "refName"));
		 */
		GT,
		/**
		 * 获取指定字段【大于且等于】的数据
		 * sql语句自动拼接：>=
		 * fieldValue："33"
		 * fieldNames："refName"该值是实体类中的属性名称
		 * eg:filtrations.add(new Filtration(MatchType.GE, 33, "refName"));
		 */
		GE,
		/**
		 * 获取指【SQL】的数据
		 * sql语句自动拼接：sql语句
		 * eg:filtrations.add(new Filtration(MatchType.SQL, "", "(ref_name is not null or ref_name = '33')"));
		 */
		SQL,
		/**
		 * and 这里只是作为一个判断（一般用于多个条件的逻辑与）eg：(A > D and C >= G)
		 */
		AND,
		/**
		 * or 这里只是作为一个判断（一般用于多个条件的逻辑或）eg：(A >= D or A <= C)
		 */
		OR;
	}
	
	/**
	 * 字段类型
	 */
	public enum FiledType{
		S(String.class), //字符串
		I(Integer.class), //整形
		L(Long.class), //长整形
		N(Double.class), //双精度
		D(Date.class), //时间
		B(Boolean.class);//布尔

		private Class<?> clazz;

		FiledType(Class<?> clazz){
			this.clazz = clazz;
		}
		public Class<?> getValue(){
			return clazz;
		}
	}
	
}