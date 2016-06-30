<%--
  Created by IntelliJ IDEA.
  User: Roger
  Date: 2016/5/19
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="default">
    <title>效果展示</title>

    <script>
        $(function (){
            $("#displayNav").addClass("active");
        });
    </script>
</head>
<body>
    <div class="row">
        <div class="col-lg-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    模型训练效果
                </div>
                <div class="panel-body">
                    <ul class="list-group">
                        <li class="list-group-item">
                            <div class="row">
                                <div class="col-lg-6">
                                    <label>模型</label>
                                </div>
                                <div class="col-lg-6">
                                    训练准确率
                                </div>
                            </div>
                        </li>
                        <c:forEach items="${trainRecords}" var="item">
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <label>${item.model}</label>
                                    </div>
                                    <div class="col-lg-6">
                                            ${item.trainPrecision}
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    模型预测效果
                </div>
                <div class="panel-body">
                    <ul class="list-group">
                        <li class="list-group-item">
                            <div class="row">
                                <div class="col-lg-6">
                                    <label>模型</label>
                                </div>
                                <div class="col-lg-6">
                                    预测准确率
                                </div>
                            </div>
                        </li>
                        <c:forEach items="${predictRecords}" var="item">
                            <li class="list-group-item">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <label>${item.model}</label>
                                    </div>
                                    <div class="col-lg-6">
                                        <c:if test="${item.predictPrecision ne -1}">
                                            ${item.predictPrecision}
                                        </c:if>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
