package com.sentaroh.android.ConvertStringResourceToTsv;

/*
The MIT License (MIT)
Copyright (c) 2020-2020 Sentaroh

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
and to permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ConvertStringResourceToTsv {
	
	public static void main(String[] args) {
		
		if (args.length!=2) {
			System.out.println("Invalid argument");
			System.exit(16);
		}
			      
		XmlPullParserFactory factory;
		System.out.println("Conversion started");
		try {

             PrintWriter pw = new PrintWriter(new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(args[1]),"UTF-8")));
                			
			 BufferedReader fReader = new BufferedReader(
					 new InputStreamReader(new FileInputStream(new File(args[0])), "UTF-8"), 1024*1024);
			 StringBuffer xmlBuffer = new StringBuffer();
			 String line;
			 while((line = fReader.readLine()) != null){
			   xmlBuffer.append(line.trim());
			 }
			
			 factory = XmlPullParserFactory.newInstance();
			 factory.setNamespaceAware(true);
		     XmlPullParser xpp = factory.newPullParser();
		     xpp.setInput(new StringReader(xmlBuffer.toString()));
		    
		     
		     String array_name="";
		     
		     String elementName;
		      int eventType = xpp.getEventType();
		            
		      while (eventType != XmlPullParser.END_DOCUMENT) {
		        if(eventType == XmlPullParser.START_DOCUMENT) {
		          // NOP
		        } else if(eventType == XmlPullParser.START_TAG) {
		          elementName = xpp.getName();
		          if(elementName.equals("string")){
//		        	  System.out.println(xpp.getAttributeName(0)+ " = "+ xpp.getAttributeValue(0));
		        	  String key_name=xpp.getAttributeValue(0);
		        	  String value=xpp.nextText();
		        	  value=value.replaceAll("&", "&amp;");
		        	  value=value.replaceAll("<", "&gt;");
		        	  value=value.replaceAll(">", "&lt;");
		        	  pw.println(key_name+"\t"+value);
		          } else if(elementName.equals("string-array")){
//				      System.out.println(xpp.getAttributeName(0)+ " = "+ xpp.getAttributeValue(0));
				      array_name=xpp.getAttributeValue(0);
		          } else if(elementName.equals("item")){
		        	  pw.println(array_name+"\t"+xpp.nextText());
		          }
		        } else if(eventType == XmlPullParser.END_TAG) {
//		        	NOP
//		        	System.out.println("end_tag = "+ xpp.getName());
		        }
		        eventType = xpp.next();  
		      }
		     
		     pw.flush();
		     pw.close();
		     
		     System.out.println("Conversion ended");
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
