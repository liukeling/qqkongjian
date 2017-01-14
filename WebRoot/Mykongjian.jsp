<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ page language="java" import="comm.*"%>
<%@ page language="java" import="java.awt.*"%>
<%@ page language="java" import="java.awt.event.*"%>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");

	String Myuserid = request.getParameter("userid");
	session.setAttribute("Myuserid",Myuserid);
	String s = request.getParameter("num");
	int num = 1;
	if(s != null){
		try{
			num = Integer.parseInt(s);
		}catch(Exception e){
			System.out.println("不能将\""+s+"\"转换为Integer类型");
		}
	}
	String ok = (String) session.getAttribute("isLine");
	if (!"ok".equals(ok)) {
		response.sendRedirect("index.jsp");
		return;
	}
	
	String beijingtu = "image/userkongjian_bj.jpg";
	
	ArrayList<shuoshuo> al = jdbctools.getshuoshuo(num, Myuserid);
	
	user u = jdbctools.getUser(Myuserid);
	
	String userName = u.getName();
	
	session.setAttribute("user_id",Myuserid);

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>我的空间</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<link rel="stylesheet" type="text/css" href="css/ss_main.css">
		<script type="text/javascript" src="javascripts/jquery-1.7.2.min.js" ></script>
		<script type="text/javascript" src="javascripts/kongjian.js"></script>
	</head>

	<body style="background-image:url(<%=beijingtu%>);">
		<table class="table2" align="center">
			<thead>
				<tr>
					<th>
						<table width="100%" height="100%" class="table1">
							<tr>
								<th>
									<font size="6" color="#033A15" ><%=userName%>&nbsp;用户空间</font>
								</th>
							</tr>
						</table>
					</th>
				</tr>
			</thead>
			<tr>
				<td align="right">
					<table width="100%" height="100%" class="table1">
						<tr>
							<td align="right">
								<center><font size="6"><a href="Myss.jsp" >说说</a></font></center>
								<br /><a href="servlet/logoutservlet"><font size="5" color="red" >注销</font></a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<table class="table2" width="900px">
						<tr>
							<td valign="top" width="20%">
								<table width="100%">
									<tr height="200" valign="top">
										<td>

											<table width="100%" class="table3">
												<tr height="200px" valign="top">
													<td>
														说说：<%=jdbctools.selectShuoshuo(Myuserid) %><br />
														 我最近的说说：<br />
														<textarea name="ssnr"
															style="width: 100%; height: 70px; resize: none"
															disabled="true"><%=jdbctools.getzuijinss(Myuserid).getNeirong() %></textarea><br />
														
													</td>
												</tr>
											</table>

										</td>
									</tr>
								</table>
							</td>

							<td valign="top" width="60%">
								<table width="100%">
									<tr height="200px" valign="top">
										<td class="table3">
											<form action="servlet/fabiaoservlet" method="post"
												onsubmit="return fb(this)">
												<table width="100%">
													<tr>
														<td>
															<textarea name="shuoshuoneirong"
																style="width: 575px; height: 100px; resize: none"></textarea>
														</td>
													</tr>
													<tr>
														<td align="right">
															<input type="submit" value="发表" />
														</td>
													</tr>
												</table>
											</form>
										</td>
									</tr>
									<tr>
										<td>
											<iframe height="900px" width="620px" src="ss_main.jsp?Myuserid=<%=Myuserid %>&num=<%=num %>&type=all" ></iframe>
										</td>
									</tr>
								</table>
							</td>
							<td valign="top" width="20%">
								<table width="100%">
									<tr valign="top" class="table3">
										<td>
											<a href="http://c.gdt.qq.com/gdt_click.fcg?viewid=FBR!bkpzRqC19gOTJ6tCz7rn1XZpGKnNbEGLUq7UHdiEjmiinAfARaRHADDzwglu4tU5XkRyZmubQKwwj8k2idal!gJK!hqWsimnRHPBwTyjDJnGj2JRtKuptut3GPkD0KoXqZAzeejV2tQSRoZmPM8M9vVyNz9s&jtype=0&i=1&os=0&s={%22aa%22:%221050%22,%22ab%22:%22509%22,%22ba%22:%221050%22,%22bb%22:%22509%22,%22g%22:%22-999%22,%22e%22:%22550%22,%22r%22:%222%22,%22p%22:%22-999%22,%22da%22:%22-999%22,%22db%22:%22-999%22}">
												<image src="./image/guanggao.jpg"  />
											</a>
										</td>
									</tr>
								</table>
							</td>

						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
