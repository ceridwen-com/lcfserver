/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.misc.ErrorBuffer;

/**
 *
 * @author Matthew
 */
public abstract class Generator {

    protected static File prepareOutputFile(File baseDirectory, File outputFile) throws IOException {
        if (!outputFile.isAbsolute()) {
            outputFile = new File(baseDirectory, outputFile.getPath());
        }
        if (!outputFile.exists()) {
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                System.out.println(String.format("Unable to fully create the output directory: %s", baseDirectory.getAbsolutePath()));
                return null;
            }
            if (!outputFile.createNewFile()) {
                System.out.println(String.format("Unable to create the output file: %s", outputFile.getAbsolutePath()));
                return null;
            }
        }
        return outputFile;
    }
    

    abstract Map getEntityMap(EntityTypes.Type entity);  

    protected void generateTemplate(String templatedir, String template, String targetdir, String prefix, String suffix, EntityTypes.Type entityType) {
        File templateDirectory = new File(templatedir);
        STGroup group = new STGroupDir(templateDirectory.getAbsolutePath());
        ErrorBuffer errorBuffer = new ErrorBuffer();
        group.setListener(errorBuffer);
        ST st = group.getInstanceOf("Entity" + template);
        Map<String, Object> attributes = getEntityMap(entityType);
        for (String key : attributes.keySet()) {
            st.add(key, attributes.get(key));
        }
        if (null == st || !errorBuffer.errors.isEmpty()) {
            System.out.println(String.format("Unable to execute template. %n%s", errorBuffer.toString()));
            return;
        }
        render(st, targetdir, prefix + entityType.name() + template + suffix);
    }

    public void render(ST st, String targetdir, String targetfile) {
        try {
            File outputFile = prepareOutputFile(new File(targetdir), new File(targetfile));
            FileWriter fileWriter = new FileWriter(outputFile);
            ErrorBuffer listener = new ErrorBuffer();
            st.write(new AutoIndentWriter(fileWriter), listener);
            fileWriter.flush();
            fileWriter.close();
            if (!listener.errors.isEmpty()) {
                System.out.println(listener.toString());
            }
        } catch (IOException e) {
            System.out.println(String.format("Unable to write output file: %s. (%s)", targetfile, e.getMessage()));
        }
    }
}
