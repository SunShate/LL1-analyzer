package org.example.ll_one_analyzer.analyzer;

import java.io.IOException;

public interface BaseAnalyzer<T> {

    boolean analyze(final T chars) throws IOException;
}
