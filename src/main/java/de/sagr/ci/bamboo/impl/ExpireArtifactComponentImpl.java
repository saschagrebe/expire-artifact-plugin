package de.sagr.ci.bamboo.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import de.sagr.ci.bamboo.api.ExpireArtifactComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({ExpireArtifactComponent.class})
@Named ("expireArtifactComponent")
public class ExpireArtifactComponentImpl implements ExpireArtifactComponent {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public ExpireArtifactComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "expireArtifactComponent:" + applicationProperties.getDisplayName();
        }
        
        return "expireArtifactComponent";
    }
}