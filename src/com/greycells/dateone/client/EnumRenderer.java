package com.greycells.dateone.client;

import com.google.gwt.text.shared.AbstractRenderer;

/**
 * Translates enum entries. Use setEmptyValue() if you want to have a custom empty value. Default empty value is "".
 * 
 * @param <T>
 *            an enumeration entry which is to be registered in {@link Translations}
 */

public class EnumRenderer<T extends Enum<?>> extends AbstractRenderer<T> {

  private String emptyValue = "";

  @Override
  public String render(T object) {
      if (object == null)
          return emptyValue;
      return object.toString();
  }

  public void setEmptyValue(String emptyValue) {
      this.emptyValue = emptyValue;
  }

}