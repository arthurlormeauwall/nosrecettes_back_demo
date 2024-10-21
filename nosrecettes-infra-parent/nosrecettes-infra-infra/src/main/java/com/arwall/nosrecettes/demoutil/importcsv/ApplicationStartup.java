package com.arwall.nosrecettes.demoutil.importcsv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup
        implements ApplicationListener<ApplicationReadyEvent> {

    ImportCsv importCsv;

    @Autowired
    ApplicationStartup(ImportCsv importCsv) {
        this.importCsv = importCsv;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            System.out.println("Application has just started in demo mode ; will import some recipe now");
            importCsv.import_recipes();
            System.out.println("Recipes are imported ; enjoy :) ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}