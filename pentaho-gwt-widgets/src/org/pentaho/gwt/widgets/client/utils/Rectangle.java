package org.pentaho.gwt.widgets.client.utils;

import com.google.gwt.user.client.Window;

public class Rectangle{
  int x, y, width, height;
  
  public Rectangle(int x, int y, int width, int height){
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public boolean intersects(Rectangle r){
    return (pointWithin(new Point(r.x, r.y)) 
        || pointWithin(new Point(r.x+r.width, r.y))
        || pointWithin(new Point(r.x, r.y+r.height))
        || pointWithin(new Point(r.x+r.width, r.y+r.height))
    ) || (r.pointWithin(new Point(this.x, this.y)) 
        || r.pointWithin(new Point(this.x+this.width, this.y))
        || r.pointWithin(new Point(this.x, this.y+this.height))
        || r.pointWithin(new Point(this.x+this.width, this.y+this.height))
    );
    
  }
  
  public boolean pointWithin(Point p){
    if((p.x > this.x && p.x < this.x+this.width) && (p.y > this.y && p.y < this.y + this.height)){
      return true;
    }
    return false; 
  }
  
  private class Point{
    int x,y;
    public Point(int x, int y){
      this.x = x;
      this.y = y;
    }
  }
}