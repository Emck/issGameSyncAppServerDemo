package com.game.sync;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试掌聚互动接口,掌聚互动支付平台服务端数据同步接口测试Demo
 * @author issGame.com
 */
@SuppressWarnings("serial")
public class TestSync extends HttpServlet {
	private PrintWriter out = null;
	
	private String AppKey = "mI6n3DV98bfJZs4T";
	
	// 该Servlet的访问地址：http://testsync.game.com/TestSync

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		out = response.getWriter();
		
		String Agent = request.getParameter("A");		// 获取参数A
		String Parameter = request.getParameter("V");	// 获取参数V
		if (Agent == null || Agent.length() <=0 || Parameter == null || Parameter.length() <=0) {
			response.setStatus(204);					// 参数不完整，返回失败
			return;
		}
		
		Parameter = Crypter.decryptBase64(Parameter,AppKey);		// 使用您的AppKey解密V参数,Demo里使用的是AppID=100的AppKey
		System.out.println(Parameter);

		String Result;
		Result = "{\"Success\":false,\"msg\":\"这里是失败信息\"}";		// 失败...
		Result = "{\"Success\":true,\"msg\":\"成功无需返回此参数\"}";	// 成功...
		
		// 返回结果(加密数据)
		out.println(Crypter.encryptBase64(Result, AppKey));
		// 返回HTTPStatus = 200
		response.setStatus(HttpServletResponse.SC_OK);
	}
}