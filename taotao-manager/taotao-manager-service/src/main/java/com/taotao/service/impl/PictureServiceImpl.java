package com.taotao.service.impl;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;

@Service
public class PictureServiceImpl implements PictureService {
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	@Value("${FILI_UPLOAD_PATH}")
	private String FILI_UPLOAD_PATH;
	@Value("${FTP_SERVER_IP}")
	private String FTP_SERVER_IP;
	@Value("${FTP_SERVER_PORT}")
	private Integer FTP_SERVER_PORT;
	@Value("${FTP_SERVER_USERNAME}")
	private String FTP_SERVER_USERNAME;
	@Value("${FTP_SERVER_PASSWORD}")
	private String FTP_SERVER_PASSWORD;

	@Override
	public PictureResult upLoadFile(MultipartFile uploadFile) {
		PictureResult pictureResult = new PictureResult();
		boolean result = false;
		String oldName="", newName="", filePath="";
		
		try {
			if (!uploadFile.isEmpty()) {
				oldName = uploadFile.getOriginalFilename();
				newName = IDUtils.genImageName() + oldName.substring(oldName.lastIndexOf("."));
				filePath = new DateTime().toString("/yyyy/MM/dd/");
				result = FtpUtil.uploadFile(FTP_SERVER_IP, FTP_SERVER_PORT, FTP_SERVER_USERNAME, FTP_SERVER_PASSWORD,
						FILI_UPLOAD_PATH, filePath, newName, uploadFile.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result) {
			pictureResult.setError(0);
			pictureResult.setUrl(IMAGE_BASE_URL + filePath+newName);
		}else{
			pictureResult.setError(1);
			pictureResult.setMessage("文件上传失败");
		}

		return pictureResult;
	}
}
