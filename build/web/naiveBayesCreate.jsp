<%-- 
    Document   : associationRulesCreate
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
            <div class="col-lg-8">
                <form action="NaiveBayes" method="POST" class="form-horizontal">
                    <input type="hidden" id="counter" name="counter" value="1"/>
                    <fieldset>
                        <legend>Naive Bayes</legend>
                        <div class="form-group">
                            <label for="name" class="col-lg-2 control-label">Nome</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control" id="name" name="name" placeholder="Nome Relação" />
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
                            <div id="rows" class="col-lg-10">
                                <div id="row-1" class="row">
                                    <div class="col-lg-1">
                                        <input type="radio" name="handle" title="Handle" id="hadle" value="1" checked="checked">
                                    </div>
                                    <div class="col-lg-3">
                                        <input type="text" class="form-control" id="column-1" name="column-1" placeholder="Titulo Coluna" />
                                    </div>
                                    <div class="col-lg-4">
                                        <input type="text" class="form-control" id="type-1" name="type-1" placeholder="Tipo" />
                                    </div>
                                    <div class="col-lg-3">
                                        <input type="number" class="form-control" min="0" id="position-1" name="position-1" placeholder="Posição" />
                                    </div>
                                    <div class="col-lg-1">
                                        <button type="button" class="btn btn-danger btn-sm" onclick="removeAtribute(this)" value="1">x</button>
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
                                <button type="submit" class="btn btn-primary" name="action" value="create">Criar</button>
                            </div>
                        </div>
                    </fieldset>                        
                </form>
            </div>
            <div class="col-lg-4">
                <button type="button" class="btn btn-success" onclick="adult()">Adult</button>
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

            function removeAtribute(elem) {
                var node = document.getElementById('row-' + elem.getAttribute('value'));
                node.parentNode.removeChild(node);
            }

            function createInput(name, id, type, placeholder, c, title, value) {
                var input = document.createElement('INPUT');

                input.setAttribute('name', name);
                input.setAttribute('id', id);
                input.setAttribute('type', type);
                input.setAttribute('title', title);
                input.setAttribute('value', value);
                input.setAttribute('placeholder', placeholder);
                input.className = c;

                return input;
            }

            function createButton(onclick, count, type) {
                var button = document.createElement('BUTTON');

                button.setAttribute('onclick', onclick + '(this)');
                button.setAttribute('value', count);
                button.setAttribute('type', type);
                button.className = 'btn btn-danger btn-sm';
                button.innerHTML = 'x';

                return button;
            }

            function addRow() {
                var div = document.createElement('DIV');
                var divHandle = document.createElement('DIV');
                var divColumn = document.createElement('DIV');
                var divType = document.createElement('DIV');
                var divPosition = document.createElement('DIV');
                var divButton = document.createElement('DIV');

                divHandle.className = 'col-lg-1';
                divColumn.className = 'col-lg-3';
                divType.className = 'col-lg-4';
                divPosition.className = 'col-lg-3';
                divButton.className = 'col-lg-1';

                var counter = document.getElementById('counter').getAttribute('value');
                counter++;
                document.getElementById('counter').setAttribute('value', counter);


                divHandle.appendChild(createInput('handle', 'handle-' + counter, 'radio', '', '', 'Handle', counter));
                divColumn.appendChild(createInput('column-' + counter, 'column-' + counter, 'text', 'Titulo Coluna', 'form-control', '', ''));
                divType.appendChild(createInput('type-' + counter, 'type-' + counter, 'text', 'Tipo', 'form-control', '', ''));
                divPosition.appendChild(createInput('position-' + counter, 'position-' + counter, 'text', 'Posição', 'form-control', '', ''));
                divButton.appendChild(createButton('removeAtribute', counter, 'button'));

                div.appendChild(divHandle);
                div.appendChild(divColumn);
                div.appendChild(divType);
                div.appendChild(divPosition);
                div.appendChild(divButton);

                div.setAttribute('id', 'row-' + counter);
                div.setAttribute('class', 'row');

                document.getElementById('rows').appendChild(div);
            }

            function adult(){
                document.getElementById('name').setAttribute('value', 'Adult');
                adjustRange(document.getElementById('range'));

                for (var i = 0; i < 14; i++) {
                    addRow();
                }

                document.getElementById('column-1').setAttribute('value', 'age');
                document.getElementById('column-2').setAttribute('value', 'workclass');
                document.getElementById('column-3').setAttribute('value', 'fnlwgt');
                document.getElementById('column-4').setAttribute('value', 'education');
                document.getElementById('column-5').setAttribute('value', 'education-num');
                document.getElementById('column-6').setAttribute('value', 'marital-status');
                document.getElementById('column-7').setAttribute('value', 'occupation');
                document.getElementById('column-8').setAttribute('value', 'relationship');
                document.getElementById('column-9').setAttribute('value', 'race');
                document.getElementById('column-10').setAttribute('value', 'sex');
                document.getElementById('column-11').setAttribute('value', 'capital-gain');
                document.getElementById('column-12').setAttribute('value', 'capital-loss');
                    document.getElementById('column-13').setAttribute('value', 'hours-per-week');
                    document.getElementById('column-14').setAttribute('value', 'native-country');
                document.getElementById('column-15').setAttribute('value', 'classificação');

                for (var i = 0; i < 15; i++) {
                document.getElementById('position-' + (i + 1)).setAttribute('value', i);
                }
                
                document.getElementById('type-1').setAttribute('value', 'numeric');
            document.getElementById('type-2').setAttribute('value', 'Private, Self-emp-not-inc, Self-emp-inc, Federal-gov, Local-gov, State-gov, Without-pay, Never-worked');
                document.getElementById('type-3').setAttribute('value', 'numeric');
                document.getElementById('type-4').setAttribute('value', 'Bachelors, Some-college, 11th, HS-grad, Prof-school, Assoc-acdm, Assoc-voc, 9th, 7th-8th, 12th, Masters, 1st-4th, 10th, Doctorate, 5th-6th, Preschool');
                document.getElementById('type-5').setAttribute('value', 'numeric');
                document.getElementById('type-6').setAttribute('value', 'Married-civ-spouse, Divorced, Never-married, Separated, Widowed, Married-spouse-absent, Married-AF-spouse');
                document.getElementById('type-7').setAttribute('value', 'Tech-support, Craft-repair, Other-service, Sales, Exec-managerial, Prof-specialty, Handlers-cleaners, Machine-op-inspct, Adm-clerical, Farming-fishing, Transport-moving, Priv-house-serv, Protective-serv, Armed-Forces');
                document.getElementById('type-8').setAttribute('value', 'Wife, Own-child, Husband, Not-in-family, Other-relative, Unmarried');
                document.getElementById('type-9').setAttribute('value', 'White, Asian-Pac-Islander, Amer-Indian-Eskimo, Other, Black');
                document.getElementById('type-10').setAttribute('value', 'Female, Male');
                document.getElementById('type-11').setAttribute('value', 'numeric');
                document.getElementById('type-12').setAttribute('value', 'numeric');
                document.getElementById('type-13').setAttribute('value', 'numeric');
                document.getElementById('type-14').setAttribute('value', 'native-United-States, Cambodia, England, Puerto-Rico, Canada, Germany, Outlying-US(Guam-USVI-etc), India, Japan, Greece, South, China, Cuba, Iran, Honduras, Philippines, Italy, Poland, Jamaica, Vietnam, Mexico, Portugal, Ireland, France, Dominican-Republic, Laos, Ecuador, Taiwan, Haiti, Columbia, Hungary, Guatemala, Nicaragua, Scotland, Thailand, Yugoslavia, El-Salvador, Trinadad&Tobago, Peru, Hong, Holand-Netherlands');
                document.getElementById('type-15').setAttribute('value', '>50K, <=50K');
                
                document.getElementById('handle-15').setAttribute('checked', 'checked');
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
                    document.getElementById('type-' + (i + 1)).setAttribute('value', 'real');
                    document.getElementById('position-' + (i + 1)).setAttribute('value', i);
                }
                document.getElementById('type-12').setAttribute('value', '0,1,2,3,4,5,6,7,8,9,10');
                document.getElementById('position-12').setAttribute('value', '11');

                document.getElementById('handle-12').setAttribute('checked', 'checked');
            }
        </script>
    </body>
</html>
<%    } else {
        response.sendRedirect("Navigation?action=index&base=true");
    }
%>
