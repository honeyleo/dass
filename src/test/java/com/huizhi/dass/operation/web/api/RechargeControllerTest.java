package com.huizhi.dass.operation.web.api;

import static org.junit.Assert.*;

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
public class RechargeControllerTest extends AbstractContextControllerTest {

	@Autowired
	private RechargeController rController;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.standaloneSetup(rController).build();
	}
	
	@Test
	public void testCreateRechargeInfo() {
		for (int i = 0; i < 10; i++) {
			String userId = "user_" + returnInt(100);
//			String gameId = returnInt(100) + "";
			String gameId = 1 + "";
			String money = returnInt(1000) * 100 + "";
			String appSecret = "8921a44dd9fae4c2e5146b2abad01ffbc410c3d3";
			String sign = "appSecret=" + appSecret + "&deviceId=" + userId + "&gameId=" + gameId + "&money=" + money; 
			
			RequestBuilder builder = MockMvcRequestBuilders.get("/recharge").param("deviceId", userId).param("gameId", gameId).param("money", money).param("sign", MessageDigestUtil.getMD5(sign));
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
