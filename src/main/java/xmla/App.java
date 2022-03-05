package xmla;


// TODO remove dependency tuples?
import java.io.*;
import java.util.ArrayList;
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
import xmla.parser.XmlaAnalyzer;
import xmla.parser.XmlaParser;


public class App {

        private final DocumentBuilder docBuilder;
        private final Document doc;

        App() throws ParserConfigurationException {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
        }

        protected String dumpXml() throws TransformerException {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new StringWriter()); // new File("C:\\testing.xml"));
                transformer.transform(source, result);
                String xmlString = result.getWriter().toString();
                return xmlString;
        }

        protected void xmlaToXml(String filePath) {
                try {
                        XmlaParser parser = new XmlaParser(
                                new FileReader(new File(filePath)),
                                new MyXmlaAnalyzer(doc));
                        parser.prepare();
                        parser.parse();
                } catch (ParserCreationException e) {
                        System.out.format("Cannot create parser: %s", e.getMessage());
                        System.exit(1);
                } catch (ParserLogException e) {
                        System.out.format("Cannot parse: %s", e.getMessage());
                        System.exit(1);
                } catch (FileNotFoundException e) {
                        System.out.format("Cannot find input file: %s", e.getMessage());
                        System.exit(1);
                }
        }

        protected void xmlToXmla() {
        }

        public static void main(String[] args) {
                if (2 != args.length) {
                        System.out.print("Command line error: xmla|xml FILE");
                        System.exit(1);
                } else {
                        try {
                                if (args[0].equals("xmla")) {
                                        App app;
                                        app = new App();
                                        app.xmlaToXml(args[1]);
                                        System.out.print(app.dumpXml());
                                } else if (args[0].equals("xml")) {
                                        System.out.print("AS XML");
                                }
                        } catch (ParserConfigurationException | TransformerException e) {
                                e.printStackTrace();
                        }
                }
        }

}


class MyXmlaAnalyzer extends XmlaAnalyzer {

        private final Document doc;

        public MyXmlaAnalyzer(Document doc) {
                this.doc = doc;
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
                return "&" + s.substring(2, len - 2) + ";";
        }

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
                EntityReference ent = doc.createEntityReference(specAsEntity(node.getImage()));
                node.addValue(ent);
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
                // TODO handle indentation rules
                Text t = doc.createTextNode(strip(node.getImage(), 2));
                node.addValue(t);
                return node;
        }

        @Override
        protected Node exitGo(Production node) {
                return node;
        }

        @Override
        protected Node exitAttr(Production node) {
                ArrayList values = getChildValues(node);
                if (1 == values.size()) {
                        Attr attr = doc.createAttribute((String) values.get(0));
                        attr.setValue("");
                        node.addValue(attr);
                } else if (3 == values.size()) {
                        Attr attr = doc.createAttribute((String) values.get(0));
                        attr.setValue((String) values.get(2));
                        node.addValue(attr);
                }
                return node;
        }

        @Override
        protected Node exitAttrs(Production node) {
                ArrayList values = getChildValues(node);
                final int len = values.size();
                if (2 < len) {
                        ArrayList values1 = (ArrayList) values.subList(1, len - 2);
//                        node.removeAllValues();
                        node.addValues(values1);
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
                } else if (2 < len) {
                        String v1 = (String) values.get(1);
                        if (v1.equals("<-")) {
                        // line tag
//                                Element tag = (Element) values.get(0);
//                                tag.appendChild(null);
                        } else if (v1.equals("<")) {
                        // tag open
                        }
                }
                return node;
        }

        @Override
        protected Node exitNodeCont(Production node) {
                node.addValues(getChildValues(node));
                return node;
        }

}

