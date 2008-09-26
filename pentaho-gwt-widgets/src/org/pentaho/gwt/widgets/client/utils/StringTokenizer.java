package org.pentaho.gwt.widgets.client.utils;

import java.util.ArrayList;

public class StringTokenizer {
  ArrayList<String> tokens = new ArrayList<String>();

  public StringTokenizer(String text, char delimiter) {
    char[] chars = text.toCharArray();

    int sindex = 0;
    int i;
    for (i = 0; i < chars.length; i++) {
      if (chars[i] == delimiter) {
        tokens.add(text.substring(sindex, i));
        sindex = i + 1;
      }
    }

    if (sindex < i) {
      tokens.add(text.substring(sindex));
    }
  }

  public int countTokens() {
    return tokens.size();
  }

  public String tokenAt(int index) {
    return (String) tokens.get(index);
  }
}
