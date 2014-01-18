package com.huizhi.dass.manager.service.impl;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huizhi.dass.manager.service.AdminMenuService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/business-config.xml")
public class AdminMenuServiceImplTest {

	@Resource
	AdminMenuService service;
	
	@Test
	public void test() {
		service.getMeneListByAdminId(1l);
	}

}
