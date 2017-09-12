package vn.edu.ptit.miniproject.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import vn.edu.ptit.miniproject.model.ItemNews;

/**
 * Created by QuangPC on 7/23/2017.
 */

public class XMLHandler extends DefaultHandler {

    private ArrayList<ItemNews> arrNews = new ArrayList<>();
    private ItemNews item;
    public static final String ITEM = "item";
    public static final String TITLE = "title";
    public static final String DES = "description";
    public static final String LINK = "link";
    public static final String PUB_DATE = "pubDate";

    private StringBuilder builder;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        builder = new StringBuilder();
        if(qName.equals(ITEM)){
            item = new ItemNews();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch,start,length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (item == null){
            return;
        }
        switch (qName){
            case ITEM:
                arrNews.add(item);
                break;
            case TITLE:
                item.setTittle(builder.toString());
                break;
            case DES:
                String f = "Google tin tức";
                if(builder.length()>f.length()) {
                    String s = builder.toString();
                    String i = "<img src=";
                    s = s.substring(s.indexOf(i) + i.length() + 3);
                    String img = "http://" + s.substring(0, s.indexOf("alt=") - 2);
                    String i2 = "</font></b></font><br><font size=";
                    s = s.substring(s.indexOf(i2) + i2.length() + 5);
                    String desc = s.substring(0, s.indexOf("</font>"));
                    desc = desc.replace("<b>", "");
                    desc = desc.replace("</b>", "");
                    desc = desc.replace("&nbsp;", "");
                    item.setImage(img);
                    item.setDes(desc);
                }
                break;
            case LINK:
                String link = builder.substring(builder.indexOf("url=")+4);
                item.setLink(link);
                break;
            case PUB_DATE:
                item.setPubDate(builder.toString());
                break;
        }
    }

    public ArrayList<ItemNews> getArrNews() {
        return arrNews;
    }
}
