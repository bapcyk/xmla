package xmla;

import java.io.*;
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
        
        protected static String tokenAsString(Token node) {
                return new String(node.getImage());
        }
        
        @Override
        protected Node exitAtom(Token node) throws ParseException {
                node.addValue(tokenAsString(node));
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
                return node;
        }

        @Override
        protected Node exitTagClose(Token node) {
                return node;
        }

        @Override
        protected Node exitSingleTag(Token node) {
                return node;
        }

        @Override
        protected Node exitLineTag(Token node) {
                return node;
        }

        @Override
        protected Node exitSpec(Token node) {
                return node;
        }

        @Override
        protected Node exitAttrStr(Token node) {
                return node;
        }

        @Override
        protected Node exitQuote(Token node) {
                return node;
        }

        @Override
        protected Node exitIs(Token node) {
                return node;
        }

        @Override
        protected Node exitBlock(Token node) {
                return node;
        }

        @Override
        protected Node exitGo(Production node) {
                return node;
        }

        @Override // TODO ??
        protected void childGo(Production node, Node child) {
        }

        @Override
        protected Node exitAttr(Production node) {
                return node;
        }

        @Override
        protected void childAttr(Production node, Node child) {
        }

        @Override
        protected Node exitAttrs(Production node) {
                return node;
        }

        @Override
        protected Node exitTag(Production node) {
                return node;
        }

        @Override
        protected void childTag(Production node, Node child) {
        }

        @Override
        protected Node exitNode(Production node) {
                return node;
        }

        @Override
        protected void childNode(Production node, Node child) {
        }

        @Override
        protected Node exitNodeCont(Production node) {
                return node;
        }

        @Override
        protected void childNodeCont(Production node, Node child) {
        }

}
