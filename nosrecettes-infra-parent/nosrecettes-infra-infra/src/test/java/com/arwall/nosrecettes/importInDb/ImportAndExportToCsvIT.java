package com.arwall.nosrecettes.importInDb;

import static com.arwall.nosrecettes.infra.apiutil.ItemApiUtil.getAllItems;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.getAllRecipe;
import static com.arwall.nosrecettes.infra.apiutil.RecipeApiUtil.retrieveRecipe;
import static com.arwall.nosrecettes.importInDb.ExportCsv.exportCsv;
import static com.arwall.nosrecettes.importInDb.ImportCsv.import_recipes;
import static com.arwall.nosrecettes.importInDb.OpenCsvUtil.readLineByLine;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.arwall.nosrecettes.infra.AbstractIntegrationT;
import com.arwall.nosrecettes.rest.model.RestIngredient;
import com.arwall.nosrecettes.rest.model.RestRecipe;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FixMethodOrder
public class ImportAndExportToCsvIT extends AbstractIntegrationT {

    public static final String EXPORTED_FILE = "export.csv";
    public static final String FILE_TO_IMPORT = "importExportTest/nosrecettes.csv";

    protected ImportAndExportToCsvIT() throws IOException {
    }

    @BeforeAll
    void setup() throws IOException {
        clearDataBase();
    }

    @Test
    void A_should_import_cvsFile_then_export() throws Exception {
        import_recipes(FILE_TO_IMPORT);
        exportCsv(EXPORTED_FILE);
    }

    @Test
    void B_then_it_should_be_equal() throws Exception {
        var items = getAllItems();
        var recipes = getAllRecipe();

        var recipesOfTheExport = readLineByLine(Path.of(EXPORTED_FILE));
        for (int i = 1; i < recipesOfTheExport.size(); i++) {
            var recipeFromFirstImportPersistedInDb = ImportRecipeUtil.getRestRecipe(recipesOfTheExport.get(0), recipesOfTheExport.get(i), items);
            var recipeReadFromExportedFile = retrieveRecipe(recipes.stream()
                    .filter(r -> r.getName().equals(recipeFromFirstImportPersistedInDb.getName()))
                    .findFirst().get()
                    .getId());
            assertThatRecipesAreEqualIgnorinIds(recipeReadFromExportedFile, recipeFromFirstImportPersistedInDb);
        }
    }

    private void assertThatRecipesAreEqualIgnorinIds(RestRecipe actualResponseRecipe,
            RestRecipe expectedRecipe) {
        assertThat(actualResponseRecipe.getType()).isEqualTo(expectedRecipe.getType());
        assertThat(actualResponseRecipe.getSource()).isEqualTo(expectedRecipe.getSource());
        assertThat(actualResponseRecipe.getSeason()).isEqualTo(expectedRecipe.getSeason());
        assertThat(actualResponseRecipe.getName()).isEqualTo(expectedRecipe.getName());
        List<RestIngredient> actualIngredients = actualResponseRecipe.getIngredients();
        List<RestIngredient> expectedIngredients = expectedRecipe.getIngredients();

        if (null == actualIngredients) {
            assertThat(expectedIngredients).isNull();
        } else {
            assertThat(actualIngredients).hasSameSizeAs(expectedIngredients);
            for (int index = 0; index < actualIngredients.size(); index++) {
                assertThat(actualIngredients.get(index).getItemId())
                        .isEqualTo(expectedIngredients.get(index).getItemId());
                assertThat(actualIngredients.get(index).getQuantity())
                        .isEqualTo(expectedIngredients.get(index).getQuantity());
            }
        }
    }
}
