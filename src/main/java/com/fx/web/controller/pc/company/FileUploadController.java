package com.fx.web.controller.pc.company;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.fx.commons.utils.enums.ReqSrc;
import com.fx.service.company.FileService;
import com.fx.web.controller.BaseController;

import io.swagger.annotations.Api;

@Api(tags="电脑端-单位管理-文件上传模块")
@Controller
@RequestMapping("/company")
public class FileUploadController extends BaseController {
	@Autowired
	private FileService fileService;
	@RequestMapping(value = "/upload")
	@ResponseBody
	public JSONObject upload(@RequestParam("file") MultipartFile file,HttpServletRequest request,@RequestParam("userName") String userName){
		return fileService.companyUploadPic(file, request, userName, ReqSrc.PC_COMPANY);
	}
}
