# configfilter-maven-plugin
Maven pluging to create multiple resource when installing the project.

Create multiple properties such as prod.properties, uat.properties, and   dev.properties in under config-template directory under the main in the  project. Then same file sets will be created under target/generated-config.

Example:

```
<plugin>
    <inherited>false</inherited>
    <groupId>com.fckey.util</groupId>
    <artifactId>configfilter-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <environmentName>prod</environmentName>
        <environmentNamesString>dev,uat,prod</environmentNamesString>
        <configDirectory>${basedir}/src/main/config</configDirectory>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>generate-config</goal>
            </goals>
            <phase>
                generate-sources
            </phase>
        </execution>
    </executions>
</plugin>
```