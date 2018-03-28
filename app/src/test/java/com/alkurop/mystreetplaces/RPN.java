package com.alkurop.mystreetplaces;

import org.junit.Test;

import java.util.HashMap;
import java.util.Stack;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RPN {

    char openBracket = '(';
    char closeBracket = ')';
    char plus = '+';
    char minus = '-';
    char pow = '^';
    char devide = '/';
    char multiply = '*';
    char space = ' ';

    HashMap<Character, Integer> priorityMap = new HashMap<>();


    public RPN() {
        priorityMap.put(plus, 1);
        priorityMap.put(minus, 1);
        priorityMap.put(devide, 2);
        priorityMap.put(multiply, 2);
        priorityMap.put(pow, 3);
        priorityMap.put(openBracket, 0);
    }

    @Test
    public void convertToRPN() {
        RPNConvertor convertor = new RPNConvertor();
        String input = "3 + 4 * 2 / (1 - 5)^2";
        String expected = "3 4 2 * 1 5 - 2 ^ / +";
        String output = convertor.convert(input);
        System.out.println(input);
        assertThat(output).isEqualToIgnoringCase(expected);
    }

    class RPNConvertor {
        String convert(String input) {
            StringBuilder sb = new StringBuilder();
            char[] chars = input.toCharArray();
            Stack<Character> operatorStack = new Stack<>();
            StringBuilder numberBuilder = new StringBuilder();

            for (char c : chars) {
                if (Character.isDigit(c)) {
                    sb.append(c).append(space);
                } else if (c == openBracket) {
                    operatorStack.push(c);
                } else if (c == closeBracket) {
                    while (operatorStack.peek() != openBracket) {
                        sb.append(operatorStack.pop()).append(space);
                    }
                    operatorStack.pop();
                } else if (priorityMap.containsKey(c)) {
                    while (!operatorStack.isEmpty() && priorityMap.get(c) <= priorityMap.get(operatorStack.peek())) {
                        sb.append(operatorStack.pop()).append(space);
                    }
                    operatorStack.push(c);
                } else if (c != space) {
                    wtf(c);
                }
            }

            while (!operatorStack.isEmpty()) {
                sb.append(operatorStack.pop()).append(space);
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }


    }

    private void wtf(char c) {
        throw new IllegalStateException("Error input" + c);
    }

    @Test
    public void countRPN() {
        int result = new RPNCounter().count("3 4 2 * 1 5 - 2 ^ / +");
        System.out.println(result);
    }

    class RPNCounter {
        int count(String expression) {
            Stack<Integer> numberStack = new Stack<>();
            String[] nums = expression.split(new String(new char[] { space }));
            for (String num : nums) {
                System.out.println(numberStack);
                if (isNumeric(num)) {
                    numberStack.push(Integer.parseInt(num));
                } else {
                    int second = numberStack.pop();
                    int first = numberStack.pop();

                    if (num.equalsIgnoreCase(new String(new char[] { plus }))) numberStack.push(first + second);
                    if (num.equalsIgnoreCase(new String(new char[] { minus }))) numberStack.push(first - second);
                    if (num.equalsIgnoreCase(new String(new char[] { devide }))) numberStack.push(first / second);
                    if (num.equalsIgnoreCase(new String(new char[] { multiply }))) numberStack.push(first * second);
                    if (num.equalsIgnoreCase(new String(new char[] { pow }))) numberStack.push(((int) Math.pow(first, second)));
                }
            }
            return numberStack.pop();
        }

        public boolean isNumeric(String str) {
            return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
        }


    }
}
