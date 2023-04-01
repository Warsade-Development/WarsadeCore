package com.warsade.core.command.requirements;

public class RequirementValidatorResponse <T> {

    T result = null;
    String errorMessage = null;

    public boolean isSucceeded() {
        return result != null && errorMessage == null;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}