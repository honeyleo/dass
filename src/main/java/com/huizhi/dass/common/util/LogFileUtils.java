package com.huizhi.dass.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class LogFileUtils<T> {
	
	/**
	 * 验证每行日志并转换为bean
	 * @param line
	 * @return
	 */
	public T checkLineAndToBean(String line, Class<T> clazz) {
		if (StringUtils.isBlank(line)) {
			return null;
		}
		if (!(line.startsWith("{") && line.endsWith("}"))) {
			return null;
		}
		T obj = JsonUtils.jsonToBean(line, clazz);
		
		return obj;
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
	
	public static void main(String[] args) {
		LogFileUtils<String> lfu = new LogFileUtils<String>(){};
		System.out.println(lfu.getMyClass());
	}
	
	/**
	 * 获得泛型的class类型
	 * @return
	 */
	protected Class<T> getMyClass() {   
		Class<T> myClass = null;
	    if (myClass == null) {   
	      myClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];   
	    }   
	    return myClass;   
	  }
	
	/**
	 * 复制文件中的数据至（数据库 || 缓存）
	 * @param logFile
	 * @throws IOException
	 */
	public <R> void copyDataFromLogFile(LogFileUtils<T> myChild, File logFile, LogFileCallback<T, R> callback) throws IOException {
		BufferedReader br = null;
		Class<T> clazz = myChild.getMyClass();
		try {
			List<T> logs = new ArrayList<T>();
			br = new BufferedReader(new FileReader(logFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				T log = checkLineAndToBean(line, clazz);
				if (log != null) {
					logs.add(log);
				}
				//10000条日志进行一次批处理
				if (logs.size() == 10000) {
					R result = callback.callBack(logs);
//					loginLogDAO.insertList(logs);
					logs.clear();
				}
			}
			//把最后剩余的日志进行操作
			if (logs.size() != 0) {
				R result = callback.callBack(logs);
//				loginLogDAO.insertList(logs);
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
	
}
