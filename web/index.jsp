<%-- 
    Document   : index
    Created on : Feb 10, 2016, 11:36:43 AM
    Author     : joao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>TopType</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
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
                            <li class="active"><a href="Navigation?action=index">Home <span class="sr-only">(current)</span></a></li>
                            <li><a href="Navigation?action=decisionTree">Árvore de Decisão</a></li>
                            <li><a href="Navigation?action=naiveBayes">Naive Bayes</a></li>
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
            <div class="row">
                <h3>Bem vindo ao Toptype!</h3>
                <p>O <strong>TopType</strong>  uma ferramenta Web para classificação dos dados a partir do aprendizado de máquina.</p>
            </div>
        </div>
        <%@include file="interfaceFooter.jsp" %>
    </body>
</html>