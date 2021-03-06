package org.rhq.core.template;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine implements Serializable {
    private static final long serialVersionUID = -6490715202402123042L;

    private final Map<String, String> tokens;

    // In English, the open delimiter @@
    // zero or more white space characters
    // The following pattern at least once:
    //    one or more word characters followed by an optional period
    // zero or more white space characters
    // The closing delimiter @@
    // 
    // Thus @@ rhq.system @@ and @@rhq.system@@ are equivalent.
    // @@ rhq.platform.ip_address @@ is valid.
    // @@ & @@ is not a valid.

    private static String tokenRegex = "@@\\s*(\\w+\\.?)+\\s*@@";
    private static String keyRegex = "(\\w+\\.?)+";

    private static Pattern tokenPattern = Pattern.compile(tokenRegex);
    private static Pattern keyPattern = Pattern.compile(keyRegex);

    public TemplateEngine(Map<String, String> tokens) {
        super();
        this.tokens = tokens;
    }

    public Map<String, String> getTokens() {
        return this.tokens;
    }

    public String replaceTokens(String input) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = tokenPattern.matcher(input);
        while (matcher.find()) {
            String next = matcher.group();
            Matcher keyMatcher = keyPattern.matcher(next);
            if (keyMatcher.find()) {
                String key = keyMatcher.group();
                String value = tokens.get(key);
                if (value != null) {
                    next = value;
                }
            }

            // If we didn't find a replacement for the key, leave the original value unchanged
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(next));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
