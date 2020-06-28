package com.fx.dao.back;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.utils.enums.FileType;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.tools.FV;
import com.fx.commons.utils.tools.U;
import com.fx.entity.back.FileMan;

@Repository
public class FileManDao extends ZBaseDaoImpl<FileMan, Long> {

	/** 日志记录 */
	private Logger log = LogManager.getLogger(this.getClass().getName());
	
	/**
	 * 获取-指定文件
	 * @param id 文件id
	 */
	public FileMan findFileMan(String id) {
		String logtxt = U.log(log, "获取-指定文件");
		
		boolean fg = true;
		FileMan fm = null;
		
		try {
			if(fg) {
				if(StringUtils.isBlank(id)) {
					fg = U.logFalse(log, "[文件id]为空");
				}else {
					id = id.trim();
					if(!FV.isLong(id)) {
						fg = U.logFalse(log, "[文件id]格式错误");
					}else {
						fm = findByField("id", Long.parseLong(id));
						if(fm == null) {
							U.log(log, "[文件]不存在");
						}
					}
					
					U.log(log, "[文件id] id="+id);
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt);
			e.printStackTrace();
		}
		
		return fm;
	}
	
	/**
	 * 获取-指定文件列表
	 * @param id 文件id数组字符串
	 */
	public List<FileMan> findFileManList(String ids) {
		String logtxt = U.log(log, "获取-指定文件列表");
		
		boolean fg = true;
		String hql = "";
		List<FileMan> fms = new ArrayList<FileMan>();
		
		try {
			List<Object> idarr = new ArrayList<Object>();
			if(fg) {
				if(StringUtils.isBlank(ids)) {
					fg = U.logFalse(log, "[文件id数组字符串]为空");
				}else {
					ids = ids.trim();
					String[] arrs = ids.split(",");
					for (String str : arrs) {
						idarr.add(Long.parseLong(str));
					}
					
					U.log(log, "[文件id数组字符串] ids="+ids);
				}
			}
			
			if(fg) {
				if(idarr.size() > 0) {
					hql = "from FileMan where id in(:v0) order by id asc";
					fms = findListIns(hql, idarr.toArray());
					
					U.log(log, "获取"+fms.size()+"个文件");
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt);
			e.printStackTrace();
		}
		
		return fms;
	}

	/**
	 * 删除-记账报销记录及文件
	 * @param unitNum 	单位编号
	 * @param uname 	用户名
	 * @param id 		标志数据
	 */
	public void delJzbxFile(String unitNum, String uname, FileType ftype, String id) {
		String logtxt = U.log(log, "删除-指定文件数据及文件");
		
		boolean fg = true;
		String hql = "";
		
		try {
			if(fg) {
				U u = new U();
				String pf = u.getClassPath();// 获取盘符
				
				hql = "from FileMan where ftype = ?0 and fdat = ?1";
				List<FileMan> fms = findhqlList(hql, ftype, unitNum+"="+uname+"="+id);
				for (FileMan fm : fms) {
					// 先删除图片
					File file = new File(pf+":"+fm.getFolderName().replace("/jzbx", UtilFile.JZBX_FILE_PATH)+"/"+fm.getFname());
					if(file.exists()) {
						file.delete();
						U.log(log, "已删除"+fm.getFname()+"文件");
					}
					// 再删除数据
					delete(fm);
					U.log(log, "删除数据完成");
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt);
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除-记账报销记录及文件
	 * @param fids 		文件数据id数组
	 */
	public void delJzbxFiles(Object[] fids) {
		String logtxt = U.log(log, "删除-指定文件数据及文件");
		
		boolean fg = true;
		String hql = "";
		
		try {
			if(fg) {
				U u = new U();
				String pf = u.getClassPath();// 获取盘符
				
				hql = "from FileMan where id in(:v0)";
				List<FileMan> fms = findListIns(hql, fids);
				for (FileMan fm : fms) {
					// 先删除图片
					File file = new File(pf+":"+fm.getFolderName().replace("/jzbx", UtilFile.JZBX_FILE_PATH)+"/"+fm.getFname());
					if(file.exists()) {
						file.delete();
						U.log(log, "已删除"+fm.getFname()+"文件");
					}
					// 再删除数据
					delete(fm);
					U.log(log, "删除数据完成");
				}
			}
		} catch (Exception e) {
			U.log(log, logtxt);
			e.printStackTrace();
		}
		
	}
	
}
