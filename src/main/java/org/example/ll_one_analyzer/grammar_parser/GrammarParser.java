package org.example.ll_one_analyzer.grammar_parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class GrammarParser {

    private List<List<String>> formProductions(final List<String> lexemes) {
        List<List<String>> productions = new ArrayList<>();

        int j = 0;
        for (int i = 1; i < lexemes.size() - 1; ++i) {
            String lexeme = lexemes.get(i);
            if (lexeme.equals("||")) {
                productions.add(lexemes.subList(j, i));
                j = i + 1;
            }
        }

        if (j != 0) {
            productions.add(lexemes.subList(j, lexemes.size()));
        } else {
            productions.add(lexemes);
        }
        return productions;
    }

    public List<Rule> parseGrammarFromFile(final Path file) throws IOException {
        final List<Rule> rules = new ArrayList<>();
        final List<String> lines = Files.readAllLines(file);

        lines.forEach(line -> {
            List<String> lexemes = List.of(line.split("\\s+"));

            if (!lexemes.get(0).isEmpty()) {
                lexemes = lexemes.stream().filter(lexeme -> !lexeme.equals("->")).collect(Collectors.toList());

                for (List<String> production : formProductions(lexemes.subList(1, lexemes.size()))) {
                    rules.add(new Rule(lexemes.get(0), production));
                }
            }
        });

        return rules;
    }
}
