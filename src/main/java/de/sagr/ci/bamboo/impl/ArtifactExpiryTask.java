package de.sagr.ci.bamboo.impl;

import com.atlassian.bamboo.artifact.Artifact;
import com.atlassian.bamboo.build.artifact.ArtifactHandler;
import com.atlassian.bamboo.build.artifact.ArtifactHandlingUtils;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.plan.artifact.ArtifactContext;
import com.atlassian.bamboo.spring.ComponentAccessor;
import com.atlassian.bamboo.task.*;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by grebe on 27.08.2016.
 */
public class ArtifactExpiryTask implements TaskType {

    @Override
    public TaskResult execute(TaskContext taskContext) throws TaskException {
        final ArtifactContext artifactContext = taskContext.getBuildContext().getArtifactContext();
        // get configured artifacts
        for (String nextArtifactName : getArtifactsToExpire(taskContext)) {
            final Multimap<String, Artifact> sharedArtifacts = artifactContext.getSharedArtifactsFromPreviousStages();
            for (Artifact nextArtifact : sharedArtifacts.get(nextArtifactName)) {
                removeArtifact(nextArtifact);
            }
            sharedArtifacts.removeAll(nextArtifactName);
        }

        return TaskResultBuilder
                .newBuilder(taskContext)
                .success()
                .build();
    }

    private String[] getArtifactsToExpire(TaskContext taskContext) {
        final ConfigurationMap config = taskContext.getConfigurationMap();
        final String artifactsToExpire = config.get(ArtifactExpiryTaskConfigurator.ARTIFACT_EXPIRY_PARAM_NAME);
        return StringUtils.split(artifactsToExpire, ";");
    }

    private void removeArtifact(final Artifact artifact) {
        final ArtifactHandler artifactHandler = ArtifactHandlingUtils.getArtifactHandlerForLink(ComponentAccessor.PLUGIN_ACCESSOR.get(), artifact.getLinkType());
        artifactHandler.removeArtifactFromStorage(artifact, null);
    }
}
