/*
 * XmlaTokenizer.java
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
import net.percederberg.grammatica.parser.TokenPattern;
import net.percederberg.grammatica.parser.Tokenizer;

/**
 * A character stream tokenizer.
 *
 * @author   Bapcyk
 * @version  1.0
 */
public class XmlaTokenizer extends Tokenizer {

    /**
     * Creates a new tokenizer for the specified input stream.
     *
     * @param input          the input stream to read
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    public XmlaTokenizer(Reader input) throws ParserCreationException {
        super(input, false);
        createPatterns();
    }

    /**
     * Initializes the tokenizer by creating all the token patterns.
     *
     * @throws ParserCreationException if the tokenizer couldn't be
     *             initialized correctly
     */
    private void createPatterns() throws ParserCreationException {
        TokenPattern  pattern;

        pattern = new TokenPattern(XmlaConstants.WHITESPACE,
                                   "WHITESPACE",
                                   TokenPattern.REGEXP_TYPE,
                                   "[ \\t\\n\\r]");
        pattern.setIgnore();
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.ATOM,
                                   "ATOM",
                                   TokenPattern.REGEXP_TYPE,
                                   "!?[a-zA-Z_][a-zA-Z0-9_:]*");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.ATTRS_OPEN,
                                   "ATTRS_OPEN",
                                   TokenPattern.STRING_TYPE,
                                   "(");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.ATTRS_CLOSE,
                                   "ATTRS_CLOSE",
                                   TokenPattern.STRING_TYPE,
                                   ")");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.TAG_OPEN,
                                   "TAG_OPEN",
                                   TokenPattern.STRING_TYPE,
                                   "<");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.TAG_CLOSE,
                                   "TAG_CLOSE",
                                   TokenPattern.STRING_TYPE,
                                   ">");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.SINGLE_TAG,
                                   "SINGLE_TAG",
                                   TokenPattern.REGEXP_TYPE,
                                   "[a-zA-Z_][a-zA-Z0-9_]*\\.");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.LINE_TAG,
                                   "LINE_TAG",
                                   TokenPattern.STRING_TYPE,
                                   "<-");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.SPEC,
                                   "SPEC",
                                   TokenPattern.REGEXP_TYPE,
                                   "\\.\\([a-zA-Z0-9_#]+\\)");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.ATTR_STR,
                                   "ATTR_STR",
                                   TokenPattern.REGEXP_TYPE,
                                   "\"[^\"]*\"");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.IS,
                                   "IS",
                                   TokenPattern.STRING_TYPE,
                                   "=");
        addPattern(pattern);

        pattern = new TokenPattern(XmlaConstants.BLOCK,
                                   "BLOCK",
                                   TokenPattern.REGEXP_TYPE,
                                   "<<[^>]*>>");
        addPattern(pattern);
    }
}
