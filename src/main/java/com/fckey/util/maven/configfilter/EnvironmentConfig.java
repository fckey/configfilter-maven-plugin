package com.fckey.util.maven.configfilter;

import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.multi.MultiDelimiterStringSearchInterpolator;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by fckey on 2016/04/09.
 */
public class EnvironmentConfig {

    public static final String ENV_NAME_PROPERTY = "env.name";
    private final String envName;
    private final Properties props;

    public EnvironmentConfig(String envName, Properties props) {
        this.envName = envName;
        this.props = props;
        this.props.setProperty(ENV_NAME_PROPERTY, envName);
    }

    public Properties getProperties() {
        return new SortedProperties(props);
    }

    public Properties getProperties(boolean intepolate) throws InterpolationException {
        return new SortedProperties(intepolate ?
                interpolateProperties(props, getInterpolator()):
        props);
    }

    public File getOutputDirectory(File roodOutputDir) {
        File envDir = new File(roodOutputDir, envName);
        return envDir;
    }

    public List<File> getOverrideDirectories(File rootInputDir, String overrideDirName) {
        File overrideRoot = new File(rootInputDir, overrideDirName);
        File envRoot = new File(overrideRoot, envName);
        return Arrays.asList(envRoot);
    }

    public String getEnvName() {
        return envName;
    }

    public Interpolator getInterpolator() {
        Interpolator interpolator = new MultiDelimiterStringSearchInterpolator();
        interpolator.addValueSource(new PropertiesBasedValueSource(getProperties()));
        return interpolator;
    }


    private Properties interpolateProperties(Properties props, Interpolator interpolator) throws InterpolationException {
        Properties interpolatedForEnv = new Properties();
        for (String property : props.stringPropertyNames()) {
            interpolatedForEnv.setProperty(property,
                    interpolator.interpolate(props.getProperty(property)));
        }
        return interpolatedForEnv;
    }
}
