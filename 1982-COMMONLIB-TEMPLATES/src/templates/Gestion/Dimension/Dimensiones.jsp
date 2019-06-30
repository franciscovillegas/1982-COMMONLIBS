<%@page language="java" contentType="text/html;charset=iso-8859-1" import="java.io.*,javax.xml.transform.*,javax.xml.transform.sax.*,javax.xml.transform.stream.*,org.xml.sax.*"%>
<html>
<head>
<title>Meta Dimensiones</title>
<link href="../servlet/Tool?style/portalrrhh.css" rel="stylesheet" type="text/css">
</head>

<center>

<%
    String xmlOrigen = (String)request.getAttribute("xml");
    String workerImagesPath = this.getServletContext().getRealPath("/") + "templates/Gestion/Dimension/Dimension.xsl";
    String xslOrigen = workerImagesPath;

    Source xmlSource = new StreamSource(new StringBufferInputStream(xmlOrigen));
    Source xsltSource = new StreamSource(new File(xslOrigen));

    StringWriter cadenaSalida = new StringWriter();

    Result bufferResultado = new StreamResult(cadenaSalida);

    TransformerFactory factoriaTrans = TransformerFactory.newInstance();
    Transformer transformador = factoriaTrans.newTransformer(xsltSource);

    transformador.transform(xmlSource, bufferResultado);
    out.print(cadenaSalida.toString());

%>
</center>

</body>
</html>