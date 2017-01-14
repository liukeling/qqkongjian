<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	response.setCharacterEncoding("utf-8");
	request.setCharacterEncoding("utf-8");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>欢迎登陆qq空间</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
		<style>
body {
	background-image: url("image/title_bj.jpg");
	text-align: center;
}

.tdname {
	color: red;
	font-weigth: bold;
}

.table1 {
	margin-top: 200px;
	background-image:url("image/denglu_bj.jpg");
	padding:30px;
	width:400px;
	height:200px;
}

.title {
	font-weight: bold;
	background-image: url("image/title_bj.jpg");
	width: 100%;
}

.main {
	width:1000px;
	height:500px;
	padding:1px;
	background-image:url("image/main_bj.jpg");
}
.body1{
	width:100%;
	margin-top:100px;
}
</style>
		<script type="text/javascript" src="javascripts/jquery-1.7.2.min.js"></script>
		<script>
	function test() {
		var userid = $("#userid").val();
		var userpswd = $("#userpswd").val();
		var veryCode = $("#yanzhengma").val();

		if ("" == userid || userid == null) {
			$("#xinxi").html("<font color='red'>用户名不能为空</font>");
			return;
		}
		if ("" == userpswd || userpswd == null) {
			$("#xinxi").html("<font color='red'>密码不能为空</font>");
			return;
		}

		var xmlhttp = new XMLHttpRequest();
		xmlhttp.open("POST", "servlet/loginservlet", false);
		xmlhttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlhttp.send("userid=" + userid + "&userpswd=" + userpswd
				+ "&yanzhengma=" + veryCode);
		var s = xmlhttp.responseText;

		if ("ok" == s) {
			window.location.href = "Mykongjian.jsp?userid=" + userid + "&num=1";
		} else if ("1" == s) {
			$("#xinxi").html("验证码错误");
		} else if ("2" == s) {
			$("#xinxi").html("登陆失败，用户名或密码错误");
		}
	}
</script>

	</head>

	<body>
		<div class="title" align="center">
			<font color="red" size="7">qq空间登录</font>
		</div>
		<div class="body1" align="center" >
			<div class="main" align="center">
				<table align="center" class="table1">
					<tr>
						<td>
							<font class="tdname">账号：</font>
						</td>
						<td>
							<input type="text" name="userid" id="userid" />
						</td>
					</tr>
					<tr>
						<td>
							<font class="tdname">密 码：</font>
						</td>
						<td>
							<input type="password" id="userpswd" />
						</td>
					</tr>
					<tr>
						<td>
							<font class="tdname">验证码</font>
						</td>
						<td>
							<input type="text" id="yanzhengma">
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<image src="servlet/veryCode" />
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<a onclick="javascript: test();"><image src="image/dengluanniu.jpg" />
							</a>
						</td>
					</tr>
					<tr>
						<td class="tdname">
							提示:
						</td>
						<td>
							<div id="xinxi"></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html>
