package org.example.ll_one_analyzer.analyzer;

import org.example.ll_one_analyzer.Pair;
import org.example.ll_one_analyzer.grammar_parser.Grammar;
import org.example.ll_one_analyzer.grammar_parser.Rule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Analyzer implements BaseAnalyzer<List<String>> {

    private Map<Pair<String, String>, Rule> parserTable;
    private static final String EPSILON = "epsilon";
    public Analyzer() {
    }

    public void buildParserTable(final Grammar grammar) {
        this.parserTable = new HashMap<>();

        for (String nonTerminal : grammar.getNonTerminals()) {
            for (String terminal: grammar.getTerminals()) {
                parserTable.put(new Pair<>(nonTerminal, terminal), null);
            }
        }

        final Map<String, Set<String>> firstSets = grammar.getFirstSets(), followSets = grammar.getFollowSets();

        for (final Rule rule : grammar.getRules()) {
            firstSets.get(rule.getLeftSide())
                    .forEach(terminal -> {
                        if (terminal.equals(EPSILON)) {
                            followSets.get(rule.getLeftSide()).forEach(followTerminal -> {
                                if (followTerminal.equals("$")) {
                                    parserTable.put(
                                            new Pair<>(rule.getLeftSide(), "$"),
                                            new Rule(rule.getLeftSide(), List.of(EPSILON))
                                    );
                                } else {
                                    parserTable.put(
                                            new Pair<>(rule.getLeftSide(), followTerminal),
                                            new Rule(rule.getLeftSide(), List.of(EPSILON))
                                    );
                                }
                            });
                        } else {
                            parserTable.put(new Pair<>(rule.getLeftSide(), terminal), rule);

                        }
                    });
        }
    }

    public Map<Pair<String, String>, Rule> getParserTable() {
        return parserTable;
    }

    @Override
    public boolean analyze(final List<String> tokens) throws IOException {
        return false;
    }
}
