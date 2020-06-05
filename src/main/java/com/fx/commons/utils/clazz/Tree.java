package com.fx.commons.utils.clazz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形结构类
 */
public class Tree<T> implements Serializable {
	private static final long serialVersionUID = -3320510772964029728L;
	
	/** 节点ID */
    private Long id;
    /** 父节点ID */
    private Long parentId;
    /** 是否有子节点 */
    private Boolean hasChildren;
    /** 是否有父节点 */
    private Boolean hasParent;
    /** 节点名称 */
    private String name;
    /** 节点URL */
    private String url;
    /** 图标 */
    private String icon;
    /** 子节点信息 */
    private List<Tree<T>> children = new ArrayList<>();
    
    
	/**  
	 * 获取 节点ID  
	 * @return id
	 */
	public Long getId() {
		return id;
	}
	
	/**  
	 * 设置 节点ID  
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**  
	 * 获取 父节点ID  
	 * @return parentId
	 */
	public Long getParentId() {
		return parentId;
	}
	
	/**  
	 * 设置 父节点ID  
	 * @param parentId
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	/**  
	 * 获取 是否有子节点  
	 * @return hasChildren
	 */
	public Boolean getHasChildren() {
		return hasChildren;
	}
	
	/**  
	 * 设置 是否有子节点  
	 * @param hasChildren
	 */
	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	
	/**  
	 * 获取 是否有父节点  
	 * @return hasParent
	 */
	public Boolean getHasParent() {
		return hasParent;
	}
	
	/**  
	 * 设置 是否有父节点  
	 * @param hasParent
	 */
	public void setHasParent(Boolean hasParent) {
		this.hasParent = hasParent;
	}
	
	/**  
	 * 获取 节点名称  
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**  
	 * 设置 节点名称  
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**  
	 * 获取 节点URL  
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
	
	/**  
	 * 设置 节点URL  
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**  
	 * 获取 图标  
	 * @return icon
	 */
	public String getIcon() {
		return icon;
	}
	
	/**  
	 * 设置 图标  
	 * @param icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**  
	 * 获取 子节点信息  
	 * @return children
	 */
	public List<Tree<T>> getChildren() {
		return children;
	}
	
	/**  
	 * 设置 子节点信息  
	 * @param children
	 */
	public void setChildren(List<Tree<T>> children) {
		this.children = children;
	}
	
	/**  
	 * 获取 serialVersionUID  
	 * @return serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

//	/* (non-Javadoc)
//	 * @see java.lang.Object#toString()
//	 */
//	@Override
//	public String toString() {
//		return "Tree [id=" + id + ", parentId=" + parentId + ", hasChildren=" + hasChildren + ", hasParent=" + hasParent
//				+ ", name=" + name + ", url=" + url + ", icon=" + icon + ", children=" + children + "]";
//	}
	
}
