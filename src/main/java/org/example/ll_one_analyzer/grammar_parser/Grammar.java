package org.example.ll_one_analyzer.grammar_parser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grammar {
    private List<Rule> rules;
    private Set<String> terminals;
    private Set<String> nonTerminals;
    private String startVariable;
    private Map<String, Set<String>> firstSets;
    private Map<String, Set<String>> followSets;

    private static final String EPSILON = "epsilon";

    public void setRules(final List<Rule> rules) {
        Set<String> nonTerminals = rules.stream().collect(
                Collectors.groupingBy(Rule::getLeftSide, Collectors.toSet())
        ).keySet();

        Set<String> terminals = rules.stream()
                .map(Rule::getRightSide)
                .flatMap(Stream::of)
                .collect(Collectors.toSet());

        terminals.removeAll(nonTerminals);

        this.startVariable = rules.get(0).getLeftSide();
        this.rules = rules;
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;

        computeFirstSets();
        computeFollowSet();
    }

    public List<Rule> getRules() {
        return rules;
    }

    public int findRuleIndex(Rule rule) {
        for (int i = 0; i < rules.size() - 1; i++) {
            if (rules.get(i).equals(rule)) {
                return i;
            }
        }
        return -1;
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public String getStartVariable() {
        return startVariable;
    }

    private void computeFirstSets() {
        firstSets = new HashMap<>();
        for (String s : nonTerminals) {

            Set<String> temp = new LinkedHashSet<>();
            firstSets.put(s, temp);
        }
        while (true) {
            boolean isChanged = false;
            for (String variable : nonTerminals) {
                Set<String> firstSet = new LinkedHashSet<>();
                for (Rule rule : rules) {

                    if (rule.getLeftSide().equals(variable)) {
                        Set<String> addAll = computeFirst(rule.getRightSide(), 0);
                        firstSet.addAll(addAll);
                    }
                }
                if (!firstSets.get(variable).containsAll(firstSet)) {
                    isChanged = true;
                    firstSets.get(variable).addAll(firstSet);
                }

            }
            if (!isChanged) {
                break;
            }
        }

        // firstSets.put("S'", firstSets.get(startVariable));
    }

    private void computeFollowSet() {
        followSets = new HashMap<>();
        for (String s : nonTerminals) {
            LinkedHashSet<String> temp = new LinkedHashSet<>();
            followSets.put(s, temp);
        }
        Set<String> start = new LinkedHashSet<>();
        start.add("$");
        followSets.put(rules.get(0).getLeftSide(), start);

        while (true) {
            boolean isChange = false;
            for (String nonTerminal : nonTerminals) {
                for (Rule rule : rules) {
                    for (int i = 0; i < rule.getRightSide().length; i++) {
                        if (rule.getRightSide()[i].equals(nonTerminal)) {
                            if (i == rule.getRightSide().length - 1) {
                                followSets.get(nonTerminal).addAll(followSets.get(rule.getLeftSide()));
                            } else {
                                Set<String> first = computeFirst(rule.getRightSide(), i + 1);
                                if (first.contains(EPSILON)) {
                                    first.remove(EPSILON);
                                    first.addAll(followSets.get(rule.getLeftSide()));
                                }
                                if (!followSets.get(nonTerminal).containsAll(first)) {
                                    isChange = true;
                                    followSets.get(nonTerminal).addAll(first);
                                }
                            }
                        }
                    }
                }
            }
            if (!isChange) {
                break;
            }
        }
    }

    public Set<String> computeFirst(String[] string, int index) {
        Set<String> first = new LinkedHashSet<>();
        if (index == string.length) {
            return first;
        }
        if (terminals.contains(string[index])) {
            first.add(string[index]);
            return first;
        }

        if (nonTerminals.contains(string[index])) {
            first.addAll(firstSets.get(string[index]));
        }

        if (first.contains(EPSILON)) {
            if (index != string.length - 1) {
                first.addAll(computeFirst(string, index + 1));
                first.remove(EPSILON);
            }
        }
        return first;
    }

    public Set<Rule> getRuledByLeftVariable(String variable) {
        Set<Rule> variableRules = new LinkedHashSet<>();
        for (Rule rule : rules) {
            if (rule.getLeftSide().equals(variable)) {
                variableRules.add(rule);
            }
        }
        return variableRules;
    }

    public boolean isNonTerminal(String s) {
        return nonTerminals.contains(s);
    }

    public Map<String, Set<String>> getFirstSets() {
        return firstSets;
    }

    public Map<String, Set<String>> getFollowSets() {
        return followSets;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.rules);
        hash = 37 * hash + Objects.hashCode(this.terminals);
        hash = 37 * hash + Objects.hashCode(this.nonTerminals);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Grammar other = (Grammar) obj;
        if (!Objects.equals(this.rules, other.rules)) {
            return false;
        }
        if (!Objects.equals(this.terminals, other.terminals)) {
            return false;
        }
        return Objects.equals(this.nonTerminals, other.nonTerminals);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Rule rule : rules) {
            str.append(rule).append("\n");
        }
        return str.toString();
    }
}
