package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import comm.jdbctools;
import comm.pinglun;
import comm.shuoshuo;
import comm.user;

public class ShuoShuoJsonServer extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ShuoShuoJsonServer() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		JSONObject job = new JSONObject();

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("reason", "null");
		hm.put("result", "null");

		String id = request.getParameter("MyId");
		user u = null;
		if (!("".equals(id) || null == id)) {
			u = new user(Integer.parseInt(id));
			try {
				u = jdbctools.getUser(id);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("".equals(id) || null == id) {
			hm.put("reason", "id不能为空");
		} else if (u.getName() == null || "".equals(u.getName())) {
			hm.put("reason", "用户为空");
		} else {
			// 用户不为空则判断type：
			String type = request.getParameter("type");
			if ("select".equals(type)) {
				String ssid = request.getParameter("ssid");
				String selecttype = request.getParameter("selecttype");
				if (ssid == null || "".equals(ssid)) {
					hm.put("reason", "说说id不能为空");
				} else if (selecttype == null || "".equals(selecttype)) {
					hm.put("reason", "查询类型不能为空");
				} else {
					if (!"onlyOne".equals(selecttype)) {
						try {
							String me = request.getParameter("me");
							boolean getMe = Boolean.parseBoolean(me);
							ArrayList<shuoshuo> al = jdbctools.getshuoshuo(
									ssid, selecttype, getMe, id);
							JSONArray ja = new JSONArray();
							for (shuoshuo ss : al) {
								JSONObject jo = new JSONObject();
								jo.put("fromssid", ss.getForm_ssid());
								jo.put("fabutime", ss.getFabutime());
								jo.put("isdz", ss.isdz());
								JSONObject j = new JSONObject();
								j.put("userzhanghao", ss.getUser()
										.getZhanghao());
								j.put("username", ss.getUser().getName());
								jo.put("user", j);
								jo.put("ssid", ss.getSsid());
								jo.put("contact", ss.getNeirong());
								jo.put("dianzannumbeer", ss.getDianzanshu());
								ja.add(jo);
							}
							hm.put("reason", "获取成功");
							hm.put("result", ja);
						} catch (Exception e) {
							hm.put("reason", "me 字段错误");
						}
					} else {
						try {
							shuoshuo ss = jdbctools.getshuoshuo_ssid(Integer
									.parseInt(ssid), id);
							JSONObject jo = new JSONObject();
							jo.put("fromssid", ss.getForm_ssid());
							jo.put("fabutime", ss.getFabutime());
							jo.put("isdz", ss.isdz());
							JSONObject j = new JSONObject();
							j.put("userzhanghao", ss.getUser().getZhanghao());
							j.put("username", ss.getUser().getName());
							jo.put("user", j);
							jo.put("ssid", ss.getSsid());
							jo.put("contact", ss.getNeirong());
							jo.put("dianzannumbeer", ss.getDianzanshu());
							hm.put("reason", "获取成功");
							hm.put("result", jo);
						} catch (Exception e) {
							hm.put("reason", "请求出错");
						}
					}
				}
			} else if ("del".equals(type)) {
				String ssid = request.getParameter("ssid");
				try {
					shuoshuo ss = jdbctools.getshuoshuo_ssid(Integer
							.parseInt(ssid), id);
					if (id != null && id.equals(ss.getUser().getZhanghao() + "")) {
						boolean ok = jdbctools.deletess(Integer.parseInt(ssid),
								ss.getForm_ssid());
						if (ok) {
							hm.put("reason", "删除成功");
						} else {
							hm.put("reason", "删除失败");
						}
					} else {
						hm.put("reason", "不能删除他人的说说!!");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if ("add".equals(type)) {
				String contect = request.getParameter("contect");
				shuoshuo ss = new shuoshuo();
				try {
					ss.setUser(jdbctools.getUser(id));
					contect = new String(contect.getBytes("ISO-8859-1"), "UTF-8");
					ss.setNeirong(contect);
					boolean ok = jdbctools.addshuoshuo(ss);
					if (ok) {
						hm.put("reason", "添加请求成功");
					} else {
						hm.put("reason", "添加失败");
					}
				} catch (Exception e) {
					hm.put("reason", "添加失败");
				}
			} else if ("replace".equals(type)) {
				String yuanssid = request.getParameter("yuanssid");
				try {
					boolean ok = jdbctools.zhuanfa(Integer.parseInt(id),
							Integer.parseInt(yuanssid));
					if (ok) {
						hm.put("reason", "转发成功");
					} else {
						hm.put("reason", "转发失败");
					}
				} catch (Exception e) {
					hm.put("reason", "转发失败");
				}
			} else if ("dz".equals(type)) {
				String ssid = request.getParameter("ssid");
				boolean isdz = jdbctools.isDZ(id, ssid);
				if (isdz) {
					boolean ok = jdbctools.dianzan(id, ssid);
					if (ok) {
						hm.put("reason", "点赞成功");
					} else {
						hm.put("reason", "点赞失败");
					}
				} else {
					boolean ok = jdbctools.quxiaodianzan(id, ssid);
					if (ok) {
						hm.put("reason", "取消点赞成功");
					} else {
						hm.put("reason", "取消点赞失败");
					}
				}
				hm.put("result", ssid);
			} else if ("addpl".equals(type)) {
				String contect = request.getParameter("contect");
				String ssid = request.getParameter("ssid");
				pinglun pl = new pinglun();
				pl.setUser(new user(Integer.parseInt(id)));
				contect = new String(contect.getBytes("ISO-8859-1"), "UTF-8");
				pl.setPlnr(contect);
				pl.setSsid(ssid);
				if (!("".equals(contect) || contect == null)) {
					boolean ok = jdbctools.addpinglun(pl);
					if (ok) {
						hm.put("reason", "评论成功");
					} else {
						hm.put("reason", "评论失败");
					}
				} else {
					hm.put("reason", "评论内容不能为空");
				}
			} else if ("selectpl".equals(type)) {
				String selecttype = request.getParameter("plselecttype");
				String ssid = request.getParameter("ssid");
				if ("".equals(ssid) || ssid == null) {
					hm.put("reason", "说说id不能为空");
				} else {
					ArrayList<pinglun> pls = jdbctools.getpinglun(Integer
							.parseInt(ssid));
					if ("selectlast".equals(selecttype)) {
						if (pls.size() != 0) {
							pinglun pl = pls.get(0);
							JSONObject j = new JSONObject();
							JSONObject user = new JSONObject();
							user.put("userid", pl.getUser().getZhanghao());
							user.put("username", pl.getUser().getName());
							j.put("user", user);
							j.put("ssid", pl.getSsid());
							j.put("plnr", pl.getPlnr());
							j.put("pltime", pl.getPltime());
							hm.put("result", j);
						} else {
							hm.put("result", "null");
						}
						hm.put("reason", "获取成功");
					}else if("selectall".endsWith(selecttype)){
						//TODO
						JSONArray ja = new JSONArray();
						if(pls.size() != 0){
							for(pinglun pl : pls){
								JSONObject j = new JSONObject();
								JSONObject user = new JSONObject();
								user.put("userid", pl.getUser().getZhanghao());
								user.put("username", pl.getUser().getName());
								j.put("user", user);
								j.put("ssid", pl.getSsid());
								j.put("plnr", pl.getPlnr());
								j.put("pltime", pl.getPltime());
								ja.add(j);
							}
						}
						hm.put("result", ja);
						hm.put("reason", "获取成功");
					}
				}
			}else if("selectdzuser".equals(type)){
				String ssid = request.getParameter("ssid");
				if(!(ssid == null || "".equals(ssid))){
					JSONArray ja = new JSONArray();
					ArrayList<user> users = jdbctools.getDzusers(ssid);
					for(user dzu : users){
						JSONObject j = new JSONObject();
						j.put("userid", dzu.getZhanghao());
						j.put("username", dzu.getName());
						ja.add(j);
					}
					hm.put("result", ja);
					hm.put("reason", "获取成功");
				}else{
					hm.put("reason", "说说id不能为空");
				}
			}else {
				hm.put("reason", "请求的type未识别");
			}
		}

		job.accumulateAll(hm);
		response.getWriter().append(job.toString()).flush();
		response.getWriter().close();
		request.getReader().close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
