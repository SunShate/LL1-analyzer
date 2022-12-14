package org.example.ll_one_analyzer;

import org.example.ll_one_analyzer.analyzer.Analyzer;
import org.example.ll_one_analyzer.grammar_parser.Grammar;
import org.example.ll_one_analyzer.grammar_parser.GrammarParser;
import org.example.ll_one_analyzer.grammar_parser.Rule;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(final String[] args) throws IOException {
        GrammarParser grammarParser = new GrammarParser();

        List<Rule> rules = grammarParser.parseGrammarFromFile(Paths.get(args[0]));

        Grammar grammar = new Grammar();
        grammar.setRules(rules);

        System.out.println(grammar.getRules());
        System.out.printf("\nterminals: %s%n", grammar.getTerminals());
        System.out.printf("\nnonTerminals: %s%n", grammar.getNonTerminals());

        System.out.printf("\nfirstSets: %s%n", grammar.getFirstSets());
        System.out.printf("\nfollowSets: %s%n", grammar.getFollowSets());

        Analyzer analyzer = new Analyzer();

        analyzer.buildParserTable(grammar);
        analyzer.getParserTable().forEach((pair, rule) -> {
            if (rule != null) {
                System.out.printf("%s, %s -> %s%n", pair.getFirst(), pair.getSecond(), rule);
            }
        });

        Scanner scanner = new Scanner(System.in);
//        BaseAnalyzer<List<String>> analyzer = new Analyzer();

        System.out.println("Write ESCAPE to end");

        String line;
        while (!Objects.equals(line = scanner.nextLine(), "ESCAPE")) {
            List<String> tokens = new ArrayList<>();
            tokens.addAll(List.of(line.split("\\s+")));
            tokens.add("$");
            System.out.println(tokens);
            if (analyzer.analyze(tokens)) {
                System.out.println("Input lexeme belongs to grammar");
            } else {
                System.out.println("Input lexeme doesn't belongs to grammar");
            }
        }
    }
}