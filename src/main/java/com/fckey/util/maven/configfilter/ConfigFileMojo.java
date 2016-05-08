package com.fckey.util.maven.configfilter;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;

/**
 * Created by fckey on 2016/04/09.
 */
public abstract class ConfigFileMojo extends AbstractMojo {

    public static final String PROPERTIES_FILE_SUFFIX = ".properties";
    public static final String DEFAULT_PROPS_FILE_NAME = "default" + PROPERTIES_FILE_SUFFIX;
    public static final String DEFAULT_HIERARCHY_PROPS_NAME = "defaults";

    /**
     * The root directory containing env specific props
     *
     * @parameter default-value="${basedir}/src/main/config"
     * @required
     */
    protected File configDir;

    /**
     * The name of a file containing default properties for all envs
     *
     * @parameter
     */
    protected String commonPropsFile = DEFAULT_PROPS_FILE_NAME;

    /**
     * The property to read from each environment file
     *
     * @parameter
     */
    protected String defaultsPropertyName = DEFAULT_HIERARCHY_PROPS_NAME;

    /**
     * Whether the output env property file per env should be
     * interpolated or left with placeholder
     *
     * @parameter
     */
    protected boolean interpolateEnvProperties = true;

    protected void validateInput() throws MojoExecutionException {
        if (!configDir.exists() || !configDir.isDirectory()) {
            throw new MojoExecutionException(
                    String.format("Config directory '%s' is not available",configDir.getAbsolutePath())
            );
        }
    }

}
