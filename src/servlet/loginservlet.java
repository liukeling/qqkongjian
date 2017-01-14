package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comm.jdbctools;
import comm.jdbcuilt;

public class loginservlet extends HttpServlet {

	public loginservlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession(true);
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html");

		String userid = request.getParameter("userid");
		String userpswd = request.getParameter("userpswd");
		String yanzhengma = request.getParameter("yanzhengma");

		if ("".equals(userid)||userid==null) {
			out.print("idnull");
		} else if ("".equals(userpswd)||userpswd==null) {
			out.print("pswdnull");
		} else {
			String veryCode = (String) session.getAttribute("veryCode");
			boolean ok = false;
			try {
				ok = jdbctools.isCunZai(userid, userpswd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (veryCode.equals(yanzhengma)) {
				if (ok) {
					session.setAttribute("isLine", "ok");
					session.setAttribute("userid", userid);
					// response.sendRedirect("../Mykongjian.jsp?userid="+userid+"&num=1");
					out.print("ok");
				} else {
					out.print("2");
				}
			} else {
				// response.sendRedirect("../index.jsp?xinxi=1");
				out.print("1");
			}
		}
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
