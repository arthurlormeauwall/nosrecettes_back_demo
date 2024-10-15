package com.arwall.nosrecettes.infra.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    public final static String FRAISE_FILENAME = "items/fraise.json";
    public final static String PRUNE_FILENAME = "items/prune.json";

    public final static String TOMATE_FILENAME = "items/tomate.json";
    public final static String RIZ_FILENAME = "items/riz.json";
    public final static String LAIT_FILENAME = "items/lait.json";
    public final static String FARINE_FILENAME = "items/farine.json";
    public final static String TARTE_AUX_FRAISES_FILENAME = "recipes/tarteauxfraisesrecipe.json";
    public final static String TARTE_AUX_PRUNES_FILENAME = "recipes/tarteauxprunesrecipe.json";
    public final static String RIZ_SAUTE_FILENAME = "recipes/rizsauterecipe.json";
    public final static String PIZZA_FILENAME = "recipes/pizzarecipe.json";
    public static final String RECIPE_SUMMARY_FILENAME = "recipes/recipesummary.json";

    public static String getFileContent(String fileName) throws IOException {
        return Files.readString(Path.of(HttpUtils.class.getClassLoader().getResource(
                fileName).getPath()), Charset.defaultCharset());
    }
}
