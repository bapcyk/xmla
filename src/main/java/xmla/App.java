package xmla;


// TODO remove dependency tuples?
//import java.beans.Beans;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.ParseException;
import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import xmla.parser.XmlaAnalyzer;
import xmla.parser.XmlaParser;


enum BlockOpt {
    RAW,
    LINE,
    STRIP,
    COMPACT,
    INDENT,
    COMMENT;

    // Like valueOf() but more flexible
    public static BlockOpt fromString(String s) {
        return switch (s) {
            case "RAW", "raw", "R", "r" ->
                RAW;
            case "LINE", "line", "L", "l" ->
                LINE;
            case "STRIP", "strip", "S", "s" ->
                STRIP;
            case "COMPACT", "compact", "C", "c" ->
                COMPACT;
            case "INDENT", "indent", "I", "i" ->
                INDENT;
            case "COMMENT", "comment", "!" ->
                COMMENT;
            default ->
                RAW;
        };
    }
}


public class App {
    protected void normalizeDoc(Document doc) {
        DOMConfiguration docConfig = doc.getDomConfig();
        docConfig.setParameter("namespaces", Boolean.TRUE);
        docConfig.setParameter("infoset", Boolean.TRUE); //?
        doc.normalizeDocument();
    }

    protected String dumpXml(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        normalizeDoc(doc); // TBH no effect
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new StringWriter()); // new File("C:\\testing.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();
        return xmlString;
    }

    protected Document xmlaToXml(String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            XmlaParser parser = new XmlaParser(
                    new FileReader(new File(filePath)),
                    new MyXmlaAnalyzer(doc));

            parser.prepare();
            parser.parse();
            return doc;
        } catch (ParserConfigurationException e) {
            System.out.format("Cannot configure parser: %s", e.getMessage());
            return null;
        } catch (ParserCreationException e) {
            System.out.format("Cannot create parser: %s", e.getMessage());
            return null;
        } catch (ParserLogException e) {
            System.out.format("Cannot parse: %s", e.getMessage());
            return null;
        } catch (FileNotFoundException e) {
            System.out.format("Cannot find input file: %s", e.getMessage());
            return null;
        }
    }

    protected String xmlToXmla(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sax = factory.newSAXParser();

            MyXmlHandler myHandler = new MyXmlHandler();
            sax.parse(new File(filePath), myHandler);
            String res = myHandler.content();
            return res;
        } catch (ParserConfigurationException e) {
            System.out.format("Cannot configure SAX parser: %s", e.getMessage());            
            return null;
        } catch (SAXException e) {
            System.out.format("Cannot create SAX parser: %s", e.getMessage());
            return null;
        } catch (IOException e) {
            System.out.format("Cannot read file: %s", e.getMessage());
            return null;
        }
    }

    /////////////////////////// MAIN ////////////////////////////
    //      To test run: 1) xmla test.txt 2) xml test.xml
    /////////////////////////////////////////////////////////////
    
    public static void main(String[] args) throws SAXException {
        if (2 != args.length) {
            System.out.println("Command line error: xmla|xml FILE");
            System.exit(1);
        } else {
            try {
                if (args[0].equals("xmla")) {
                    App app;
                    app = new App();
                    Document doc = app.xmlaToXml(args[1]);
                    if (null == doc)
                        System.exit(1);
                    else
                        System.out.println(app.dumpXml(doc));
                } else if (args[0].equals("xml")) {
                    App app;
                    app = new App();
                    String res = app.xmlToXmla(args[1]);
                    if (null == res)
                        System.exit(1);
                    else
                        System.out.println(res);
                }
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }

}


class MyXmlHandler extends DefaultHandler {

    int level;
    StringBuilder out;

    public MyXmlHandler() {
        level = 0;
        out = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        level++;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        level--;
    }

    @Override
    public void characters(char ch[], int start, int length) {
        ;
    }

    @Override
    public void startDocument() {
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
    }

    @Override
    public void endDocument() {
        ;
    }

    public String content() {
        return out.toString();
    }
}


/// Parser
class MyXmlaAnalyzer extends XmlaAnalyzer {

    private final Document doc;
    private static String NL; // system new-line representation

    public MyXmlaAnalyzer(Document doc) {
        this.doc = doc;
        NL = System.getProperty("line.separator");
    }

    protected static String chop(String s) {
        final int len = s.length();
        if (len > 0) {
            return s.substring(0, s.length() - 1);
        } else {
            return s;
        }
    }

    protected static String strip(String s, int ntimes) {
        final int len = s.length();
        if (len >= ntimes * 2) {
            return s.substring(ntimes, len - ntimes);
        } else {
            return "";
        }
    }

    protected static String specAsEntity(String s) {
        final int len = s.length();
        return s.substring(2, len - 1);
    }

    protected static int blankPrefixSize(String s) {
        int i = 0;
        while (i < s.length() && (' ' == s.charAt(i) || '\t' == s.charAt(i))) {
            i++;
        }
        return i;
    }

    protected static String reindent(String s) {
        String[] lines = s.split("(\r|\n)+");
        int minIdent = Arrays.stream(lines).map(MyXmlaAnalyzer::blankPrefixSize).min(Integer::compare).get();
        return Arrays.stream(lines)
                .map(ln -> ln.substring(minIdent))
                .collect(Collectors.joining(NL));
    }

    protected static String formatTextBlock(String s, List<BlockOpt> fmts) {
        if (fmts.isEmpty()) {
            return s;
        } else {
            BlockOpt fmt = fmts.get(0);
            fmts = fmts.subList(1, fmts.size());
            return switch (fmt) {
                case RAW ->
                    formatTextBlock(s, fmts);
                case LINE ->
                    formatTextBlock(s.replaceAll("(\r|\n)+", " "), fmts);
                case STRIP ->
                    formatTextBlock(s.strip(), fmts);
                case COMPACT ->
                    formatTextBlock(s.replaceAll("(\r|\n|\t| )+", " "), fmts);
                case INDENT ->
                    formatTextBlock(reindent(s), fmts);
                default ->
                    formatTextBlock(s, fmts);
            };
        }
    }

    // FIXME SPEC still is not shown like .(lt) -> &lt;
    /////////////////////////////////////////////////////////////////
    //                           Handlers
    /////////////////////////////////////////////////////////////////
    @Override
    protected Node exitAtom(Token node) throws ParseException {
        node.addValue(node.getImage());
        return node;
    }

    @Override
    protected Node exitAttrsOpen(Token node) {
        return node;
    }

    @Override
    protected Node exitAttrsClose(Token node) {
        return node;
    }

    @Override
    protected Node exitTagOpen(Token node) {
        node.addValue("<"); // to detect different branches
        return node;
    }

    @Override
    protected Node exitTagClose(Token node) {
        return node;
    }

    @Override
    protected Node exitSingleTag(Token node) {
        Element el = doc.createElement(chop(node.getImage()));
        node.addValue(el);
        return node;
    }

    @Override
    protected Node exitLineTag(Token node) {
        node.addValue("<-"); // to detect different branches
        return node;
    }

    @Override
    protected Node exitSpec(Token node) {
        String text = node.getImage();
        if (!text.isBlank()) {
            text = text
                    .replaceAll("(?i)\\.\\(lt\\)", "<")
                    .replaceAll("(?i)\\.\\(gt\\)", ">")
                    .replaceAll("(?i)\\.\\(quot\\)", "\"")
                    .replaceAll("(?i)\\.\\(apos\\)", "'")
                    .replaceAll("(?i)\\.\\(amp\\)", "&");
//        EntityReference ent = doc.createEntityReference(specAsEntity(node.getImage()));
            Text t = doc.createTextNode(text);
            node.addValue(t);
        }
        return node;
    }

    @Override
    protected Node exitAttrStr(Token node) {
        node.addValue(strip(node.getImage(), 1));
        return node;
    }

    @Override
    protected Node exitIs(Token node) {
        return node;
    }

    @Override
    protected Node exitBlock(Token node) {
        String[] blocks = strip(node.getImage(), 2).split(" *\\:", 2);
        String text, spec;
        boolean isComment = false;
        switch (blocks.length) {
            case 0 -> {
                spec = "";
                text = "";
            }
            case 1 -> {
                spec = "";
                text = blocks[0];
            }
            default -> {
                spec = blocks[0];
                text = blocks[1];
            }
        }
        text = text.replaceAll("(?i)\\.\\(lt\\)", "<").replaceAll("(?i)\\.\\(gt\\)", ">");
        if (0 < spec.length()) {
            var fmts = Arrays.stream(spec.split(" +")).map(BlockOpt::fromString).toList();
            text = formatTextBlock(text, fmts);
            isComment = fmts.contains(BlockOpt.COMMENT);
        }
        if (isComment) {
            Comment c = doc.createComment(text);
            node.addValue(c);
        } else {
            Text t = doc.createTextNode(text);
            node.addValue(t);
        }
        return node;
    }

    @Override
    protected Node exitGo(Production node) {
        ArrayList values = getChildValues(node);
        doc.appendChild((Element) values.get(0));
        return node;
    }

    @Override
    protected Node exitAttr(Production node) {
        ArrayList values = getChildValues(node);
        if (1 == values.size()) {
            Attr attr = doc.createAttribute((String) values.get(0));
            attr.setValue("");
            node.removeAllValues();
            node.addValue(attr);
        } else if (2 == values.size()) {
            // 2, not 3 for rules "ATOM IS ATOM | ATOM IS ATTR_STR" bcz no a special handling of "IS"
            // just default handling
            Attr attr = doc.createAttribute((String) values.get(0));
            attr.setValue((String) values.get(1));
            node.removeAllValues();
            node.addValue(attr);
        }
        return node;
    }

    @Override
    protected Node exitAttrs(Production node) {
        ArrayList values = getChildValues(node);
        node.removeAllValues();
        for (var val : values) {
            node.addValue(val);
        }
        return node;
    }

    @Override
    protected Node exitTag(Production node) {
        ArrayList values = getChildValues(node);
        final int len = values.size();
        if (0 < len) {
            Element el = doc.createElement((String) values.get(0));
            int i = 1;
            while (i < len) {
                el.setAttributeNode((Attr) values.get(i));
                i++;
            }
            node.addValue(el);
        }
        return node;
    }

    @Override
    protected Node exitNode(Production node) {
        ArrayList values = getChildValues(node);
        final int len = values.size();
        if (1 == len) {
            System.out.println();
            // TODO
        } else if (2 <= len) {
            Element tag = (Element) values.get(0);
            String v1 = (String) values.get(1);
            if (v1.equals("<-")) {
                System.out.println("PPP <-");
            } else if (v1.equals("<")) {
                // tag open
                System.out.println("PPP <");
            }
            if (">" == values.get(len - 1)) {
                values.remove(len - 1);
            }
            values.remove(1);
            // assembly of the subtree
            for (int i = 1; i < values.size(); i++) {
                Object val = values.get(i);
                switch (val) {
                    case Text t ->
                        tag.appendChild(t);
                    case EntityReference er ->
                        tag.appendChild(er);
                    case Comment c ->
                        tag.appendChild(c);
                    default ->
                        tag.appendChild((Element) val);
                }
            }
        }
        node.removeAllValues();
        node.addValue(values.get(0));
        return node;
    }

    @Override
    protected Node exitNodeCont(Production node) {
        ArrayList values = getChildValues(node);
        for (var item : values) {
//                        System.out.println(item);
            if (item instanceof Text) {
                System.out.println(" text!");
            } else if (item instanceof EntityReference) {
                System.out.println(" Ref: " + item.toString());
            } else {
                System.out.println(" NODE!");
            }
        }
        node.addValues(values);
        return node;
    }

}

