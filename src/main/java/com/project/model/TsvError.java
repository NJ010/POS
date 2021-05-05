package com.project.model;

public class TsvError {
    int line;
    String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getLine() {
        return line;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
