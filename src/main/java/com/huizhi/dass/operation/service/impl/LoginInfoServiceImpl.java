package com.huizhi.dass.operation.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huizhi.dass.common.Constants;
import com.huizhi.dass.common.util.JsonUtils;
import com.huizhi.dass.common.util.LogFileCallback;
import com.huizhi.dass.common.util.LogFileUtils;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.dao.LoginLogDAO;
import com.huizhi.dass.operation.service.LoginInfoService;

@Service
public class LoginInfoServiceImpl implements LoginInfoService {

	private static final Logger logger = LoggerFactory
			.getLogger("loginLog");

	private static final String LOG_PATH = Constants.LOG_PATH;
	private static final String LOGIN_LOG_FLAG = Constants.LOGIN_LOG_FLAG;

	@Autowired
	private LoginLogDAO loginLogDAO;

	LogFileUtils<LoginLog> logFileUtils = new LogFileUtils<LoginLog>(){};
	
	@Override
	public boolean addLoginLog(LoginLog log) {

		logger.info(JsonUtils.beanToJson(log));

		return true;
	}

	private boolean addLoginLogToDBFromLog() {
		String date = DateTime.now().toString("yyyy-MM-dd");
		
		List<String> paths = logFileUtils.traverseLogFile(LOG_PATH, date, LOGIN_LOG_FLAG);
		try {
			for (String path : paths) {
				System.out.println("== read file start: " + DateTime.now());
				
				logFileUtils.copyDataFromLogFile(logFileUtils, new File(path), new LogFileCallback<LoginLog, Boolean>() {
					
					@Override
					public Boolean callBack(List<LoginLog> list) {
						System.out.println("=== add db start: " + DateTime.now());
						loginLogDAO.insertList((List<LoginLog>) list);
						System.out.println("=== add db end: " + DateTime.now());
						return true;
					}
				});
				
				
//				readLogFileAndAddIntoDB(new File(path));
				
				System.out.println("== read file end: " + DateTime.now());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}


	/**
	 * 遍历获得日志文件路径列表
	 * 
	 * @param root
	 *            日志根路径
	 * @param date
	 *            日志的时间
	 * @param flag
	 *            日志前缀标记
	 * @param paths路径列表
	 */
	@Deprecated
	public List<String> traverseLogFile(String root, String date, String flag) {
		List<String> paths = new ArrayList<String>();
		File file = new File(root);
		// 子文件夹，文件名为日期
		File[] subFiles = file.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				if (subFile.getName().equals(date)) {
					File file2 = new File(subFile.getAbsolutePath());
					File[] sonFiles = file2.listFiles();
					// 日志文件，文件名为
					for (File sonFile : sonFiles) {
						if (sonFile.getName().startsWith(flag)) {
							paths.add(sonFile.getAbsolutePath());
						}
					}
				}
			}
		}
		return paths;
	}

	/**
	 * 读取文件并分批插入数据库
	 * @param logFile
	 * @throws IOException
	 */
	@Deprecated
	private void readLogFileAndAddIntoDB(File logFile) throws IOException {
		BufferedReader br = null;
		try {
			List<LoginLog> logs = new ArrayList<LoginLog>();
			br = new BufferedReader(new FileReader(logFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				LoginLog log = logFileUtils.checkLineAndToBean(line, LoginLog.class);
				if (log != null) {
					logs.add(log);
				}
				//10000条日志进行一次插入DB批处理
				if (logs.size() == 10000) {
					System.out.println("=== add db start: " + DateTime.now());
					loginLogDAO.insertList(logs);
					System.out.println("=== add db end: " + DateTime.now());
					logs.clear();
				}
			}
			//把最后剩余的日志写入数据库
			if (logs.size() != 0) {
				System.out.println("=== add db start: " + DateTime.now());
				loginLogDAO.insertList(logs);
				System.out.println("=== add db end: " + DateTime.now());
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
	

}
