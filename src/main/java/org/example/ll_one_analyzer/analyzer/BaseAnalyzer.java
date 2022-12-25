package org.example.ll_one_analyzer.analyzer;

import java.io.IOException;

public interface BaseAnalyzer<Tokens, ParserTable, Grammar> {

    boolean analyze(final Tokens chars) throws IOException;
    void buildParserTable(final Grammar grammar);
    ParserTable getParserTable();

}
