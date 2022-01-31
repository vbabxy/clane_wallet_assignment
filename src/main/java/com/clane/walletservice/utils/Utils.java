package com.clane.walletservice.utils;

import com.clane.walletservice.exception.BadRequestException;

public final class Utils {


    public static void validatePagRequest(int page, int size) {
        if(page < 1) {
            throw new BadRequestException("page number must be equal or greater than 1");
        }

        if(size < 1) {
            throw new BadRequestException("page size must be equal or greater than 1");
        }
    }

    public static String generateRandomDigits() {
        int m = (int) Math.pow(10, 7);
        int randomNumber = m + new java.util.Random().nextInt(9 * m);
        return "00" + randomNumber;
    }

    public static String  generateReference() {
        return "CLE-"+System.nanoTime();
    }
}
