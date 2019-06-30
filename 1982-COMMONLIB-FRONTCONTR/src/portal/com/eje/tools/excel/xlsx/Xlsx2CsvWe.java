
/* ====================================================================
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==================================================================== */

package portal.com.eje.tools.excel.xlsx;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import cl.ejedigital.consultor.DataFields;
import cl.ejedigital.consultor.Field;
import cl.ejedigital.tool.misc.Formatear;
import cl.ejedigital.tool.validar.Validar;

/**
* A rudimentary XLSX -> CSV processor modeled on the
* POI sample program XLS2CSVmra by Nick Burch from the
* package org.apache.poi.hssf.eventusermodel.examples.
* Unlike the HSSF version, this one completely ignores
* missing rows.
* <p/>
* Data sheets are read using a SAX parser to keep the
* memory footprint relatively small, so this should be
* able to read enormous workbooks.  The styles table and
* the shared-string table must be kept in memory.  The
* standard POI styles table class is used, but a custom
* (read-only) class is used for the shared string table
* because the standard POI SharedStringsTable grows very
* quickly with the number of unique strings.
* <p/>
* Thanks to Eric Smith for a patch that fixes a problem
* triggered by cells with multiple "t" elements, which is
* how Excel represents different formats (e.g., one word
* plain and one word bold).
* 
* @author Chris Lott
*/
public class Xlsx2CsvWe {

 /**
  * The type of the data value is indicated by an attribute on the cell.
  * The value is usually in a "v" element within the cell.
  */
 enum xssfDataType {
     BOOL,
     ERROR,
     FORMULA,
     INLINESTR,
     SSTINDEX,
     NUMBER,
     Date
 }

 public enum CellExcelBasicTypes {
	 String,
	 Number,
	 Date
 }

 /**
  * Derived from http://poi.apache.org/spreadsheet/how-to.html#xssf_sax_api
  * <p/>
  * Also see Standard ECMA-376, 1st edition, part 4, pages 1928ff, at
  * http://www.ecma-international.org/publications/standards/Ecma-376.htm
  * <p/>
  * A web-friendly version is http://openiso.org/Ecma/376/Part4
  */
 class MyXSSFSheetHandler extends DefaultHandler {

     /**
      * Table with styles
      */
     private StylesTable stylesTable;

     /**
      * Table with unique strings
      */
     private ReadOnlySharedStringsTable sharedStringsTable;

     /**
      * Number of columns to read starting with leftmost
      */
     private final int minColumnCount;

     // Set when V start element is seen
     private boolean vIsOpen;

     // Set when cell start element is seen;
     // used when cell close element is seen.
     private xssfDataType nextDataType;

     // Used to format numeric cell values.
     private short formatIndex;
     private String formatString;
     private final DataFormatter formatter;

     private int thisColumn = -1;
     private String rowLetter = "";
     // The last column printed to the output stream
     private int lastColumnNumber = -1;

     // Gathers characters as they are seen.
     private StringBuilder value;

     private IXlsx2Row eventReceptor;
     private DataFields fila;
     private double filaNum;
     private boolean omitePrimeraFila;
     
     private int cantConvercionesError=0;
     private int cantConvercionesOK=0;
     
     private IReplaceField replaceField;
     private Map<String, CellExcelBasicTypes> definition;
     
     private XSSFCellStyle style;
     /**
      * Accepts objects needed while parsing.
      *
      * @param styles  Table of styles
      * @param strings Table of shared strings
      * @param cols    Minimum number of columns to show
      * @param target  Sink for output
      */
     public MyXSSFSheetHandler(
             StylesTable styles,
             ReadOnlySharedStringsTable strings,
             int cols,
             IXlsx2Row eventReceptor,
             boolean omitePrimeraFila,
             IReplaceField replace,
             Map<String, CellExcelBasicTypes> definition) {
         this.stylesTable = styles;
         this.sharedStringsTable = strings;
         this.minColumnCount = cols;
         this.value = new StringBuilder();
         this.nextDataType = xssfDataType.NUMBER;
         this.formatter = new DataFormatter();
         this.eventReceptor = eventReceptor;
         this.omitePrimeraFila = omitePrimeraFila;
         this.replaceField = replace;
         this.definition = definition;
         this.style = null;
         
         filaNum = 0;
     }
     
  

     /**
        * 
        */
     public void startElement(String uri, String localName, String name,
                              Attributes attributes) throws SAXException {

         if ("inlineStr".equals(name) || "v".equals(name)) {
             vIsOpen = true;
             // Clear contents cache
             value.setLength(0);
         }
         // c => cell
         else if ("c".equals(name)) {
             // Get the cell reference
             String r = attributes.getValue("r");
             int firstDigit = -1;
             for (int c = 0; c < r.length(); ++c) {
                 if (Character.isDigit(r.charAt(c))) {
                     firstDigit = c;
                     break;
                 }
             }
             thisColumn = nameToColumn(r.substring(0, firstDigit));
             rowLetter  = r.substring(0, firstDigit);
             if(lastColumnNumber > thisColumn || lastColumnNumber == -1) {
            	 fila = new DataFields();
            	          	 
            	  
             }
             
             if(filaNum > 0) {
            	 //System.out.println("["+ (int)filaNum  + " " + rowLetter+"]");
             }
             
             
             this.nextDataType = xssfDataType.NUMBER;
             this.formatIndex = -1;
             this.formatString = null;
             String cellType = attributes.getValue("t");
             String cellStyleStr = attributes.getValue("s");

             if ("b".equals(cellType))
                 nextDataType = xssfDataType.BOOL;
             else if ("e".equals(cellType))
                 nextDataType = xssfDataType.ERROR;
             else if ("inlineStr".equals(cellType))
                 nextDataType = xssfDataType.INLINESTR;
             else if ("s".equals(cellType))
                 nextDataType = xssfDataType.SSTINDEX;
             else if ("str".equals(cellType))
                 nextDataType = xssfDataType.FORMULA;
             else if (cellStyleStr != null) {
                 // It's a number, but almost certainly one
                 //  with a special style or format 
            	 int styleIndex = Validar.getInstance().validarInt(cellStyleStr,-1);
            	 style = stylesTable.getStyleAt(styleIndex);
                 
                 if("N".equals(r.substring(0, firstDigit)) || "M".equals(r.substring(0, firstDigit)) ){
                	 //System.out.println("Msg");
                 }

                 this.formatIndex = style.getDataFormat();
                 this.formatString = style.getDataFormatString();
                 if (this.formatString == null)
                      this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
             }
             
             CellExcelBasicTypes def = definition.get(rowLetter.toUpperCase()); 
             if(def == null) {
            	 def = definition.get(rowLetter.toLowerCase()); 
             }
             else if( def == null) {
            	 throw new RuntimeException("No existe definición para la columna:"+rowLetter);
             }
             
             if(def == CellExcelBasicTypes.Date) {
            	 this.nextDataType = xssfDataType.Date;
             }
         }

     }

     /*
        * (non-Javadoc)
        * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
        */
     public void endElement(String uri, String localName, String name) {

         String thisStr = null;


         // v => contents of a cell
         if ("v".equals(name)) {
             
        	 if(filaNum > 0) {
            	 //System.out.println("["+ (int)filaNum  + " " + rowLetter+"]");
             }
             
             // Process the value contents as required.
             // Do now, as characters() may be called more than once
             switch (nextDataType) {

                 case BOOL:
                     char first = value.charAt(0);
                     thisStr = first == '0' ? "FALSE" : "TRUE";
                     break;

                 case ERROR:
                     thisStr = "\"ERROR:" + value.toString() + '"';
                     break;

                 case FORMULA:
                     try {
                    	 thisStr =  value.toString();
                     }
                     catch (Exception e) {
                    	 thisStr = "\"ERROR:" + value.toString() + '"';
                    	 cantConvercionesError++;
                     }
                     
                     break;

                 case INLINESTR:
                     try {
                         XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                         thisStr = rtsi.toString();
                     }
                     catch(Exception e) {
                    	 thisStr = "\"ERROR:" + value.toString() + '"';
                    	 cantConvercionesError++;
                     }

                     break;

                 case SSTINDEX:
                     try {
                        
                        thisStr = getText(value.toString());
                     }
                     catch (NumberFormatException ex) {
                    	 thisStr = "\"ERROR:" + value.toString() + '"';
                    	 cantConvercionesError++;
                     }
                     catch(Exception e) {
                    	 thisStr = "\"ERROR:" + value.toString() + '"';
                    	 cantConvercionesError++;
                	 }
                     break;

                 case NUMBER:
                	 try {
	                      
                        String n = value.toString();
                        if (this.formatString != null) {
                        	Date dateJava = HSSFDateUtil.getJavaDate(Double.parseDouble(n));
                            thisStr = formatter.formatRawCellContents(Double.parseDouble(n), this.formatIndex, this.formatString);
                            
                        } else {
                        	thisStr = getNumber(n);
                        }
                        
 
                	 }
                	 catch(Exception e) {
                		 thisStr = "\"ERROR:" + value.toString() + '"';
                		 cantConvercionesError++;
                	 }
                     break;
                 case Date:
                	 String date = value.toString();
                	 try {
                    	
                    	 thisStr = getDate(date);
                	 }
                	 catch(Exception e) {
                		System.out.println("Error convert to date:["+this.rowLetter+this.filaNum+"]");
                		cantConvercionesError++;
                	 }
                	 break;
                 default:
                     thisStr = "(TODO: Unexpected type: " + nextDataType + ")";
                     cantConvercionesError++;
                     break;
             }

             // Output after we've seen the string contents
             // Emit commas for any fields that were missing on this row
             if (lastColumnNumber == -1) {
                 lastColumnNumber = 0;
             }
            
             // Might be the empty string.
             fila.put(String.valueOf(thisColumn+1), new Field(thisStr));

             // Update column
             if (thisColumn > -1)
                 lastColumnNumber = thisColumn;

         } else if ("row".equals(name)) {

             // Print out any missing commas if needed
             if (minColumns > 0) {
                 // Columns are 0 based
                 if (lastColumnNumber == -1) {
                     lastColumnNumber = 0;
                 }
                
             }

             // We're onto a new row
             if(eventReceptor != null) {
            	 if(omitePrimeraFila && filaNum == 0) {
            		
            	 }
            	 else {
                	 for(int i = 1 ; i <= minColumnCount ; i++) {
                    	 if(fila.get(String.valueOf(i)) == null) {
                    		 fila.put(String.valueOf(i), new Field(""));
                    	 }
                     }
                     
                     eventReceptor.row(fila);
            	 }            	 
             }

             lastColumnNumber = -1;
             filaNum++;
         }

     }
     
     public Object handleNumber(XSSFCellStyle style, String number) {
    	     List<String> formattedValues = new ArrayList<String>();

    	    if (style != null) {
    	        short formatIndex = style.getDataFormat();
    	        String formatString = style.getDataFormatString();
    	        if (formatString == null) {
    	            formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
    	        }
    	        if (formatString != null) {
    	            formattedValues.add(formatter.formatRawCellContents(Double.parseDouble(number), formatIndex, formatString));
    	            if (formatString.contentEquals("D/M/YYYY")) {
    	            	formattedValues.add(formatter.formatRawCellContents(Double.parseDouble(number), formatIndex, formatString));
    	            }
    	        }
    	    }else{
    	       formattedValues.add(number);
    	    }
    	    
    	    if(formattedValues.size() > 0) {
    	    	return formattedValues.get(0);
    	    }
    	    else {
    	    	return null;
    	    }
    	}
     

     private String getDate(String value) throws ParseException {
    	 Date dateJava = HSSFDateUtil.getJavaDate(Double.parseDouble(value));

    	 String thisStr = new CellDateFormatter(this.formatString).format(dateJava);
    	 formatString = this.formatString.replaceAll("m", "z");
    	 formatString = this.formatString.replaceAll("M", "m");
    	 formatString = this.formatString.replaceAll("z", "M");
    	 
    	 thisStr = Formatear.getInstance().toAnotherDate(thisStr, formatString, "yyyyMMdd hh:mm:ss");
    	 
    	 return thisStr;
     }
     
     private String getNumber(String value) throws Exception {
    	 NumberFormat f = NumberFormat.getInstance();
    	 f.setGroupingUsed(false);
    	 String thisStr = f.format(Double.parseDouble(value));
 	    	 
    	 if(replaceField != null) {
    		 thisStr = replaceField.replaceNumber(thisStr);
    	 }
    	 
    	 return thisStr;
     }
    
     private String getText(String value) throws Exception {
    	 int idx = Integer.parseInt(value);
         XSSFRichTextString rtss = new XSSFRichTextString();
         return sharedStringsTable.getEntryAt(idx); 
     }
     
     /**
      * Captures characters only if a suitable element is open.
      * Originally was just "v"; extended for inlineStr also.
      */
     public void characters(char[] ch, int start, int length)
             throws SAXException {
         if (vIsOpen)
             value.append(ch, start, length);
     }

     /**
      * Converts an Excel column name like "C" to a zero-based index.
      *
      * @param name
      * @return Index corresponding to the specified name
      */
     private int nameToColumn(String name) {
         int column = -1;
         for (int i = 0; i < name.length(); ++i) {
             int c = name.charAt(i);
             column = (column + 1) * 26 + c - 'A';
         }
         return column;
     }
     
     public int getConvercionesError() {
    	 return cantConvercionesError;
     }
  
 }

 ///////////////////////////////////////

 private OPCPackage xlsxPackage;
 private Integer minColumns;
 private IXlsx2Row rowHandler;
 private Boolean omitePrimeraFila;
 private IReplaceField replaceField;
 /**
  * Creates a new XLSX -> CSV converter
  *
  * @param pkg        The XLSX package to process
  * @param output     The PrintStream to output the CSV to
  * @param minColumns The minimum number of columns to output, or -1 for no minimum
  */
 public Xlsx2CsvWe(OPCPackage pkg, int minColumns, IXlsx2Row rowHandler, boolean omitePrimeraFila, IReplaceField replaceField) {
     this.xlsxPackage = pkg;
     this.minColumns = minColumns;
     this.rowHandler = rowHandler;
     this.omitePrimeraFila = omitePrimeraFila;
     this.replaceField = replaceField;
 }

 /**
  * Parses and shows the content of one sheet
  * using the specified styles and shared-strings tables.
  *
  * @param styles
  * @param strings
  * @param sheetInputStream
  */
 public void processSheet(
         StylesTable styles,
         ReadOnlySharedStringsTable strings,
         InputStream sheetInputStream ,
         Map<String, CellExcelBasicTypes> definition)
         throws IOException, ParserConfigurationException, SAXException {

     InputSource sheetSource = new InputSource(sheetInputStream);
     SAXParserFactory saxFactory = SAXParserFactory.newInstance();
     SAXParser saxParser = saxFactory.newSAXParser();
     XMLReader sheetParser = saxParser.getXMLReader();
     ContentHandler handler = new MyXSSFSheetHandler(styles, strings, this.minColumns, rowHandler, omitePrimeraFila, replaceField, definition);
     
     
     sheetParser.setContentHandler(handler);
     sheetParser.parse(sheetSource);
 
     System.out.println("Cantidad de Errores de conversión:"+((MyXSSFSheetHandler)handler).getConvercionesError() );
 }

 /**
  * Initiates the processing of the XLS workbook file to CSV.
  *
  * @throws IOException
  * @throws OpenXML4JException
  * @throws ParserConfigurationException
  * @throws SAXException
  */
 public void process(int pageIndex, Map<String, CellExcelBasicTypes> definition ) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

     ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
     XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
     StylesTable styles = xssfReader.getStylesTable();
     XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
     int index = 0;
     boolean encontro = false;
     
     while (iter.hasNext()) {
    	 InputStream stream = iter.next();
    	 
	    try {
	    	 if(pageIndex == index) {
	    		 encontro = true;
	             String sheetName = iter.getSheetName();
	             
	             System.out.println(sheetName + " [index=" + index + "]:");
	             processSheet(styles, strings, stream, definition);       
	         }
    	 }
    	 finally {
    		 stream.close();
    	 }

         ++index;
     }
     
     if(!encontro){
    	 System.out.println("Page: " + pageIndex + " [No fue encontrada]:");
     }
 }
 
 public void process(String pageName, Map<String, CellExcelBasicTypes> definition) throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

     ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
     XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
     StylesTable styles = xssfReader.getStylesTable();
     XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
     int index = 0;
     boolean encontro = false;
     
     while (iter.hasNext()) {
    	 InputStream stream = iter.next();
    	 
         String sheetName = iter.getSheetName();

         if(pageName.equals(sheetName)) {
        	 encontro = true;
             System.out.println(sheetName + " [index=" + index + "]:");
             processSheet(styles, strings, stream, definition);
             stream.close();
             
    	 }

         ++index;
     }
     
     if(!encontro){
    	 System.out.println("Page: " + pageName + " [No fue encontrada]:");
     }
    	 
 }

 
 public List<String> getSheets() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {

     XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
     XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
     List<String> lista = new LinkedList<String>();
     
     while (iter.hasNext()) {  	 
         String sheetName = iter.getSheetName();
         lista.add(sheetName);
     }
     
     return lista;
 }

 

}