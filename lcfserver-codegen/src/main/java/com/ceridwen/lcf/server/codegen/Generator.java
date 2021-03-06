/* 
 * Copyright 2019 Ceridwen Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.EntityType;
import org.stringtemplate.v4.AutoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;
import org.stringtemplate.v4.misc.ErrorBuffer;
import org.stringtemplate.v4.misc.STMessage;

/**
 *
 * @author Ceridwen Limited
 */
public abstract class Generator {

    /**
     *
     * @param baseDirectory
     * @param outputFile
     * @return
     * @throws IOException
     */
    protected static File prepareOutputFile(File baseDirectory, File outputFile) throws IOException {
        if (!outputFile.isAbsolute()) {
            outputFile = new File(baseDirectory, outputFile.getPath());
        }
        if (!outputFile.exists()) {
            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, "Unable to fully create the output directory: {0}", baseDirectory.getAbsolutePath());
                return null;
            }
            if (!outputFile.createNewFile()) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, "Unable to create the output file: {0}", outputFile.getAbsolutePath());
                return null;
            }
        }
        return outputFile;
    }
    

    abstract Map getEntityMap(EntityType entity);  

    /**
     *
     * @param templatedir
     * @param template
     * @param targetdir
     * @param prefix
     * @param suffix
     * @param entityType
     */
    protected void generateTemplate(String templatedir, String template, String targetdir, String prefix, String suffix, EntityType entityType) {
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
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, "Unable to execute template. {0}", errorBuffer.toString());
            return;
        }
        Logger.getLogger(Generator.class.getName()).log(Level.INFO, "Generating {0} to {1}", new String[]{prefix + EntityCodeListClassMapping.getEntityClass(entityType).getSimpleName() + template + suffix, targetdir} );      
        render(st, targetdir, prefix + EntityCodeListClassMapping.getEntityClass(entityType).getSimpleName() + template + suffix);
    }

    
    
    /**
     *
     * @param st
     * @param targetdir
     * @param targetfile
     */
    public void render(ST st, String targetdir, String targetfile) {
        try {
            File outputFile = prepareOutputFile(new File(targetdir), new File(targetfile));
            FileWriter fileWriter = new FileWriter(outputFile);
            ErrorBuffer listener = new ErrorBuffer();
            st.write(new AutoIndentWriter(fileWriter), listener);
            fileWriter.flush();
            fileWriter.close();
            if (!listener.errors.isEmpty()) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, "StringTemplate Rendering error {0}", listener.toString());                
            }
        } catch (IOException e) {
            Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, String.format("Unable to write output file: %s.", targetfile), e);                
        }
    }
}
