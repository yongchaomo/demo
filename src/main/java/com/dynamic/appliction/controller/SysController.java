package com.dynamic.appliction.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dynamic.appliction.pojo.bean.MailBox;
import com.dynamic.appliction.pojo.bean.Resp;
import com.dynamic.appliction.service.UserService;
import com.dynamic.appliction.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SysController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	/**
	 * @Description: 分页查询用户
	 * @Param:
	 * @return: Json（Boolean）
	 * @throws Exception
	 * @Author: Mr.Mo
	 * @Date: 2018/6/22
	 */
	@ResponseBody
	@GetMapping(value = "queryUserList")
	public Map<String, Object> queryUserList(int pageSize, int pageNumber, String country, String usdRange)
			throws Exception {
		return userService.queryUserList(pageSize, pageNumber, country, usdRange);
	}

	/**
	 * @Description: 给用户群发信息
	 * @Param:
	 * @return: Json（Boolean）
	 * @throws Exception
	 * @Author: Mr.Mo
	 * @Date: 2018/6/22
	 */
	@ResponseBody
	@RequestMapping(value = "groupMail", method = RequestMethod.POST)
	public Resp groupMail(@RequestParam(value = "mails[]", required = false) List<String> mails,
			@RequestParam("mailBox") String mailBox) throws Exception {
		MailBox mailBoxs = JSONArray.toJavaObject((JSONObject) JSONArray.parse(mailBox), MailBox.class);
		userService.groupMail(mails, mailBoxs);
		return null;
	}

	/**
	 * @Description: 图片上传
	 * @Param: User user
	 * @return: Json（Boolean）
	 * @Author: Mr.Mo
	 * @Date: 2018/6/22
	 */
	@ResponseBody
	@RequestMapping(value = "uploadMultipleFile", method = RequestMethod.POST, produces = "application/json;charset=utf8")
	public String uploadMultipleFile(@RequestParam("file") MultipartFile[] files, HttpServletRequest request) {
		String uploadPath = request.getServletContext().getRealPath("/").replaceAll("\\\\", "/") + "upload/";
		return SysController.uploadImage(uploadPath, files);

	}

	// UploadUtil.java中uploadImage方法如下
	public static String uploadImage(String uploadPath, MultipartFile[] multipartFile) {
		List<String> images = new ArrayList<>();
		try {
			for (int i = 0; i < multipartFile.length; i++) {
				byte[] file = multipartFile[i].getBytes();
				String fileName = multipartFile[i].getOriginalFilename();
				File targetFile = new File(uploadPath);
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				String path = uploadPath + fileName;
				FileOutputStream out = new FileOutputStream(path);
				out.write(file);
				out.flush();
				out.close();
				images.add("http://139.199.90.26/upload/" + fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JsonUtil.list2json(images);
	}
}
