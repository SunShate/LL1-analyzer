# OmSTU intelligent systems lab 2
- LL(1) analyzer

Program computes FIRST and FOLLOW sets for given grammar, after that Analyzer makes parser table.

And then implements an algorithm with token buffer and terminal/nonTerminal stack based on the table.


### To test if grammar.txt is LL(1) parsable please enter content of test2 here http://mdaines.github.io/grammophone

## Dependencies

- java 11
- maven

## Build

```shell
mvn package
```

## Run

Program take an argument as path to file with LL(1) grammar:

```shell
java -jar ./target/ll-one-analyzer-1.0-SNAPSHOT.jar 'src/main/resources/<grammar-file>'
```

Parser will parse grammar with next syntax:
```text
<Program> ->  <Expr>

<Expr> -> <A> <E2>
<E2> -> <L_operations> <A> <E2> || epsilon
<L_operations> -> || || &&

<A> -> <T> <A2>
<A2> -> <A_operations_one> <T> <A2> || epsilon
<A_operations_one> -> + || -

<T> -> <F> <T2>
<T2> -> <A_operations_two> <F> <T2> || epsilon
<A_operations_two> -> * || /

<F> -> ( <Expr> ) || <Value>
```
* if || appears with at least 3 lexemes it means that it have at least 2 statements
* epsilon is the empty string
* each terminal/nonTerminal must be separated by space or any space character