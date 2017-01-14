package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import comm.jdbctools;
import comm.pinglun;

public class zpzservlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public zpzservlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		HttpSession session = request.getSession(true);
		
		String id = request.getParameter("id");
		String num = request.getParameter("num");
		String type = (String)session.getAttribute("type");
		if("dz".equals(id)){
			String userid = request.getParameter("userid");
			String ssid = request.getParameter("ssid");
			boolean ok = jdbctools.dianzan(userid, ssid);
			response.sendRedirect("../ss_main.jsp?userid="+userid+"&num="+num+"&type="+type);
		}else if("pl".equals(id)){
			String ssid = request.getParameter("ssid");
			String userid = request.getParameter("userid");
			String plnr = request.getParameter("plnr");
			
			pinglun pl = new pinglun();
			pl.setPlnr(plnr);
			pl.setSsid(ssid);
			try {
				pl.setUser(jdbctools.getUser(userid));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean b = jdbctools.addpinglun(pl);
			
			
			response.sendRedirect("../ss_main.jsp?type="+type+"&Myuserid="+userid+"&num="+num+"&plsf="+b);
		}else if("zf".equals(id)){
			String userid = (String)session.getAttribute("userid");
			String yuan_ssid = (String)request.getParameter("yuan_ssid");
			boolean b = jdbctools.zhuanfa(Integer.parseInt(userid),Integer.parseInt(yuan_ssid));
			if(b){
				response.sendRedirect("../ss_main.jsp?type="+type+"&Myuserid="+userid+"&num="+num);
			}else{
				System.out.println("×ª·¢Ê§°Ü");
				response.sendRedirect("../ss_main.jsp?type="+type+"&Myuserid="+userid+"&num="+num);
			}
		}else if("qxdz".equals(id)){
			String userid = request.getParameter("userid");
			String ssid = request.getParameter("ssid");
			boolean ok = jdbctools.quxiaodianzan(userid, ssid);
			response.sendRedirect("../ss_main.jsp?type="+type+"&Myuserid="+userid+"&num="+num);
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
