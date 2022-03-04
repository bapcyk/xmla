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
                        XmlaParser parser = new XmlaParser(new FileReader(new File(filePath)),
                                new ArithmeticCalculator());
                } catch (ParserCreationException | FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        protected void xmlToXmla() {
        }

        public static void main(String[] args) {
                if (2 != args.length) {
                        System.out.print("Syntax error: xmla|xml FILE");
//                        return 1;
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
//                return 0;
        }

}

class ArithmeticCalculator extends XmlaAnalyzer {

        @Override
        protected Node exitAtom(Token node) throws ParseException {
                node.addValue(new String(node.getImage()));
                return node;
        }
}
