package org.example.ll_one_analyzer.grammar_parser;

import java.util.List;
import java.util.Objects;

public class Rule {
    private final String nonTerminal;
    private final List<String> production;

    public Rule(final String nonTerminal, final List<String> production) {
        this.nonTerminal = nonTerminal;
        this.production = production;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public List<String> getProduction() {
        return production;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(nonTerminal, rule.nonTerminal) && Objects.equals(production, rule.production);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nonTerminal, production);
    }
}
