package com.fckey.util.maven.configfilter;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by fckey on 2016/04/09.
 *
 * Takes a template directory and a directory containing environment property files,
 * and meges the data into a set of merged config directories per environment.
 *
 * @goal generate-config
 * @phase generate-sources
 */
@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal", "unchecked"})
public class MergeConfigMojo extends ConfigFileMojo {

    public static final String DEFAULT_FILE_OVERRIDE_DIR = "_override";
    public static final String MERGED_ENV_PROPERTIES_FILE_NAME = "env"+ PROPERTIES_FILE_SUFFIX;

    /**
     * Location of the output dir
     *
     * @parameter default-value="${project.build.directory}/generated-resources/config"
     * @required
     */
    private File outputDir;

    /**
     * Location of the input dir
     *
     * @parameter default-value="${basedir}/src/main/config-template"
     * @required
     */
    private File inputDir;

    /**
     * The names of envs
     *
     * @parameter
     */
    private List<String> environmentNames = Collections.EMPTY_LIST;

    /**
     * The names of envs to build config for
     *
     * @parameter expression="${configFilter.environmentNames}"
     */
    private String environmentNamesString;

    /**
     * The name of the directory inside the configured input directory that contains
     * environment specific overides of specific files from the template
     *
     * @parameter
     */
    private String fileOverrideDir = DEFAULT_FILE_OVERRIDE_DIR;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        validateInput();
        try {
            ConfigDefinition configDef = new ConfigDefinition(configDir, dermineEnvNames(),
                    commonPropsFile, interpolateEnvProperties,
                    defaultsPropertyName, getLog());

            ConfigInput input = new ConfigInput(configDef, inputDir, fileOverrideDir);
            for (EnvironmentConfig config : configDef) {
                getLog().info("Merging config for env: " + config.getEnvName());
                input.mergeWithConfig(config, outputDir);
            }

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generte config", e);
        }
    }

    private List<String> dermineEnvNames() {
        List<String> names = new ArrayList<>();
        if (environmentNamesString != null) {
            String[] envs = environmentNamesString.split(",");
            names.addAll(Arrays.asList(envs));
        } else {
            names.addAll(environmentNames);
        }
        return names;
    }

    @Override
    protected void validateInput() throws MojoExecutionException {
        super.validateInput();
        if (!outputDir.exists()) {
            outputDir.mkdir();
        } else if (!outputDir.isDirectory()) {
            throw new MojoExecutionException(
                    String.format("Output directory '%s' is not available", outputDir.getAbsolutePath())
            );
        }

        if (!environmentNames.isEmpty() && environmentNamesString == null) {
            throw new MojoExecutionException(
                    "One of environmentNames or envNameString must be provided."
            );
        }
    }
}
