package com.aghajari.axemojiview.sticker;

import java.io.Serializable;

public class Sticker<T> implements Serializable {
  private static final long serialVersionUID = 3L;

  T data;

  public Sticker(T data){
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public String toString(){
    return data.toString();
  }
}
