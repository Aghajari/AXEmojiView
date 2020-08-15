package com.aghajari.emojiview.sticker;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

  @Override
  public boolean equals(Object o){
    return getData().toString().equals(o.toString());
  }

  @SuppressWarnings("unchecked")
  public static <T extends Serializable> T load(String string) {
    byte[] bytes = Base64.decode(string, 0);
    T object = null;
    try {
      ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
      object = (T) objectInputStream.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return object;
  }

  public static String toString(@SuppressWarnings("rawtypes") Sticker object) {
    Log.d("sticker",object.getClass().getSimpleName());
    String encoded = null;
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(object);
      objectOutputStream.close();
      encoded = new String(Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return encoded;
  }

  public RecentStickerManager getDefaultRecentManager (Context context, String type){
    return new RecentStickerManager(context,type);
  }

  public boolean isInstance(Class<?> o){
   return o.isInstance(getData());
  }

}
