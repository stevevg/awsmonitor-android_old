package com.example.awsmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class GsnParser {
    // We don't use namespaces
    private static final String ns = null;
   
    public ArrayList<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    
    private ArrayList<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, ns, "result");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            String name_attr = "";
            try{
            	if(parser.getAttributeName(0).equals("name"))
            		name_attr = parser.getAttributeValue(0);
            } catch (IndexOutOfBoundsException e){
            	
            }
            // Starts by looking for the tuple tag
            if (name.equals("tuple")) {
                entries.add(readEntry(parser));
            }
            else if (name.equals("data") || name.equals("stream-element")){
            	parser.next();
            }
            else if (name.equals("field") && name_attr.equals("max(timed)")){
            	String field = readField(parser);
            	Entry entry = new Entry(field);
            	entries.add(entry);
            }
            else {
                skip(parser);
            }
        }  
        return entries;
    }
    
    public static class Entry {
        public final String field;

        private Entry(String field) {
            this.field = field;
        }
    }
      
    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "tuple");
        String field = null;
        int i = 1;
        //Log.e("MainActivity", String.valueOf(i));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("field") && i == 1) {
                field = readField(parser);
                i++;
            } else {
                skip(parser);
            }
        }
        return new Entry(field);
    }

    // Processes field tags in the feed.
    private String readField(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "field");
        String field = readText(parser);
//Log.i("TAG", field.toString());
        parser.require(XmlPullParser.END_TAG, ns, "field");
        return field;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "0";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                depth--;
                break;
            case XmlPullParser.START_TAG:
                depth++;
                break;
            }
        }
     }
}
