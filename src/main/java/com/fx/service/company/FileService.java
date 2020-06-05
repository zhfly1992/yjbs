package com.fx.service.company;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.hiberantedao.service.BaseService;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.entity.company.FileManage;


public interface FileService extends BaseService<FileManage, Long> {
	/**
	 * 
	 * @Description:后台上传图片
	 * @param file
	 * @param request
	 * @param userName
	 * @return
	 * @author :zh
	 * @version 2020年4月23日
	 */
	public JSONObject companyUploadPic( MultipartFile file,HttpServletRequest request,String uName,ReqSrc reqsrc);
	
	
	/**
	 * 自定义文件真是名称
	 * @author xx
	 * @date 20200520
	 * @return 格式：yjbs_20150723100825_1
	 */
	public String diyFileRelName();
	/**
	 * 保存文件
	 * @author xx
	 * @date 20160412
	 * @param path 图片路径
	 * @param fRelName 文件真实名
	 * @param filePath 文件名
	 * @param loginName 操作用户
	 * @param fType 上传方式
	 * @param remark 图片备注
	 */
	public String saveFile(String path,String fRelName,String [] filePath,String loginName,int fType,String remark);
	/**
	 * 更新文件
	 * @author xx
	 * @date 20160412
	 * @param path 图片路径
	 * @param fRelName 文件真实名
	 * @param filePath 文件名
	 * @param fileId 修改ID
	 * @param remark 图片备注
	 */
	public String modifyFile(String path,String fRelName,String [] filePath,Long fileId,String remark);
	/**
	 * 删除文件
	 * @author xx
	 * @date 20160509
	 */
	public void delFile(String path,Long fileId);
	
	/**
	 * 上传文件
	 * @param reqsrc
	 * @param multipartRequest 文件request
	 * @param response response
	 * @param remark 备注
	 * @return 结果字符串
	 * @return map{code: 结果状态码, msg: 结果状态说明, data: 数据}
	 */
	public Map<String, Object> upFiles(ReqSrc reqsrc,MultipartHttpServletRequest multipartRequest, HttpServletResponse response, 
		String remark);
	
	/**
	 * 添加-文件数据
	 * @param freq 			文件request
	 * @param dir 			文件存放目录
	 * @param uploadUname 	上传文件用户名
	 * @param ftype 		文件类型
	 * @param fnote 		文件备注
	 * @param filedName 	属性名称
	 * @return map{code: 结果状态码, msg: 状态码说明, data: 数据}
	 */
	public Map<String, Object> addFileMan(MultipartHttpServletRequest freq, String dir, String uploadUname, 
		String ftype, String fnote, String filedName);
	
	/**
	 * 添加-指定图片列表对象
	 * @param multRequest 文件上传request
	 * @param dirPath 图片所属根文件夹
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息], flist:[添加成功后的文件列表]}
	 */
	public Map<String, Object> addFileList(MultipartHttpServletRequest multRequest, 
		String dirPath);
	
	/**
	 * 删除-指定图片列表对象
	 * @param ids 		文件对象id数组
	 * @param dirPath 	图片所属根文件夹
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delFileList(Object[] ids, String dirPath);
	
	/**
	 * 删除-指定文件列表对象
	 * @param fmlist 	文件列表
	 * @param dirPath 	图片所属根文件夹
	 * @return map{code[1-成功；0-失败；-1-异常；], msg[提示信息]}
	 */
	public Map<String, Object> delFileList(List<FileManage> fmlist, String dirPath);
}
