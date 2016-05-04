package com.sogou.firstwebservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;



/**
 * Servlet implementation class WelcomeUserServlet
 */
@WebServlet("/WelcomeUserServlet")
public class WelcomeUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public WelcomeUserServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		//使用字节流读取发送过来的数据
//		BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
//		byte[] buffer = new byte[1024];
//		int len = -1;
//		StringBuffer sb = new StringBuffer();
//		while ((len = bis.read(buffer)) != -1) {
//			sb.append(new String(buffer,0,len));
//		}
//		bis.close();
//		System.out.println(sb.toString());
//		JSONObject json = JSONObject.fromObject(sb.toString());
		
		//使用字符流来读取发送过来的数据
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = bufferedReader.readLine()) != null) {
			sb.append(line);
		}
		bufferedReader.close();
		JSONObject json = JSONObject.fromObject(sb.toString());
		
		
		String name = json.getString("name");
		String password = json.getString("password");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		
		if (name.equals("admin")&&password.equals("123456")) {
			JSONObject rjson = new JSONObject();
			rjson.put("json",true);
			System.out.println("rjson="+rjson);
			response.getOutputStream().write(rjson.toString().getBytes("UTF-8"));
		}else {
			JSONObject rjson = new JSONObject();
			rjson.put("json", false);  
            System.out.println("rjson=" + rjson);  
            // response.getWriter().write(rjson.toString());//向客户端发送一个带有json对象内容的响应  
            response.getOutputStream().write(rjson.toString().getBytes("UTF-8"));//向客户端发送一个带有json对象内容的响应
		}
	}
	
	

}
