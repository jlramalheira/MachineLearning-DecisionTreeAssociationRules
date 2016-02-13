<%-- 
    Document   : bothClassificationsView
    Created on : Feb 13, 2016, 6:53:00 PM
    Author     : joao
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="model.File"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    int correctsDecisionTree = (int) request.getAttribute("correctsDecisionTree");
    int correctsNaiveBayes = (int) request.getAttribute("correctsNaiveBayes");
    int totalTest = (int) request.getAttribute("totalTest");
    int totalTrainig = (int) request.getAttribute("totalTrainig");
    String fileName = (String) request.getAttribute("fileName");
%>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>TopType</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <header>
            <nav class="navbar navbar-inverse">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="Navigation?action=index">TopType</a>
                    </div>

                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav">
                            <li><a href="Navigation?action=index">Home</a></li>
                            <li><a href="Navigation?action=decisionTree">Árvore de Decisão </a></li>
                            <li><a href="Navigation?action=naiveBayes">Naive Bayes</a></li>
                            <li class="active"><a href="Navigation?action=bothClassifications">Árvore de Decisão e Naive Bayes <span class="sr-only">(current)</span></a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li><a href="Navigation?action=upload">Upload</a></li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <div class="container">
            <%@include file="interfaceMessages.jsp" %>
            <h2>Análise</h2>
            <div class="col-lg-4">
                <p><strong>Algoritmo Utilizado: </strong>J48 e Naive Bayes</p>
                <p><strong>Total de Elementos Teste: </strong><%=(totalTest)%></p>
                <p><strong>Total de Elementos Treinamento: </strong><%=(totalTrainig)%></p>
                <p><strong>Total de Elementos: </strong><%=(totalTest + totalTrainig)%></p>
                <p><strong>Percentual Treinamento Exato: </strong><%=new DecimalFormat("00.00").format((double) totalTest / (totalTest + totalTrainig) * 100)%>%</p>
                <form action="BothClassifications" method="GET">
                    <input type="hidden" name="fileName" value="<%=fileName%>" />
                    <div class="form-group">
                        <button type="submit" class="btn btn-default" name="action" id="download" value="download">Download Ambos</button>
                    </div>
                </form>
            </div>
            <div class="col-lg-7">
                <div id="chart_div"></div>
            </div>
            <div class="col-lg-1">

            </div>
        </div>
        <%@include file="interfaceFooter.jsp" %>

        <!--Load the AJAX API-->
        <script type="text/javascript" src="js/loader.js"></script>
        <script type="text/javascript">

            google.charts.load('current', {packages: ['corechart', 'bar']});
            google.charts.setOnLoadCallback(drawStacked);

            function drawStacked() {
                var data = google.visualization.arrayToDataTable([
                    ['Algorithm', 'Corretos', 'Incorretos'],
                    ['Árvore de Decisão', <%=correctsDecisionTree%>, <%=(totalTest - correctsDecisionTree)%>],
                    ['Naive Bayes', <%=correctsNaiveBayes%>, <%=(totalTest - correctsNaiveBayes)%>]
                ]);

                var options = {
                    title: 'Comparação dos Algoritmos',
                    chartArea: {width: '50%'},
                    isStacked: true,
                    hAxis: {
                        title: 'Classificação',
                        minValue: 0
                    },
                    vAxis: {
                        title: 'Algoritmo'
                    }
                };
                var chart = new google.visualization.BarChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }

        </script>
</html>