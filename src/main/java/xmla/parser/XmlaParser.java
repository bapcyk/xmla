/*
 * XmlaParser.java
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 *
 * Permission is granted to copy this document verbatim in any
 * medium, provided that this copyright notice is left intact.
 *
 * Copyright (c). All rights reserved.
 */

package xmla.parser;

import java.io.Reader;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ProductionPattern;
import net.percederberg.grammatica.parser.ProductionPatternAlternative;
import net.percederberg.grammatica.parser.RecursiveDescentParser;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A token stream parser.
 *
 * @author   Bapcyk
 * @version  1.0
 */
public class XmlaParser extends RecursiveDescentParser {

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_1 = 3001;

    /**
     * A generated production node identity constant.
     */
    private static final int SUBPRODUCTION_2 = 3002;

    /**
     * Creates a new parser with a default analyzer.
     *
     * @param in             the input stream to read from
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public XmlaParser(Reader in) throws ParserCreationException {
        super(in);
        createPatterns();
    }

    /**
     * Creates a new parser.
     *
     * @param in             the input stream to read from
     * @param analyzer       the analyzer to use while parsing
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    public XmlaParser(Reader in, XmlaAnalyzer analyzer)
        throws ParserCreationException {

        super(in, analyzer);
        createPatterns();
    }

    /**
     * Creates a new tokenizer for this parser. Can be overridden by a
     * subclass to provide a custom implementation.
     *
     * @param in             the input stream to read from
     *
     * @return the tokenizer created
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    protected Tokenizer newTokenizer(Reader in)
        throws ParserCreationException {

        return new XmlaTokenizer(in);
    }

    /**
     * Initializes the parser by creating all the production patterns.
     *
     * @throws ParserCreationException if the parser couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        ProductionPattern             pattern;
        ProductionPatternAlternative  alt;

        pattern = new ProductionPattern(XmlaConstants.GO,
                                        "Go");
        alt = new ProductionPatternAlternative();
        alt.addProduction(XmlaConstants.NODE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(XmlaConstants.ATTR,
                                        "Attr");
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.ATOM, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.ATOM, 1, 1);
        alt.addToken(XmlaConstants.IS, 1, 1);
        alt.addToken(XmlaConstants.ATOM, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.ATOM, 1, 1);
        alt.addToken(XmlaConstants.IS, 1, 1);
        alt.addToken(XmlaConstants.ATTR_STR, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(XmlaConstants.ATTRS,
                                        "Attrs");
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.ATTRS_OPEN, 1, 1);
        alt.addProduction(XmlaConstants.ATTR, 1, -1);
        alt.addToken(XmlaConstants.ATTRS_CLOSE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(XmlaConstants.TAG,
                                        "Tag");
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.ATOM, 1, 1);
        alt.addProduction(XmlaConstants.ATTRS, 0, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(XmlaConstants.NODE,
                                        "Node");
        alt = new ProductionPatternAlternative();
        alt.addProduction(XmlaConstants.TAG, 1, 1);
        alt.addProduction(SUBPRODUCTION_1, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(XmlaConstants.NODE_CONT,
                                        "NodeCont");
        alt = new ProductionPatternAlternative();
        alt.addProduction(SUBPRODUCTION_2, 1, -1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_1,
                                        "Subproduction1");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.DOT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.LINE_TAG, 1, 1);
        alt.addProduction(XmlaConstants.NODE_CONT, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.TAG_OPEN, 1, 1);
        alt.addProduction(XmlaConstants.NODE_CONT, 0, 1);
        alt.addToken(XmlaConstants.TAG_CLOSE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);

        pattern = new ProductionPattern(SUBPRODUCTION_2,
                                        "Subproduction2");
        pattern.setSynthetic(true);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.BLOCK, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addToken(XmlaConstants.SPEC, 1, 1);
        pattern.addAlternative(alt);
        alt = new ProductionPatternAlternative();
        alt.addProduction(XmlaConstants.NODE, 1, 1);
        pattern.addAlternative(alt);
        addPattern(pattern);
    }
}
