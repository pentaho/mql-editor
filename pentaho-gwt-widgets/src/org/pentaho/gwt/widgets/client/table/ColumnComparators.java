package org.pentaho.gwt.widgets.client.table;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Classes for comparing columns when sorting a scroll table.
 */
public interface ColumnComparators {
  public enum ColumnComparatorTypes {
    NUMERIC,
    DATE,
    STRING_CASE,
    STRING_NOCASE
  }
  
  public abstract class BaseColumnComparator implements Comparator<Element> {
    protected boolean ascending = true;
    
    public static BaseColumnComparator getInstance(ColumnComparatorTypes type){
      switch(type){
        case NUMERIC:
          return new NumericColumnComparator();
        case STRING_NOCASE:
          return new StringColumnComparator(false);
        case STRING_CASE:
          return new StringColumnComparator(true);
        case DATE:
          return new DateColumnComparator();
      }
      return null;
    }
    
    public boolean isAscending() {
      return ascending;
    }

    public void setAscending(boolean ascending) {
      this.ascending = ascending;
    }

    public BaseColumnComparator(boolean ascending) {
      this.ascending = ascending;
    }
    
    public BaseColumnComparator(){}

    protected Element getLeftObject(Element o1, Element o2) {
      return ascending ? o1 : o2;
    }

    protected Element getRightObject(Element o1, Element o2) {
      return ascending ? o2 : o1;
    }
  }
  
  public class NumericColumnComparator extends BaseColumnComparator {
    public int compare(Element arg0, Element arg1) {
      Element left = getLeftObject(arg0, arg1);
      Element right = getRightObject(arg0, arg1);
      
      // If left object is null, it is less; unless both are null, then they are equal
      if(left == null){
        if(right == null){
          return 0;
        }
        return -1;
      }
      
      Float vLeft = Float.valueOf(DOM.getInnerText(left));
      Float vRight = Float.valueOf(DOM.getInnerText(right));
      
      if(vLeft < vRight)
        return -1;
      if(vLeft > vRight)
        return 1;
      
      //Assumed equal
      return 0;
    }
  }
  
  public class DateColumnComparator extends BaseColumnComparator{
    @SuppressWarnings("deprecation") //GWT requires new Date(String)
    public int compare(Element arg0, Element arg1) {
      Element left = getLeftObject(arg0, arg1);
      Element right = getRightObject(arg0, arg1);
      
      // If left object is null, it is less; unless both are null, then they are equal
      if(left == null){
        if(right == null){
          return 0;
        }
        return -1;
      }
      
      Date vLeft = null;
      Date vRight = null;
      
      try{
         vLeft = new Date(DOM.getInnerText(left));
      } catch (Exception ex){
        return -1;
      }
      
      try{
        vRight = new Date(DOM.getInnerText(right));
      } catch (Exception ex) {
        return 1;
      }
      
      if(vLeft.before(vRight)){
        return -1;
      }
      if(vLeft.after(vRight)){
        return 1;
      }
      
      //Assumed equal
      return 0;
    }
  }
  
  public class StringColumnComparator extends BaseColumnComparator{
    private boolean caseSensitive = false;
    
    public void setCaseSensitive(boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
    }
    public boolean isCaseSensitive() {
      return caseSensitive;
    }
    
    public StringColumnComparator(){}
    public StringColumnComparator(boolean caseSensitive){
      this.setCaseSensitive(caseSensitive);
    }
    
    public int compare(Element arg0, Element arg1) {
      Element left = getLeftObject(arg0, arg1);
      Element right = getRightObject(arg0, arg1);
      
      // If left object is null, it is less; unless both are null, then they are equal
      if(left == null){
        if(right == null){
          return 0;
        }
        return -1;
      }
      
      String vLeft = DOM.getInnerText(left);
      String vRight = DOM.getInnerText(right);
      
      if(caseSensitive){
        return vLeft.compareTo(vRight);
      } else {
        return vLeft.compareToIgnoreCase(vRight);
      }
    }
  }
}
