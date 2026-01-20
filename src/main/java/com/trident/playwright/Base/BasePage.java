package com.trident.playwright.Base;

public class BasePage {

    public void syncUntil(long timeUnit) throws InterruptedException {
        Thread.sleep(timeUnit);
    }
}