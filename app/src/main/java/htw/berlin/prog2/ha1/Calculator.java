package htw.berlin.prog2.ha1;

/**
 * Eine Klasse, die das Verhalten des Online Taschenrechners imitiert, welcher auf
 * https://www.online-calculator.com/ aufgerufen werden kann (ohne die Memory-Funktionen)
 * und dessen Bildschirm bis zu zehn Ziffern plus einem Dezimaltrennzeichen darstellen kann.
 * Enthält mit Absicht noch diverse Bugs oder unvollständige Funktionen.
 */
package htw.berlin.prog2.ha1;

public class Calculator {

    private String screen = "0";
    private double latestValue;
    private String latestOperation = "";

    public String readScreen() {
        return screen;
    }

    public void pressDigitKey(int digit) {
        if (digit > 9 || digit < 0) throw new IllegalArgumentException();

        if (screen.equals("0") || latestValue == Double.parseDouble(screen)) screen = "";

        screen = screen + digit;
    }

    public void pressClearKey() {
        screen = "0";
        latestOperation = "";
        latestValue = 0.0;
    }

    public void pressBinaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        latestOperation = operation;
        screen = "0";  // Reset screen for the next input
    }

    public void pressUnaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen);
        if (operation.equals("√") && screen.startsWith("-")) {
            screen = "Error"; // Handle negative square root case
        } else {
            double result = switch (operation) {
                case "√" -> Math.sqrt(Double.parseDouble(screen));
                case "%" -> Double.parseDouble(screen) / 100;
                case "1/x" -> 1 / Double.parseDouble(screen);
                default -> throw new IllegalArgumentException();
            };
            screen = formatResult(result);
        }
    }

    public void pressDotKey() {
        if (!screen.contains(".")) screen = screen + ".";
    }

    public void pressNegativeKey() {
        screen = screen.startsWith("-") ? screen.substring(1) : "-" + screen;
    }

    public void pressEqualsKey() {
        try {
            double result = switch (latestOperation) {
                case "+" -> latestValue + Double.parseDouble(screen);
                case "-" -> latestValue - Double.parseDouble(screen);
                case "x" -> latestValue * Double.parseDouble(screen);
                case "/" -> {
                    if (Double.parseDouble(screen) == 0) {
                        yield Double.POSITIVE_INFINITY; // Division by zero case
                    }
                    yield latestValue / Double.parseDouble(screen);
                }
                default -> throw new IllegalArgumentException();
            };
            if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
                screen = "Error"; // Display "Error" on division by zero
            } else {
                screen = formatResult(result);
            }
        } catch (Exception e) {
            screen = "Error"; // Catch any unexpected errors and display "Error"
        }
    }

    // Helper method to format the result to a maximum of 8 decimal places
    private String formatResult(double result) {
        String resultString = String.format("%.8f", result).replaceAll("0+$", "").replaceAll("\\.$", "");
        if (resultString.length() > 10) {
            resultString = resultString.substring(0, 10);
        }
        return resultString;
    }
}

