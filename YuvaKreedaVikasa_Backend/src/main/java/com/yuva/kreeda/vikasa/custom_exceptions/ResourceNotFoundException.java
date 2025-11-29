package com.yuva.kreeda.vikasa.custom_exceptions;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String mesg) {
    super(mesg);
  }
}
