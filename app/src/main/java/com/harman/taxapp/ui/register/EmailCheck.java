package com.harman.taxapp.ui.register;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailCheck {
    private boolean check;

    public EmailCheck(String email) {
        Pattern p = Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
        Matcher matcher = p.matcher(email);
        check = matcher.matches();

        Pattern oneCharStart = Pattern.compile("^\\w{1}@");
        Matcher matcherOneChar = oneCharStart.matcher(email);
        if (matcherOneChar.find()) {
            check = false;
        }

        Pattern oneChar = Pattern.compile("@\\w{1}\\.\\w{2,4}$");
        Matcher matcherOne = oneChar.matcher(email);
        if (matcherOneChar.find()) {
            check = false;
        }
    }

    public boolean check() {
        return check;
    }

}
