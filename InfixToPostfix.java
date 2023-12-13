import java.util.Scanner;

class InfixToPostfix {
    static final int MAX = 100;
    static char[] stack = new char[MAX];
    static char[] infix = new char[MAX];
    static char[] postfix = new char[MAX];
    static int top = -1;

    static void push(char val) {
        if (top == MAX - 1) {
            System.out.println("Stack Overflow");
            System.exit(1);
        }
        stack[++top] = val;
    }


    static char pop() {
        if (top == -1) {
            System.out.println("Stack Underflow");
            System.exit(1);
        }
        return stack[top--];
    }
    static boolean isEmpty() {
        return top == -1;
    }

    static void inToPost() {
        int i, j = 0;
        char next;
        char symbol;

        for (i = 0; i < infix.length; i++) {
            symbol = infix[i];

            if (!space(symbol)) {
                switch (symbol) {
                    case '(':
                        push(symbol);
                        break;
                    case ')':
                        while ((next = pop()) != '(')
                            postfix[j++] = next;
                        break;
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                    case '^':
                        while (!isEmpty() && precedence(stack[top]) >= precedence(symbol))
                            postfix[j++] = pop();
                        push(symbol);
                        break;
                    default:
                        postfix[j++] = symbol;
                }
            }
        }
        while (!isEmpty())
            postfix[j++] = pop();
        postfix[j] = '\0';
    }

    static boolean space(char c) {
        return c == ' ' || c == '\t';
    }

    static int precedence(char symbol) {
        switch (symbol) {
            case '^':
                return 3;
            case '/':
            case '*':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return 0;
        }
    }

    static void print() {
        int i = 0;
        System.out.print("Infix: ");
        for (char c : infix) {
            if (c == '\0') {
                break;
            }
            System.out.print(c);
        }
        System.out.println();
        System.out.print("Postfix: ");
        for (char c : postfix) {
            if (c == '\0') {
                break;
            }
            System.out.print(c);
        }
        System.out.println("\n\nStep by Step:");

        int stepCount = 1;
        for (i = 0; postfix[i] != '\0'; i++) {
            if (!Character.isDigit(postfix[i])) {
                StringBuilder step = new StringBuilder();
                for (int k = 0; k <= i; k++) {
                    step.append(postfix[k]);
                }
                System.out.println("Step " + stepCount + ": " + step);
                stepCount++;
            }
        }
    }

    static void evaluatePostfix() {
        int result;
        for (int i = 0; i < postfix.length && postfix[i] != '\0'; i++) {
            if (Character.isDigit(postfix[i])) {
                push(postfix[i]);
            } else {
                int a = pop() - '0';
                int b = pop() - '0';

                switch (postfix[i]) {
                    case '+':
                        push((char) (b + a + '0'));
                        break;
                    case '-':
                        push((char) (b - a + '0'));
                        break;
                    case '*':
                        push((char) (b * a + '0'));
                        break;
                    case '/':
                        push((char) (b / a + '0'));
                        break;
                    case '^':
                        push((char) (Math.pow(b, a) + '0'));
                        break;
                }
            }
        }
        result = pop() - '0';
        System.out.println("Result: " + result);
    }

    static void inputExpression() {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;

        while (!isValid) {
            System.out.print("Enter infix expression: ");
            String input = scanner.nextLine().trim();

            if (isValidInput(input)) {
                infix = input.toCharArray();
                isValid = true;
            } else {
                System.out.println("Invalid input. Please enter a valid infix expression.");
            }
        }
    }

    static boolean isValidInput(String input) {
        String infixPattern = "^[\\d()]+([\\+\\-*/^][\\d()]+)+$";
        return input.replaceAll("\\s", "").matches(infixPattern);
    }

    public static void main(String[] args) {
        inputExpression();
        inToPost();
        print();
        evaluatePostfix();
    }
}
