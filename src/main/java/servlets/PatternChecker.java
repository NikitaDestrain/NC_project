package servlets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternChecker {
    private static PatternChecker instance;

    private PatternChecker() {
    }

    public static PatternChecker getInstance() {
        if (instance == null) instance = new PatternChecker();
        return instance;
    }

    public boolean isLikeFilterCorrect(String like) {
        Pattern pattern = Pattern.compile("[%_]*[A-Za-z0-9]+[%_]*");
        Matcher matcher = pattern.matcher(like);

        return matcher.matches();
    }

    public boolean isEqualsFilterCorrect(String equals) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        Matcher matcher = pattern.matcher(equals);

        return matcher.matches();
    }

    public boolean isLatinChars(String s) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+\\s*[A-Za-z0-9]*"); // a342aa
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public boolean isCorrectLogin(String s) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+"); // a342aa
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public boolean isCorrectDescription(String s) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]*\\s*[A-Za-z0-9]*"); // a342aa
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
