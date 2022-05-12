package com.otto15.common.utils;

/**
 * Class for data normalizing.
 * @author Rakhmatullin R.
 */
public final class DataNormalizer {

    private DataNormalizer() {

    }

    /**
     *
     * @param data all arguments in one String line
     * @return String[] array with arguments given in the command line
     */
    public static String[] normalize(String data) {
        if ("".equals(data) || data == null) {
            return new String[0];
        }
        String[] args = SmartSplitter.smartSplit(data.trim());
        for (String str : args
        ) {
            str = str.trim();
        }
        return args;
    }
}
