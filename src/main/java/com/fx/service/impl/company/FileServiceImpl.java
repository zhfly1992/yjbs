package com.fx.service.impl.company;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.dao.ZBaseDaoImpl;
import com.fx.commons.hiberantedao.service.BaseServiceImpl;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.commons.utils.other.UtilFile;
import com.fx.commons.utils.tools.LU;
import com.fx.commons.utils.tools.U;
import com.fx.dao.company.FileDao;
import com.fx.entity.company.FileManage;
import com.fx.service.company.FileService;
import com.fx.web.util.RedisUtil;

@Service
@Transactional
public class FileServiceImpl extends BaseServiceImpl<FileManage, Long> implements FileService {
	/** 日志记录 */
	private Logger	log	= LogManager.getLogger(this.getClass());
	/**缓存服务*/
	@Autowired
    private RedisUtil redis;
	@Autowired
	private FileDao	fileDao;



	@Override
	public ZBaseDaoImpl<FileManage, Long> getDao() {
		return fileDao;
	}

	private final static String	FIAL	= "后台-上传图片失败";

	private final static String	SUCCESS	= "后台-上传图片成功";

	
	private String				uploadFolder = "C://upload/pic";



	@Override
	public JSONObject companyUploadPic(MultipartFile file, HttpServletRequest request, String uName, ReqSrc reqsrc) {
		// TODO Auto-generated method stub
		U.log(log, "单位管理-上传图片", reqsrc);
		JSONObject result = new JSONObject();
		boolean fg = true;
		try {
			if (file.isEmpty()) {
				U.log(log, FIAL + "图片为空");
				result.put("code", 0);
				result.put("msg", FIAL + "图片为空");
				fg = false;
			}

			if (fg) {
				// 获取文件名
				String origFileName = file.getOriginalFilename();

				// 获取文件后缀名
				String suffixName = origFileName.substring(origFileName.lastIndexOf("."));
				// 重新生成文件名
				String fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;

//				String path = request.getContextPath();
				
//				String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
//						+ path;
				
//				String basePath = request.getScheme() + "://" + request.getLocalAddr() + path;
				
				String basePath = request.getScheme() + "://" + "121.37.161.108" ;
				
				File dest = new File(uploadFolder + '/' + fileName);
				
				file.transferTo(dest);

				U.log(log, SUCCESS);
				/**nginx做映射处理**/
				String url =  basePath + "/file/pic/" + fileName;
				
				FileManage fileManage = new FileManage();
				fileManage.setfCName(uName);
				fileManage.setfRelName(origFileName);
				fileManage.setfName(fileName);
				fileManage.setfUrl(url);
				fileManage.setfType(1);
				fileManage.setStatus(1);
				fileManage.setfUpTime(new Date());
				fileDao.save(fileManage);
				
				result.put("code", 1);
				result.put("msg", SUCCESS);
				result.put("url", url);
				U.log(log, SUCCESS + "记录保存成功");
			}
		} catch (Exception e) {
			U.logEx(log, e.toString());
			e.printStackTrace();
			result.put("code", 0);
			result.put("msg", FIAL);
		}
		return result;
	}

	@Override
	public String diyFileRelName() {
		String fRelName = "yjbs_"+System.currentTimeMillis()+"_";
		//查询数据库，检查上一条的文件真实名的最后编码
		Object obj = fileDao.findObj("select max(id) from FileManage");
		if(obj != null && Integer.parseInt(obj.toString()) > 0){
			fRelName += Integer.parseInt(obj.toString()) + 1;
		}else{
			fRelName += "1";
		}
		return fRelName;
	}
	@Override
	public String saveFile(String path,String fRelName,String [] filePath,String loginName,int fType,String remark) {
		FileManage fm = new FileManage();
    	//////////////进行保存数据///////////
		fm.setfCName(loginName);
		fm.setfName(filePath[0]+filePath[1]);//设置文件名
		fm.setfRelName(fRelName+filePath[1]);//设置文件真实名
		if(StringUtils.isNotEmpty(remark)){
			fm.setfRemark(remark);	
		}else{
			fm.setfRemark(loginName);
		}
		fm.setfType(fType);
		fm.setfUpTime(new Date());
		fm.setfUrl(path.substring(path.lastIndexOf("/"),path.length()));
		fileDao.save(fm);
		return fm.getId()+","+fm.getfUrl()+"/"+fm.getfRelName();
	}
	@Override
	public String modifyFile(String path,String fRelName, String[] filePath, Long fileId,String remark) {
		FileManage fm = fileDao.find(fileId);
		File file = new File(path+"/"+fm.getfRelName());
		//删除服务器端文件
		if(file.exists()){
			file.delete();
		}
		fm.setfName(filePath[0]+filePath[1]);//设置文件名
		fm.setfRelName(fRelName+filePath[1]);//设置文件真实名
		if(StringUtils.isNotEmpty(remark)){
			fm.setfRemark(remark);	
		}
		fm.setfUpTime(new Date());
		fileDao.update(fm);
		return fm.getId()+","+fm.getfUrl()+"/"+fm.getfRelName();
	}
	@Override
	public void delFile(String path, Long fileId) {
		FileManage fm = fileDao.findByField("id",fileId);
		if(fm!=null){
			fileDao.delete(fm);
			File file = new File(path+"/"+fm.getfRelName());
			//删除服务器端文件
			if(file.exists()){
				file.delete();
			}
		}
	}
	
	@Override
	public Map<String, Object> upFiles(ReqSrc reqsrc,MultipartHttpServletRequest multipartRequest, HttpServletResponse response, 
		String remark) {
		String logtxt = U.log(log, "单位管理-上传文件", reqsrc);
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if (multipartRequest.getFileMap().size()==0) {
				fg = U.setPutFalse(map, "[上传文件]不能为空");
			}
			if(fg){
				String path = UtilFile.COMPANYFILE;
				UtilFile.creatFolder(path);// 判断文件夹是否存在，不存在创建
				String[] filePath = null;// 文件名称数组
				MultipartFile imgFile = null;// 图片属性
				String key = null; // 文件控件name
				String fileName = null; // 上传文件真实名
				String fileT = null; // 文件后缀名
				String fRelName = null; // 系统自定义名称
				String loginName = LU.getLUName(multipartRequest, redis);
				String voucherInfo = null;// [凭证照ID,凭证照URL]
				List<String> urlist=new ArrayList<>();
				// 获取多个file
				for (Iterator<String> it = multipartRequest.getFileNames(); it.hasNext();) {
					key = (String) it.next();
					imgFile = multipartRequest.getFile(key);
					if (imgFile.getOriginalFilename().length() > 0) {
						fileName = UUID.randomUUID().toString() + ".png";
						fileT = fileName.substring(fileName.lastIndexOf("."));
						fRelName = key.split(",")[0] + "_" + diyFileRelName();
						fileName = fileName.substring(0, fileName.lastIndexOf(".")); // 截取后缀名前面的字符串
						filePath = new String[] { fileName, fileT };
						if (key.contains(",")) {// 修改
							voucherInfo = modifyFile(path, fRelName, filePath, Long.valueOf(key.split(",")[1]), remark);
						} else {
							voucherInfo = saveFile(path, fRelName, filePath, loginName, 2, remark);
						}
						urlist.add(voucherInfo.split(",")[1]);
						// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
						FileUtils.copyInputStreamToFile(imgFile.getInputStream(), new File(path, fRelName + fileT));
					}
				}
				map.put("urls", urlist.toArray());
				U.setPut(map, 1, "添加文件数据完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> addFileList(MultipartHttpServletRequest multRequest, 
		String dirPath) {
		U.log(log, "添加-文件数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
				String folderName = U.getYearMonthFolder(new Date());
				String uploadFileUrl = UtilFile.AD_IMG_FILE_URL + folderName;
			    U.creatFolder(uploadFileUrl); // 判断文件夹是否存在，不存在创建
				MultipartFile imgfile = null; // 图片属性
				String key = ""; // 文件控件name
				String ftype = ".png"; // 文件后缀名
				String fname = "";  // 上传文件真实名
				
				List<FileManage> flist = new ArrayList<FileManage>();
				// 获取多个file  
			    for (Iterator<String> it = multRequest.getFileNames(); it.hasNext();) {
			    	key = it.next().toString();
			    	if(key.contains("file")){ // 其他图片
	            		continue;
	            	}
			    	
			        imgfile = multRequest.getFile(key);  
			        if (imgfile.getOriginalFilename().length() > 0) {
		            	fname = U.getUUID() + ftype;// 为文件新命名，防止重复  
		            	
		            	FileManage fm = new FileManage();
//		            	fm.set
//		            	fm.setFolderName(folderName+"/");
//		            	fm.setRealName(fname);
//		            	
//		            	if(key.contains("con_img")){ // 内容图片
//		            		flist.add(fm);
//		            		fm.setfType(FileType.AD_DETAIL_CON_IMG);
//		            	}
		            	fileDao.save(fm);// 保存图片信息
		            	
		            	// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
	                    FileUtils.copyInputStreamToFile(imgfile.getInputStream(), new File(uploadFileUrl, fname));
			        }  
			    }
			    
			    map.put("flist", flist);
				
				U.setPut(map, 1, "添加文件数据完成");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, "添加-文件列表异常");
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> delFileList(Object[] ids, String dirPath) {
		U.log(log, "删除-文件数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "";
		boolean fg = true;
		
		try {
			List<FileManage> fms = new ArrayList<FileManage>();
			if(fg) {
				hql = "from FileManage where id in(:v0)";
				fms = fileDao.findListIns(hql, ids);
			}
			
			if(fg){
				if(fms.size() == 0){
					fg = U.setPutFalse(map, "文件列表数据长度为0");
				}else{
					U.log(log, "正在删除"+fms.size()+"条文件数据");
				}
			}
			
			if(fg){
				String f_url = "";
				int count = 0;// 实际删除文件数量
				for(int i = 0; i < fms.size(); i++){
					FileManage fm = fms.get(i);
					File file = new File(dirPath + "/"+ fm.getfRelName());
					f_url = file.getPath();
					// 删除服务器端文件
					if(file.exists()){
						file.delete();
						count ++;
						U.log(log, "删除文件成功-["+f_url+"]");
					}else{
						U.log(log, "文件不存在-["+f_url+"]");
					}
					
					// 删除文件数据
					fileDao.delete(fms.get(i));
					
					U.log(log, "删除-文件数据成功");
				}
				
				U.setPut(map, 1, "删除文件数据完成");
				
				U.log(log, "删除文件数据完成，文件共："+fms.size()+"个，实际删除："+count+"张");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, "删除-文件列表异常");
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> delFileList(List<FileManage> fmlist, String dirPath) {
		String logtxt = U.log(log, "删除-指定文件列表对象");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
				if(fmlist.size() == 0){
					fg = U.setPutFalse(map, "文件列表数据长度为0");
				}else{
					U.log(log, "正在删除"+fmlist.size()+"条文件数据");
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(dirPath)) {
					U.log(log, "[文件根目录]为空");
					dirPath = "";
				}else {
					dirPath = dirPath.trim();
					
					U.log(log, "[文件根目录] dirPath="+dirPath);
				}
			}
			
			if(fg){
				int count = 0;// 实际删除文件数量
				
				for(int i = 0; i < fmlist.size(); i++){
					FileManage fm = fmlist.get(i);
					
					File file = new File(dirPath + fm.getfUrl() + "/" + fm.getfRelName());
					// 删除服务器端文件
					if(file.exists()){
						file.delete();
						count ++;
						U.log(log, "删除文件成功-["+file.getPath()+"]");
					}else{
						U.log(log, "文件不存在-["+file.getPath()+"]");
					}
					
					// 删除文件数据
					fileDao.delete(fmlist.get(i));
					
					U.log(log, "删除-文件数据成功");
				}
				
				U.setPut(map, 1, "删除文件数据完成");
				
				U.log(log, "删除文件数据完成，文件共："+fmlist.size()+"个，实际删除："+count+"张");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	public Map<String, Object> addFileMan(MultipartHttpServletRequest freq, String dir, String uploadUname, 
		String ftype, String fnote, String filedName) {
		String logtxt = U.log(log, "添加-文件数据");
		
		Map<String, Object> map = new HashMap<String, Object>();
		boolean fg = true;
		
		try {
			if(fg){
				if(!freq.getFileNames().hasNext()){
					fg = U.setPutFalse(map, "[文件]不能为空");
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(dir)) {
					fg = U.setPutFalse(map, "[保存文件根目录]不能为空");
				}else {
					dir = dir.trim();
					
					// 判断文件夹是否存在，不存在创建
				    U.creatFolder(dir);
					
					U.log(log, "[保存文件根目录] dir="+dir);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(uploadUname)) {
					fg = U.setPutFalse(map, "[上传文件用户名]不能为空");
				}else {
					uploadUname = uploadUname.trim();
					
					U.log(log, "[上传文件用户名] uploadUname="+uploadUname);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(ftype)) {
					fg = U.setPutFalse(map, "[文件所属类型]不能为空");
				}else {
					ftype = ftype.trim();
					
					U.log(log, "[文件所属类型] ftype="+ftype);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(fnote)) {
					fg = U.setPutFalse(map, "[文件备注]不能为空");
				}else {
					fnote = fnote.trim();
					if(fnote.length() > 100) {
						fg = U.setPutFalse(map, "[文件备注]最多100个字");
					}
					
					U.log(log, "[文件备注] fnote="+fnote);
				}
			}
			
			if(fg) {
				if(StringUtils.isEmpty(filedName)) {
					fg = U.setPutFalse(map, "[文件所属属性名称]不能为空");
				}else {
					filedName = filedName.trim();
					
					U.log(log, "[文件所属属性名称] filedName="+filedName);
				}
			}
			
			if(fg) {
				List<FileManage> fmlist = new ArrayList<FileManage>();
				// 获取多个file  
			    for (Iterator<String> it = freq.getFileNames(); it.hasNext();) {
			    	MultipartFile mf = freq.getFile(it.next().toString());// 获取文件
			        
			        if(mf.getOriginalFilename().length() == 0) {
			        	U.log(log, "文件["+mf.getName()+"]不存在");
			        }else {
		            	FileManage fm = new FileManage();
		            	fm.setfCName(uploadUname);
						fm.setfType(Integer.parseInt(ftype));
						fm.setStatus(0);
						fm.setfUrl(dir);
						// 文件原始名称
						fm.setfName(mf.getName()+"_"+mf.getOriginalFilename());
						fm.setfRemark(fnote);
						fm.setFiledName(filedName);
						fm.setfUpTime(new Date());
						// 文件上传完成后才保存数据
	                    fileDao.save(fm);
	                    U.log(log, "保存文件["+fm.getfName()+"]完成");
		            	
	                    
	                    /*------此处重新设置新的文件名称，目的是保存文件对象id，方便前端页面获取此id-------*/
	                    // 文件真实名称
						fm.setfRelName(System.currentTimeMillis()+"_"+fm.getId()+".png");
						
		            	// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的  
	                    FileUtils.copyInputStreamToFile(mf.getInputStream(), new File(fm.getfUrl(), fm.getfRelName()));
	                    
	                    // 文件上传完成后才保存数据
	                    fileDao.update(fm);
		            	fmlist.add(fm);
		            	
		            	U.log(log, "修改文件真实名称["+fm.getfRelName()+"]完成");
			        }
			    }
			    
			    map.put("fmlist", fmlist);
			    
			    U.setPut(map, 1, "添加文件数据成功");
			}
		} catch (Exception e) {
			U.setPutEx(map, log, e, logtxt);
			e.printStackTrace();
		}
		
		return map;
	}
	
}
