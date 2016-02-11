<%-- 
    Document   : upload
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
                            <li><a href="Navigation?action=index">Home</a></li>
                            <li><a href="Navigation?action=decisionTree">Decision Tree</a></li>
                            <li><a href="Navigation?action=associationRules">Association Rules</a></li>
                        </ul>
                        <ul class="nav navbar-nav navbar-right">
                            <li class="active"><a href="Navigation?action=upload">Upload <span class="sr-only">(current)</span></a></li>
                        </ul>
                    </div>
                </div>
            </nav>
        </header>
        <div class="container">
            <%@include file="interfaceMessages.jsp" %>
            <form action="Upload" method="POST" class="form-horizontal col-lg-8" enctype="multipart/form-data">
                <fieldset>
                    <legend>Upload</legend>
                    <div class="form-group">
                        <label for="name" class="col-lg-2 control-label">Nome</label>
                        <div class="col-lg-10">
                            <input type="text" class="form-control" id="name" name="name" placeholder="Nome da Base" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="file" class="col-lg-2 control-label">Arquivo</label>
                        <div class="col-lg-10">
                            <input type="file" class="form-control" id="file" name="file" />
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-lg-10 col-lg-offset-2">
                            <button type="submit" class="btn btn-primary">Upload</button>
                        </div>
                    </div>
                </fieldset>                        
            </form>
        </div>
        <%@include file="interfaceFooter.jsp" %>
    </body>
</html>