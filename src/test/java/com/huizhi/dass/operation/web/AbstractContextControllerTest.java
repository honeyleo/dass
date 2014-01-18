package com.huizhi.dass.operation.web;

import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

//标记为web应用测试
@WebAppConfiguration
//指定配置文件
@ContextConfiguration({"classpath:spring/business-config.xml", "classpath:spring/tools-config.xml", "classpath:spring/mvc-core-config.xml"})
public abstract class AbstractContextControllerTest {
	
	@Autowired
	protected WebApplicationContext wac;
	
	@Autowired
	protected MockServletContext servletContext;
	
	@Autowired
	protected MockHttpServletRequest request;
	
	@Autowired
	protected MockHttpServletResponse response;
	
	@Autowired
	protected MockHttpSession session;
	
	@Autowired
	protected ServletWebRequest webRequest;
	
	//当前测试环境
	protected MockMvc mockMvc;
	
	

}
