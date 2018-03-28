package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pipong {
    Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");

    @Test
    public void pinPongTest() {
        String input = "3 + 4 * 2 / (1 - 5)^2";
        double expected = 3.5;
        Matcher region = pattern.matcher(input).region(0, 2);
        double calculate = new PinPong().calculate(input);
        assert expected == calculate;
    }

    char openBracket = '(';
    char closeBracket = ')';
    char plus = '+';
    char minus = '-';
    char pow = '^';
    char devide = '/';
    char multiply = '*';

    HashMap<Character, Integer> priorityMap = new HashMap<>();


    public Pipong() {
        priorityMap.put(plus, 1);
        priorityMap.put(minus, 1);
        priorityMap.put(devide, 2);
        priorityMap.put(multiply, 2);
        priorityMap.put(pow, 3);
    }

    class PinPong {
        double calculate(String input) {
            while (!input.isEmpty()) {
                int closeIndex = input.indexOf(closeBracket);
                if (closeIndex != -1) {
                    int openIndex = input.substring(0, closeIndex).lastIndexOf(openBracket);
                    if (openIndex == -1) {
                        throw new IllegalStateException("Error input");
                    } else {
                        String substring = input.substring(openIndex + 1, closeIndex);
                        String substring2 = input.substring(openIndex, closeIndex + 1);
                        double replace = calculate(substring);
                        input = input.replace(substring2, String.valueOf(replace));
                    }
                } else if (input.indexOf(multiply) != -1) {
                    int mult = input.indexOf(multiply);
                    Region firstArgument = findFirstArgument(input, mult);
                    Region secondArgument = findSecondArgument(input, mult);
                    double res = firstArgument.value * secondArgument.value;
                    String replaceRegion = input.substring(firstArgument.startIndex, secondArgument.endIndex);
                    input = input.replace(replaceRegion, String.valueOf(res));
                } else if (input.indexOf(devide) != -1) {
                    int mult = input.indexOf(devide);
                    Region firstArgument = findFirstArgument(input, mult);
                    Region secondArgument = findSecondArgument(input, mult);
                    double res = firstArgument.value / secondArgument.value;
                    String replaceRegion = input.substring(firstArgument.startIndex, secondArgument.endIndex);
                    input = input.replace(replaceRegion, String.valueOf(res));
                } else if (input.indexOf(plus) != -1) {
                    int mult = input.indexOf(plus);
                    Region firstArgument = findFirstArgument(input, mult);
                    Region secondArgument = findSecondArgument(input, mult);
                    double res = firstArgument.value + secondArgument.value;
                    String replaceRegion = input.substring(firstArgument.startIndex, secondArgument.endIndex);
                    input = input.replace(replaceRegion, String.valueOf(res));
                } else if (input.indexOf(minus) != -1) {
                    int mult = input.indexOf(minus);
                    Region firstArgument = findFirstArgument(input, mult);
                    Region secondArgument = findSecondArgument(input, mult);
                    double res = firstArgument.value - secondArgument.value;
                    String replaceRegion = input.substring(firstArgument.startIndex, secondArgument.endIndex);
                    input = input.replace(replaceRegion, String.valueOf(res));
                } else {
                    return Double.parseDouble(input.trim());
                }
            }
            throw new IllegalStateException("Error input");
        }

        Region findFirstArgument(String input, int operatorIndex) {
            LinkedList<Region> regions = new LinkedList<>();

            String substring1 = input.substring(0, operatorIndex);
            Matcher matcher = pattern.matcher(substring1);
            while (matcher.find()) {
                regions.add(new Region(matcher.start(), matcher.end()));
            }
            Region last = regions.getLast();
            String substring = substring1.substring(last.startIndex, last.endIndex);

            double v = Double.parseDouble(substring);
            last.value = v;
            return last;
        }

        Region findSecondArgument(String input, int operatorIndex) {
            LinkedList<Region> regions = new LinkedList<>();

            String substring1 = input.substring(operatorIndex, input.length());
            Matcher matcher = pattern.matcher(substring1);
            while (matcher.find()) {
                regions.add(new Region(matcher.start() + operatorIndex, matcher.end() + operatorIndex));
            }
            Region first = regions.getFirst();
            String substring = substring1.substring(first.startIndex - operatorIndex, first.endIndex - operatorIndex);

            double v = Double.parseDouble(substring);
            first.value = v;
            return first;
        }
    }

    class Region {
        final int startIndex;
        final int endIndex;
        double value;

        Region(int startIndex, int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }
}
