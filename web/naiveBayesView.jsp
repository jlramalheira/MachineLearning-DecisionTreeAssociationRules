<%-- 
    Document   : naiveBayesView
    Created on : Feb 13, 2016, 6:27:42 PM
    Author     : joao
--%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="model.File"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    int corrects = (int) request.getAttribute("corrects");
    int totalTest = (int) request.getAttribute("totalTest");
    int totalTrainig = (int) request.getAttribute("totalTrainig");
    String fileName = (String) request.getAttribute("fileName");
    int truePositive = (int) request.getAttribute("truePositive");
    int trueNegative = (int) request.getAttribute("trueNegative");
    int falsePositive = (int) request.getAttribute("falsePositive");
    int falseNegative = (int) request.getAttribute("falseNegative");
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
                            <li><a href="Navigation?action=decisionTree">Árvore de Decisão</a></li>
                            <li class="active"><a href="Navigation?action=naiveBayes">Naive Bayes <span class="sr-only">(current)</span></a></li>
                            <li><a href="Navigation?action=bothClassifications">Árvore de Decisão e Naive Bayes</a></li>
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
            <div class="col-lg-3">
                <h2>Análise</h2>
                <p><strong>Algoritmo Utilizado: </strong>Naive Bayes</p>
                <p><strong>Total de Elementos Teste: </strong><%=(totalTest)%></p>
                <p><strong>Total de Elementos Treinamento: </strong><%=(totalTrainig)%></p>
                <p><strong>Total de Elementos: </strong><%=(totalTest + totalTrainig)%></p>
                <p><strong>Percentual Treinamento Exato: </strong><%=new DecimalFormat("00.00").format((double) totalTest / (totalTest + totalTrainig) * 100)%>%</p>
                <form action="NaiveBayes" method="GET">
                    <input type="hidden" name="fileName" value="<%=fileName%>" />
                    <div class="form-group">
                        <button type="submit" class="btn btn-default" name="action" id="download" value="download">Download Naive Bayes</button>
                    </div>
                </form>
            </div>
            <div class="col-lg-5">
                <div id="chart_div"></div>
            </div>
            <div class="col-lg-4">
                <br />
                <br />
                <br />
                <div class="row">
                    <table class="table table-striped text-center">
                        <thead>
                            <tr>
                                <th></th>
                                <th>Predição Positiva</th>
                                <th>Predição Negativa</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>Caso Positivo</th>
                                <td><%=truePositive%></td>
                                <td><%=falseNegative%></td>
                            </tr>
                            <tr>
                                <th>Caso Negativo</th>
                                <td><%=falsePositive%></td>
                                <td><%=trueNegative%></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <p><strong>Precisão: </strong><%=new DecimalFormat("0.00").format((double) truePositive / (truePositive + falsePositive) * 100)%>%</p>
                    <p><strong>Taxa VP: </strong><%=new DecimalFormat("0.00").format((double) truePositive / (truePositive + falseNegative) * 100)%>%</p>
                    <p><strong>Taxa FP: </strong><%=new DecimalFormat("0.00").format((double) falsePositive / (falsePositive + trueNegative) * 100)%>%</p>
                </div>
            </div>
        </div>
        <%@include file="interfaceFooter.jsp" %>

        <!--Load the AJAX API-->
        <script type="text/javascript" src="js/loader.js"></script>
        <script type="text/javascript">

            // Load the Visualization API and the corechart package.
            google.charts.load('current', {'packages': ['corechart']});

            // Set a callback to run when the Google Visualization API is loaded.
            google.charts.setOnLoadCallback(drawChart);

            // Callback that creates and populates a data table,
            // instantiates the pie chart, passes in the data and
            // draws it.
            function drawChart() {

                // Create the data table.
                var data = new google.visualization.DataTable();
                data.addColumn('string', 'Topping');
                data.addColumn('number', 'Slices');
                data.addRows([
                    ['Correctos', <%=corrects%>],
                    ['Incorretos', <%=(totalTest - corrects)%>]
                ]);

                // Set chart options
                var options = {'title': 'Precisão da Naive Bayes',
                    'width': 500,
                    'height': 400};

                // Instantiate and draw our chart, passing in some options.
                var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
        </script>
</html>

