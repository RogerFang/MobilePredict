<%--
  Created by IntelliJ IDEA.
  User: Roger
  Date: 2016/5/11
  Time: 23:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="/WEB-INF/jsp/include/taglib.jsp"%>
<html>
<head>
    <title><sitemesh:title/></title>
    <link href="${ctx}/static/css/bootstrap.min.css" rel="stylesheet">

    <script src="${ctx}/static/js/jquery-1.12.0.js" type="text/javascript"></script>
    <script src="${ctx}/static/js/jquery.form.js" type="text/javascript"></script>

    <style>
        .col-center-block {
            width:auto;
            display: table;
            margin-left: auto;
            margin-right: auto;
        }
        .col-text-center{
            text-align: center;
        }
    </style>
    <sitemesh:head/>
</head>
<body>
    <div class="container" style="width: 80%">
        <ul class="nav nav-tabs">
            <li id="displayNav"><a href="${ctx}/display">效果展示</a></li>
            <li id="systemNav"><a href="${ctx}/system">设置</a></li>
            <shiro:user><li class="pull-right"><a href="${ctx}/logout">退出</a></li></shiro:user>
        </ul>

        <div id="content" style="margin: 4%;">
            <sitemesh:body />
        </div>

        <%--<%@ include file="/WEB-INF/jsp/layout/footer.jsp"%>--%>
    </div>
<script src="${ctx}/static/js/bootstrap.min.js" type="text/javascript"></script>
</body>
</html>
