<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"
	+request.getServerName()+":"
	+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Gogoing</title>
      
    <style>
    body {
	background-image:url('background.png');
}
    
	#search{
	width:78px;
	height:28px;
	font:20px "黑体"
	}
	
	#textArea{
	width:300px;
	height:30px;
	font:20px "黑体"
	}
	</style>

  </head>
  
  <body>
	<p align="center"><img src="G.png" style="width: 300px; height: 300px; "/></p>
	
	
	<form action="search.jsp" name="search" method="get" 
			enctype="application/x-www-form-urlencoded">
	<table border="0" height="30px" width="450px" align="center">		
		<tr>
			<td width ="66%"><input name="keyword" type="text" maxlength="100" 
					id="textArea" style="width: 335px; height: 40px; "></td>
			<td height="29" align="center"><input type="submit" value="搜索一下" 
					id = "search" style="width: 116px; height: 40px; "></td>
		</tr>
	</table>
	</form>
  </body>

</html>
