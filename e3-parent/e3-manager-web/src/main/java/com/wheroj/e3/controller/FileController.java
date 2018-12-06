package com.wheroj.e3.controller;

import java.io.IOException;
import java.util.HashMap;

import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wheroj.common.FastDFSClient;
import com.wheroj.common.FileUtils;
import com.wheroj.common.JsonUtils;

@Controller
public class FileController {
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@Value("${file_download_path}")
	private String file_download_path;
	
	@Value("${fastdfs_config_path}")
	private String fastdfs_config_path;
	
	//content-type是text/plan格式的json字符串。兼容性是最好的
	@PostMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) throws IOException, MyException {
		System.out.println("-------" + IMAGE_SERVER_URL);
		System.out.println("-------" + file_download_path);
		System.out.println("-------" + fastdfs_config_path);
		FastDFSClient fastDFSClient = new FastDFSClient(fastdfs_config_path);
		String extName = FileUtils.getExtName(uploadFile.getOriginalFilename());
		String[] results = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		if (results.length == 2) {
			hashMap.put("error", 0);
			hashMap.put("url", IMAGE_SERVER_URL + results[0] + "/" + results[1]);
		} else {
			hashMap.put("error", 1);
			hashMap.put("message", "上传失败");
		}
		return JsonUtils.objectToJson(hashMap);
	}
}
