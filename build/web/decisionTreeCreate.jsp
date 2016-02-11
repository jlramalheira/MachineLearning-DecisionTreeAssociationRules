<%-- 
    Document   : decisionTreeCreate
    Created on : Feb 10, 2016, 9:10:17 PM
    Author     : joao
--%>

<%@page import="model.File"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<File> files = (List<File>) request.getAttribute("files");
    if (files != null) {
%>
<!DOCTYPE html>
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
                            <li class="active"><a href="Navigation?action=decisionTree">Decision Tree <span class="sr-only">(current)</span></a></li>
                            <li><a href="Navigation?action=associationRules">Association Rules</a></li>
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
            <div class="col-lg-8">
                <form action="DecisionTree" method="POST" class="form-horizontal">
                    <input type="hidden" id="counter" name="counter" value="1"/>
                    <fieldset>
                        <legend>Decision Tree</legend>
                        <div class="form-group">
                            <label for="name" class="col-lg-2 control-label">Nome</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control" id="name" name="name" placeholder="Nome Árvore" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="file" class="col-lg-2 control-label">Base</label>
                            <div class="col-lg-10">
                                <select class="form-control" id="file" name="file">
                                    <%for (File file : files) {%>
                                    <option value="<%=file.getPath()%>"><%=file.getName()%></option>
                                    <%} %>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="range" class="col-lg-2 control-label">Treinamento</label>
                            <div class="col-lg-9">
                                <input type="range" min="0" max="100" step="10" value="70" class="form-control" id="range" name="range" />
                            </div>
                            <div class="col-lg-1">
                                <strong><span id="percentValue">70</span>%</strong>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="column-1" class="col-lg-2 control-label">Colunas:</label>
                            <div id="rows">
                                <div>
                                    <div class="col-lg-3">
                                        <input type="text" class="form-control" id="column-1" name="column-1" placeholder="Titulo Coluna" />
                                    </div>
                                    <div class="col-lg-5">
                                        <input type="text" class="form-control" id="type-1" name="type-1" placeholder="Tipo" />
                                    </div>
                                    <div class="col-lg-2">
                                        <input type="number" class="form-control" min="0" id="position-1" name="position-1" placeholder="Posição" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12 text-right">
                                <button type="button" class="btn btn-default" onclick="addRow()">+</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-10 col-lg-offset-2">
                                <button type="submit" class="btn btn-primary" name="action" value="create">Create</button>
                            </div>
                        </div>
                    </fieldset>                        
                </form>
            </div>
            <div class="col-lg-4">
                <button type="button" class="btn btn-success" onclick="wineQuality()">Wine Quality</button>
            </div>
        </div>
        <%@include file="interfaceFooter.jsp" %>
        <script>
            $("#range").on("input", function () {
                adjustRange(this);
            });

            function adjustRange(element) {
                document.getElementById('percentValue').innerHTML = $("#range").val();
                var value = $("#range").val() / 100;

                $(element).css('background-image',
                        '-webkit-gradient(linear, left top, right top, '
                        + 'color-stop(' + value + ', #DEDEDE), '
                        + 'color-stop(' + value + ', #FFFFFF)'
                        + ')'
                        );
            }

            function createInput(name, type, placeholder, c) {
                var input = document.createElement('INPUT');

                input.setAttribute('name', name);
                input.setAttribute('id', name);
                input.setAttribute('type', type);
                input.setAttribute('placeholder', placeholder);
                input.className = c;

                return input;
            }

            function addRow() {
                var div = document.createElement('DIV');
                var divColumn = document.createElement('DIV');
                var divType = document.createElement('DIV');
                var divPosition = document.createElement('DIV');
                var divSpace = document.createElement('DIV');

                divColumn.className = 'col-lg-3';
                divType.className = 'col-lg-5';
                divPosition.className = 'col-lg-2';
                divSpace.className = 'col-lg-2';

                var counter = document.getElementById('counter').getAttribute('value');
                counter++;
                document.getElementById('counter').setAttribute('value', counter);


                divColumn.appendChild(createInput('column-' + counter, 'text', 'Titulo Coluna', 'form-control'));
                divType.appendChild(createInput('type-' + counter, 'text', 'Tipo', 'form-control'));
                divPosition.appendChild(createInput('position-' + counter, 'text', 'Posição', 'form-control'));

                div.appendChild(divSpace);
                div.appendChild(divColumn);
                div.appendChild(divType);
                div.appendChild(divPosition);

                document.getElementById('rows').appendChild(div);
            }

            function wineQuality() {
                document.getElementById('name').setAttribute('value', 'WineQuality');
                adjustRange(document.getElementById('range'));

                for (var i = 0; i < 11; i++) {
                    addRow();
                }
                
                document.getElementById('column-1').setAttribute('value', 'fixedAcidity');
                document.getElementById('column-2').setAttribute('value', 'volatileAcidity');
                document.getElementById('column-3').setAttribute('value', 'citricAcid');
                document.getElementById('column-4').setAttribute('value', 'residualSugar');
                document.getElementById('column-5').setAttribute('value', 'chlorides');
                document.getElementById('column-6').setAttribute('value', 'freeSulfurDioxide');
                document.getElementById('column-7').setAttribute('value', 'totalSulfurDioxide');
                document.getElementById('column-8').setAttribute('value', 'density');
                document.getElementById('column-9').setAttribute('value', 'pH');
                document.getElementById('column-10').setAttribute('value', 'sulphates');
                document.getElementById('column-11').setAttribute('value', 'alcohol');
                document.getElementById('column-12').setAttribute('value', 'quality');
                
                for (var i = 0; i < 11; i++) {
                    document.getElementById('type-'+(i+1)).setAttribute('value', 'real');
                    document.getElementById('position-'+(i+1)).setAttribute('value', i);
                }
                document.getElementById('type-12').setAttribute('value', '0,1,2,3,4,5,6,7,8,9,10');
                document.getElementById('position-12').setAttribute('value', '11');
            }
        </script>
    </body>
</html>
<%    } else {
        response.sendRedirect("Navigation?action=index&base=true");
    }
%>
