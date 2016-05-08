package com.fckey.util.maven.configfilter;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by fckey on 2016/04/09.
 */
public class ConfigDefinition implements Iterable<EnvironmentConfig> {

    private File configDirectory;
    private List<String> envNames;
    private String commonPropsFile;
    private boolean interpolateEnvProperties;
    private String hierachyPropertyName;
    private Log log;

    private List<EnvironmentConfig> configs = new ArrayList<>();

    public ConfigDefinition(File configDirectory, List<String> envNames, String commonPropsFile, boolean interpolateEnvProperties, String hierachyPropertyName, Log log) throws IOException {
        this.configDirectory = configDirectory;
        this.envNames = envNames;
        this.commonPropsFile = commonPropsFile;
        this.interpolateEnvProperties = interpolateEnvProperties;
        this.hierachyPropertyName = hierachyPropertyName;
        this.log = log;
        init();
    }

    private void init() throws IOException {
        Properties commonProps = loadProps(configDirectory, commonPropsFile);
        for (String envName : envNames) {
            log.info("processing: "+envName);
            Properties envProps = loadProps(configDirectory, getPropertiesFileNameFor(envName));

            String hierarchy = envProps.getProperty(hierachyPropertyName);
            Properties baseForEnv = getPropertyHierarchy(hierarchy, commonProps);
            baseForEnv.putAll(envProps);

            configs.add(new EnvironmentConfig(envName, baseForEnv));

        }
    }

    private Properties getPropertyHierarchy(String hierarchy, Properties commonProps) throws IOException {
        Properties hierarchyProps = new Properties(commonProps);
        if (hierarchy != null) {
            for (String item : hierarchy.split(",")) {
                hierarchyProps.putAll(loadProps(configDirectory,getPropertiesFileNameFor(item.trim())));
            }
        }
        return hierarchyProps;
    }

    private static String getPropertiesFileNameFor(String envName) {
        return envName + MergeConfigMojo.PROPERTIES_FILE_SUFFIX;
    }

    private Properties loadProps(File dir, String propsFileName) throws IOException {
        Properties props = new Properties();
        File commonFile = new File(dir, propsFileName);
        if (!commonFile.exists() || !commonFile.isFile()) {
            log.warn(String.format("Config file '%s' not found.", commonFile.getAbsolutePath()));
        } else {
            try(FileInputStream in = new FileInputStream(commonFile)) {
                props.load(in);
            }
        }
        return props;
    }

    public boolean isInterpolateEnvProperties() {
        return interpolateEnvProperties;
    }

    @Override
    public Iterator<EnvironmentConfig> iterator() {
        return configs.iterator();
    }


}
