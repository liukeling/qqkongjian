<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="comm.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String Myuserid = (String)session.getAttribute("Myuserid");
String type = request.getParameter("type");
int num = Integer.parseInt(request.getParameter("num"));

if(type==null||"".equals(type)){
	type="all";
}
if(num==0){
	num = 1;
}
session.setAttribute("type",type);
user u = jdbctools.getUser(Myuserid);
ArrayList<shuoshuo> al = jdbctools.getshuoshuo(num, Myuserid);
if("me".equals(type)){
	al = jdbctools.getMyshuoshuo(Myuserid,num);
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'ss_main.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<link rel="stylesheet" type="text/css" href="css/ss_main.css">

		<script type="text/javascript" src="javascripts/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="javascripts/kongjian.js"></script>
	</head>

	<body>
		<table align="center">
			<%for(int i = 0; i < al.size(); i ++){
										shuoshuo ss = al.get(i);
										user u1 = jdbctools.getUser(ss.getUser().getZhanghao()+"");
									%>
			<tr height="200px" valign="top">
				<td class="table3">
					<input type="hidden" name="id" value="<%=u.getZhanghao() %>" />

					<table width="100%">
						<tr>
							<td>
								<div style="font-weight: bold;">
									发布人:<%=u1.getName() %>
									<%int yuanid = ss.getForm_ssid();
															if(yuanid != -1){ 
																shuoshuo yuan_ss = jdbctools.getshuoshuo_ssid(yuanid, Myuserid);
																String yuan_user_id = yuan_ss.getUser().getZhanghao()+"";
																user yuan_user = jdbctools.getUser(yuan_user_id);
															%>
									&nbsp;&nbsp;摘自<%=yuan_user.getName() %>的说说
									<%}else{ %>
									&nbsp;&nbsp;
									<%} %>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div style="font-weight: bold;">
									发布时间:<%=ss.getFabutime() %></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div style="font-weight: bold;">
									内容:
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<textarea name="ssnr"
									style="width: 575px; height: 100px; resize: none"
									disabled="true"><%=ss.getNeirong() %></textarea>
							</td>
						</tr>
						<tr>
							<td style="font-weight: bold;">

								<a onclick="javascript:plshow(<%=ss.getSsid() %>);"> <font
									color="blue">打开评论框</font>
								</a>&nbsp;
								<a onclick="javascript:plhide(<%=ss.getSsid() %>);"> <font
									color="blue">关闭评论框</font>
								</a>&nbsp;&nbsp;

								<a
									href="servlet/zpzservlet?id=zf&ssid=<%=ss.getSsid() %>&num=<%=num %>&yuan_ssid=<%=ss.getSsid() %>">转发</a>
								&nbsp;&nbsp;
								<%if(jdbctools.isDZ(u.getZhanghao()+"",ss.getSsid())){%>
								<a
									href="servlet/zpzservlet?id=dz&userid=<%=u.getZhanghao() %>&ssid=<%=ss.getSsid() %>&num=<%=num %>">点赞</a>
								<% }else{%>
								<a
									href="servlet/zpzservlet?id=qxdz&userid=<%=u.getZhanghao() %>&ssid=<%=ss.getSsid() %>&num=<%=num %>">取消点赞</a>
								<%} %>
								&nbsp; 点赞数：<%=ss.getDianzanshu() %>&nbsp;
								<%if("me".equals(type)){ %>
								<a
									href="./servlet/deletess?ssid=<%=ss.getSsid() %>&userid=<%=Myuserid %>&num=<%=num %>">删除</a>
								<% }%>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr class="pinyu">
				<td>
					<div id="<%=ss.getSsid()%>">
						<form action="servlet/zpzservlet" method="post">
							<input type="hidden" value="<%=num %>" name="num" />
							<input type="hidden" value="pl" name="id" />
							<input type="hidden" value="<%=ss.getSsid() %>" name="ssid" />
							<input type="hidden" value="<%=Myuserid %>" name="userid" />
							<textarea id="tex<%=ss.getSsid() %>" name="plnr"
								style="width: 575px; height: 50px; resize: none"></textarea>
							<br />
							<input type="submit" value="评论" name="submit" />
						</form>
					</div>
				</td>
			</tr>
			<%
									
									ArrayList<pinglun> plal = jdbctools.getpinglun(Integer.parseInt(ss.getSsid()));
									
										for(int k = 0; k < plal.size(); k++){
										pinglun pinglunss = plal.get(k);
									 %>
			<tr class="pinyu">
				<td>
					<%=pinglunss.getUser().getName() %>：<%=pinglunss.getPlnr() %>
				</td>
			</tr>

			<%
										}
									%>
			<tr>
				<td>
					<br />
				</td>
			</tr>
			<%
									} %>

			<tr>
				<td>
					<a href="ss_main.jsp?Myuserid=<%=Myuserid %>&num=<%=(num+1) %>">加载更多</a>
				</td>
			</tr>
		</table>
	</body>
</html>
