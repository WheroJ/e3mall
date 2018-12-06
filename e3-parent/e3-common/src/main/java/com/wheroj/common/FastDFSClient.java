package com.wheroj.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class FastDFSClient {
	private StorageClient storageClient;
	
	public FastDFSClient(String conf) {
		if (conf.contains("classpath:")) {
			conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
		}
		try {
			ClientGlobal.init(conf);
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			storageClient = new StorageClient(trackerServer, null);
		} catch (IOException | MyException e) {
			e.printStackTrace();
		}
	}
	
	public String[] uploadFile(String local_filename) throws IOException, MyException {
		String file_ext_name = FileUtils.getExtName(local_filename);
		return storageClient.upload_file(local_filename, file_ext_name, null);
	}
	
	public String[] uploadFile(byte[] content, String extName) throws IOException, MyException {
		return storageClient.upload_file(content, extName, null);
	}
	
	public String downloadFile(String group, String remoteFileName, String savePath) throws IOException, MyException {
		if (remoteFileName == null || group == null) {
			return null;
		}
		
		byte[] bytes = storageClient.download_file(group, remoteFileName);
		
		String extName = FileUtils.getExtName(remoteFileName);
		String fileName = UUID.randomUUID().toString();
		if (extName != null) {
			fileName += "." + extName;
		}
		writeFileToLocal(bytes, savePath, fileName);
		return savePath + File.separator + fileName;
	}
	
    private static void writeFileToLocal(byte[] bytes, String downloadPath, String fileName) throws IOException {
    	File file = new File(downloadPath, fileName);
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(bytes);
		outputStream.close();
    }
}
