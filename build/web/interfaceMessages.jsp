<%-- MENSAGENS --%>
<%@page import="util.Message"%>
<%@page import="java.util.ArrayList"%>
<%
    ArrayList<Message> messages = (ArrayList<Message>) request.getAttribute("messages");
    if (messages == null) {
        messages = new ArrayList<Message>();
    }
    for (Message m : messages) {
        out.print(m.printMessage());
    }
%>