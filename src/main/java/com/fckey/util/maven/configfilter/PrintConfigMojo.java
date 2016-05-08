package com.fckey.util.maven.configfilter;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by fckey on 2016/04/09.
 *
 *  Takes a template directory and a directory containing environment property files,
 * and meges the data into a set of merged config directories per environment.
 *
 * @goal print-config
 */
public class PrintConfigMojo extends ConfigFileMojo {

    /**
     * The name of env to pring config for
     *
     * @parameter expression="${configFilter.environment}"
     * @required
     */
    private String environmentName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("env:"+ environmentName);
        validateInput();
        try {
            ConfigDefinition configDef = new ConfigDefinition(configDir, Arrays.asList(environmentName),
                    commonPropsFile, interpolateEnvProperties,
                    defaultsPropertyName, getLog());

            for (EnvironmentConfig config : configDef) {
                doOutput(config.getProperties());
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generte config", e);
        }
    }

    private void doOutput(Properties props) {
        Log log = getLog();
        log.info("Config for environment:" + environmentName);
        Enumeration<Object> keys = props.keys();
        while (keys.hasMoreElements()) {
            Object o = keys.nextElement();
            String key = (String) o;
            log.info(key + "->" + props.getProperty(key));
        }
    }

}
