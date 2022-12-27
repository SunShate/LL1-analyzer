package org.example.ll_one_analyzer.analyzer;

import org.example.ll_one_analyzer.Pair;
import org.example.ll_one_analyzer.grammar_parser.Grammar;
import org.example.ll_one_analyzer.grammar_parser.Rule;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;

public class Analyzer implements BaseAnalyzer<
        List<String>,
        Map<Pair<String, String>, Rule>,
        Grammar> {

    private Map<Pair<String, String>, Rule> parserTable;

    private Set<String> terminals;
    private Set<String> nonTerminals;
    private String startStatement;
    private static final String EPSILON = "epsilon";

    public Analyzer() {
    }

    @Override
    public void buildParserTable(final Grammar grammar) {
        this.startStatement = grammar.getStartVariable();
        this.parserTable = new HashMap<>();
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();

        this.nonTerminals = grammar.getNonTerminals();
        this.terminals = grammar.getTerminals();

        for (String nonTerminal : nonTerminals) {
            for (String terminal : terminals) {
                parserTable.put(new Pair<>(nonTerminal, terminal), null);
            }
        }

        final Map<String, Set<String>> followSets = grammar.getFollowSets();

        for (final Rule rule : grammar.getRules()) {
            for (final String firstTerminal : grammar.computeFirst(rule.getRightSide(), 0)) {
                parserTable.put(new Pair<>(rule.getLeftSide(), firstTerminal), rule);
                if (firstTerminal.equals(EPSILON)) {
                    for (final String followTerminal : followSets.get(rule.getLeftSide())) {
                        parserTable.put(
                                new Pair<>(rule.getLeftSide(), followTerminal),
                                rule
                        );
                        if (followTerminal.equals("$")) {
                            parserTable.put(
                                    new Pair<>(rule.getLeftSide(), "$"),
                                    rule
                            );
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<Pair<String, String>, Rule> getParserTable() {
        return parserTable;
    }

    @Override
    public boolean analyze(final List<String> tokens) throws IOException {
        int ip = 0;
        final Stack<String> grammarStack = new Stack<>();
        Rule rule;

        grammarStack.addAll(List.of("$", startStatement));

        String X = grammarStack.peek();
        while (!X.equals("$")) {
            String a = tokens.get(ip);

            if (X.equals(a)) {
                grammarStack.pop();
                ip++;
            } else if (X.equals("String") && a.matches("[a-z_][a-z]*(?:[A-Z][a-z0-9]+)*[a-z0-9_]?")) {
                grammarStack.pop();
                ip++;
            } else if (X.equals("Number") && a.matches("\\d+")) {
                grammarStack.pop();
                ip++;
            } else if (a.matches("[a-z_][a-z]*(?:[A-Z][a-z0-9]+)*[a-z0-9_]?") &&
                    (rule = parserTable.get(new Pair<>(X, "String"))) != null) {
                System.out.println(rule);
                grammarStack.pop();
                grammarStack.addAll(Arrays.stream(rule.getRightSide()).collect(Collector.of(
                        ArrayDeque::new,
                        ArrayDeque::addFirst,
                        (d1, d2) -> {
                            d2.addAll(d1);
                            return d2;
                        }))
                );
            } else if (a.matches("\\d+") && (rule = parserTable.get(new Pair<>(X, "Number"))) != null) {
                System.out.println(rule);
                grammarStack.pop();
                grammarStack.addAll(Arrays.stream(rule.getRightSide()).collect(Collector.of(
                        ArrayDeque::new,
                        ArrayDeque::addFirst,
                        (d1, d2) -> {
                            d2.addAll(d1);
                            return d2;
                        }))
                );
            } else if (terminals.contains(X)) {
                System.err.println("There should be an error");
                return false;
            } else if ((rule = parserTable.get(new Pair<>(X, a))) != null) {
                System.out.println(rule);
                grammarStack.pop();
                grammarStack.addAll(Arrays.stream(rule.getRightSide())
                        .filter(element -> !element.equals(EPSILON))
                        .collect(Collector.of(
                                ArrayDeque::new,
                                ArrayDeque::addFirst,
                                (d1, d2) -> {
                                    d2.addAll(d1);
                                    return d2;
                                }))
                );
            } else {
                System.err.println("There should be an error");
                return false;
            }
            X = grammarStack.peek();
        }
        return true;
    }
}
