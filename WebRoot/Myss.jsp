<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="comm.*" %>
<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String ok = (String) session.getAttribute("isLine");
	String num = request.getParameter("num");
	if(num == null || "".equals(num)){
		num = "1";
	}
	
	
	if (!"ok".equals(ok)) {
		response.sendRedirect("index.jsp");
		return;
	}
	String Myuserid = (String)session.getAttribute("Myuserid");
	user me = jdbctools.getUser(Myuserid);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>我的说说</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="javascripts/kongjian.js"></script>
	<style>
		body{
			background-image:url("image/userkongjian_bj.jpg");
		}
		.title{
			width:100%;
			font-size:40;
		}
	</style>
	<script>
		function changeSS(type){
			document.getElementById("ss").src ="ss_main.jsp?Myuserid=<%=Myuserid %>&num=<%=num %>&type="+type;
		}
	</script>
	
	</head>

	<body>
		<table width="1000px" align="center">
			<thead>
				<tr>
					<td align="center" class="title" colspan="2" >
						<%=me.getName() %>的说说
					</td>
				</tr>
			</thead>
			<tr>
				<td colspan="2">
					<center><a href="Mykongjian.jsp?userid=<%=Myuserid %>&num=1" >个人主页</a></center><br /><a href="servlet/logoutservlet">注销</a>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td align="center">
								<input type="button" onclick ="changeSS('all')" value="说说首页" />
							</td>
							<td align="center">
							<input type="button" onclick ="changeSS('me')" value="个人说说" />
								
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div>
						<form action="servlet/fabiaoservlet" method="post"onsubmit="return fb(this)">
							<input type="hidden" name="location" value="Myss.jsp" />
							<table width="100%">
								<tr>
									<td>
										<textarea name="shuoshuoneirong"
											style="width: 100%; height: 100px; resize: none"></textarea>
									</td>
								</tr>
								<tr>
									<td align="right">
										<input type="submit" value="发表" />
									</td>
								</tr>
							</table>
						</form>
						<iframe height="1000px" width="620px" src="ss_main.jsp?Myuserid=<%=Myuserid %>&num=<%=num %>&type=me" id="ss" ></iframe>
					</div>
				</td>
				<td width="30%" align="left" valign="top" >
					说说：共<%=jdbctools.selectShuoshuo(Myuserid) %>条<br />
				</td>
			</tr>
		</table>
	</body>
</html>
