package com.selina.lending.testutil;

import java.util.Random;

public class ProductHelper {
    public static final String[] PRODUCT_CODES = {"Fix0005", "Fix0006", "Fix0007", "Fix011", "Fix013", "Var0005",
            "Var0006", "Var0007", "Var0008", "Var0009", "Fix008", "Fix009", "Fix010", "Fix012", "Fix014"};

    public static String getRandomProductCode() {
        int random = new Random().nextInt(PRODUCT_CODES.length);
        return PRODUCT_CODES[random];
    }
}
