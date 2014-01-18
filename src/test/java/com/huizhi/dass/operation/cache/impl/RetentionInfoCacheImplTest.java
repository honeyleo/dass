package com.huizhi.dass.operation.cache.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huizhi.dass.model.Criteria;
import com.huizhi.dass.model.operation.LoginLog;
import com.huizhi.dass.operation.cache.RetentionInfoCache;
import com.huizhi.dass.operation.dao.LoginLogDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/business-config.xml")
public class RetentionInfoCacheImplTest {

	@Autowired
	private RetentionInfoCache riCache;
	
	@Autowired
	private LoginLogDAO llDAO;
	
//	@Test
	public void testInsertLoginLogList() {
		int size = 0;
		int offset = 100000;
		for (int i = 0;; i += offset) {
			Criteria criteria = new Criteria();
			criteria.setOffset(i);
			criteria.setRows(offset);
			List<LoginLog> list = llDAO.selectByExample(criteria);
			System.out.println(list.size());
			size += list.size();
			if (list.size() == 0) {
				break;
			}
			
			System.out.println(DateTime.now().toString());
//			riCache.insertLoginLogList(list);
			System.out.println(DateTime.now().toString());
			list.clear();
			System.out.println(DateTime.now().toString());
			
		}
		
		System.out.println(size);
	}
	
	@Test
	public void testClearCacheByPrefixOfKey() {
//		riCache.clearCacheByPrefixOfKey();
	}

}
