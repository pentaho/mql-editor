/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.gwt.widgets.client.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 * Please use org.pentaho.gwt.widgets.client.utils.string.StringUtils instead.  
 * We are gradually moving to a more logical breakdown of GWT modules for more succinct reuse.
 */
@Deprecated
public class StringTokenizer {
  ArrayList<String> tokens = new ArrayList<String>();

  @Deprecated
  public StringTokenizer(String text, String delimiters) {
    if (text == null || "".equals(text)) { //$NON-NLS-1$
      return;
    }
    char[] delimiterArray = delimiters.toCharArray();
    List<Character> delimiterList = new ArrayList<Character>();
    for (char delim : delimiterArray) {
      delimiterList.add(delim);
    }
    char[] chars = text.toCharArray();

    int sindex = 0;
    int i;
    for (i = 0; i < chars.length; i++) {
      if (delimiterList.contains(chars[i])) {
        tokens.add(text.substring(sindex, i));
        sindex = i + 1;
      }
    }

    if (sindex < i) {
      tokens.add(text.substring(sindex));
    }
  }

  @Deprecated
  public StringTokenizer(String text, char delimiter) {
    this(text, new String(new char[] { delimiter }));
  }

  @Deprecated
  public int countTokens() {
    return tokens.size();
  }

  @Deprecated
  public String tokenAt(int index) {
    return (String) tokens.get(index);
  }
}
