<%--
  Created by IntelliJ IDEA.
  User: Roger
  Date: 2016/5/23
  Time: 17:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="default">
    <title>用户登录</title>
</head>
<body>
    <form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal">
        <div class="control-group">
            <label for="username" class="control-label">名称:</label>
            <div class="controls">
                <input type="text" id="username" name="username"  value="${username}" class="input-medium required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="password" class="control-label">密码:</label>
            <div class="controls">
                <input type="password" id="password" name="password" class="input-medium required"/>
            </div>
        </div>
        <br>
        <br>
        <div class="control-group">
            <div class="controls">
                <input id="submit_btn" class="btn btn-primary" type="submit" value="登录"/>
            </div>
        </div>
    </form>
</body>
</html>
