package com.arch.ability.router;

// Package level visibility by design.
class Matcher {

    private static final String SEP = ",";
    /**
     *
     * @param sellerOrBusinessUnitNo
     * @param propertyValue  @RegisterExt里对应资源文件配置的值
     * @return
     */
    boolean match(String sellerOrBusinessUnitNo, String propertyValue) {
        if (propertyValue == null) {
            return false;
        }

        String[] propertyValues = propertyValue.split(SEP);
        for (String value : propertyValues) {
            if (value.trim().equals(sellerOrBusinessUnitNo)) {
                return true;
            }
        }

        return false;
    }
}
