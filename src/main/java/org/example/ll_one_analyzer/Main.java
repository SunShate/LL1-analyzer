package org.example.ll_one_analyzer;

import org.example.ll_one_analyzer.first.First;
import org.example.ll_one_analyzer.grammar_parser.GrammarParser;
import org.example.ll_one_analyzer.grammar_parser.Rule;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(final String[] args) throws IOException {
        GrammarParser grammarParser = new GrammarParser();

        List<Rule> grammar = grammarParser.parseGrammarFromFile(Paths.get(args[0]));

        Set<String> nonTerminals = grammar.stream().collect(
                Collectors.groupingBy(Rule::getNonTerminal, Collectors.toSet())
        ).keySet();

        Set<String> terminals = grammar.stream()
                .map(Rule::getProduction)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        terminals.removeAll(nonTerminals);

        System.out.println(grammar);
        System.out.printf("\nterminals: %s%n", terminals);
        System.out.printf("\nnonTerminals: %s%n", nonTerminals);


//        Scanner scanner = new Scanner(System.in);
//        BaseAnalyzer<List<String>> analyzer = new Analyzer();
//
//        System.out.println("Write ESCAPE to end");
//
//        String line;
//        while (!Objects.equals(line = scanner.nextLine(), "ESCAPE")) {
//            System.out.printf(
//                    "input string belongs to grammar - %b%n",
//                    analyzer.analyze(List.of(line.split(" ")))
//            );
//        }
    }
}