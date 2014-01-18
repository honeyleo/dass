package com.huizhi.dass.operation.web.api;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.huizhi.dass.common.util.MessageDigestUtil;
import com.huizhi.dass.operation.web.AbstractContextControllerTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountControllerTest extends AbstractContextControllerTest {

	@Autowired
	private AccountController accountController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
	}
	
	@Test
	public void testCreateLoginLog() {
		for (int i = 0; i < 10; i++) {
			String userId = "user_" + returnInt(100);
//			String gameId = returnInt(100) + "";
			String gameId = 1 + "";
			String type = returnInt(3) + "";
			String appSecret = "7921a44dd9fae4c2e5146b2abad01ffbc410c3d3";
			String sign = "appSecret=" + appSecret + "&gameId=" + gameId + "&type=" + type + "&userId=" + userId; 
			
			RequestBuilder builder = MockMvcRequestBuilders.get("/account").param("userId", userId).param("gameId", gameId).param("type", type).param("sign", MessageDigestUtil.getMD5(sign));
			try {
				ResultActions ra = mockMvc.perform(builder);
				MvcResult mr = ra.andReturn();
				String result = mr.getResponse().getContentAsString();
				System.out.println("======================" + result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private int returnInt(int n) {
		Random r = new Random();
		return r.nextInt(n) + 1;
	}

}
