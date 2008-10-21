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
