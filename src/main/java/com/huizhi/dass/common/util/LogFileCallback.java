package com.huizhi.dass.common.util;

import java.util.List;

/**
 * 日志文件工具类 回调函数接口
 * 
 * @author Administrator
 *
 * @param <T> 集合泛型类型
 * @param <R> 返回类型
 */
public interface LogFileCallback<T, R> {
	
	public R callBack(List<T> list);

}
