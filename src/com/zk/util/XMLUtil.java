package com.zk.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLUtil
{

    public XMLUtil()
    {
        doc = null;
        root = null;
    }

    public XMLUtil(String strFilename)
    {
        doc = null;
        root = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try
        {
            db = dbf.newDocumentBuilder();
        }
        catch(ParserConfigurationException pce)
        {
            logger.error(pce);
            throw new RuntimeException("xml init error");
        }
        try
        {
            doc = db.parse(strFilename);
        }
        catch(DOMException dom)
        {
            logger.error(dom);
            throw new RuntimeException("xml init error");
        }
        catch(IOException ioe)
        {
            logger.error(ioe);
            throw new RuntimeException("xml init error");
        }
        catch(SAXException saxe)
        {
            logger.error(saxe);
            throw new RuntimeException("xml init error");
        }
        root = doc.getDocumentElement();
    }

    public XMLUtil(InputStream is)
    {
        doc = null;
        root = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try
        {
            db = dbf.newDocumentBuilder();
        }
        catch(ParserConfigurationException pce)
        {
            logger.error(pce);
            throw new RuntimeException("xml init error");
        }
        try
        {
            doc = db.parse(is);
        }
        catch(DOMException dom)
        {
            logger.error(dom);
            throw new RuntimeException("xml init error");
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            logger.error(ioe);
            throw new RuntimeException("xml init error");
        }
        catch(SAXException saxe)
        {
            logger.error(saxe);
            throw new RuntimeException("xml init error");
        }
        root = doc.getDocumentElement();
    }

    public Element getRoot()
    {
        return root;
    }

    public Document getDocument()
    {
        return doc;
    }

    public void ouputXML(String filepath)
    {
        try
        {
            OutputFormat format = new OutputFormat(doc);
            format.setLineSeparator("\r\n");
            format.setIndenting(true);
            format.setEncoding("utf-8");
            FileWriter fout = new FileWriter(filepath);
            XMLSerializer serial = new XMLSerializer(fout, format);
            serial.asDOMSerializer();
            serial.serialize(doc);
            fout.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error(e);
        }
    }

    public void ouputXML_ZH(String filepath)
    {
        try
        {
            OutputFormat format = new OutputFormat(doc);
            format.setLineSeparator("\r\n");
            format.setIndenting(true);
            format.setEncoding("GB2312");
            FileWriter fout = new FileWriter(filepath);
            XMLSerializer serial = new XMLSerializer(fout, format);
            serial.asDOMSerializer();
            serial.serialize(doc);
            fout.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            logger.error(e);
        }
    }

    public void ouputXML_ZH_utf8(String filepath)
    {
        TransformerFactory trf = TransformerFactory.newInstance();
        try
        {
            Transformer tf = trf.newTransformer();
            DOMSource source = new DOMSource(doc);
            tf.setOutputProperty("encoding", "UTF-8");
            tf.setOutputProperty("indent", "yes");
            OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8");
            StreamResult result = new StreamResult(pw);
            tf.transform(source, result);
        }
        catch(TransformerConfigurationException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(TransformerException e)
        {
            System.out.println(e.getMessage());
        }
        catch(UnsupportedEncodingException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void ouputXML(OutputStream os)
    {
        try
        {
            OutputFormat format = new OutputFormat(doc);
            format.setLineSeparator("\r\n");
            format.setIndenting(true);
            format.setEncoding("utf-8");
            XMLSerializer serial = new XMLSerializer(os, format);
            serial.asDOMSerializer();
            serial.serialize(doc);
        }
        catch(Exception e)
        {
            logger.error(e);
        }
    }

    public Element getElementByTagName(String tagname, Element node)
    {
        return (Element)node.getElementsByTagName(tagname).item(0);
    }

    public List<Node> getElementsByTagName(String tagname, Element node)
    {
        return getElementsByTagName(tagname, node, true);
    }

    public List<Node> getElementsByTagName(String tagname, Element node, boolean isAll)
    {
        List<Node> list = new ArrayList<Node>();
        NodeList nodelist = node.getElementsByTagName(tagname);
        if(isAll)
        {
            for(int i = 0; i < nodelist.getLength(); i++)
                list.add(nodelist.item(i));

        } else
        {
            for(int i = 0; i < nodelist.getLength(); i++)
                if(nodelist.item(i).getParentNode() != null && nodelist.item(i).getParentNode().equals(node))
                    list.add(nodelist.item(i));

        }
        return list;
    }

    public String getValue(String path)
    {
        path = path.replace(".", "/");
        String paths[] = path.split("/");
        Element element = getRoot();
        String arr$[] = paths;
        int len$ = arr$.length;
        int i$ = 0;
        do
        {
            if(i$ >= len$)
                break;
            String path1 = arr$[i$];
            if(element == null)
                break;
            element = getElementByTagName(path1, element);
            i$++;
        } while(true);
        if(element == null || element.getFirstChild() == null)
            return "";
        else
            return element.getFirstChild().getNodeValue();
    }

    public String getProperty(String proName)
    {
        Element root = getRoot();
        if (root == null)
        {
        	return "";
        }
        else
        {
        	return root.getAttribute(proName);
        }
    }


    private static Logger logger = Logger.getLogger(XMLUtil.class);
    private Document doc;
    private Element root;

}
