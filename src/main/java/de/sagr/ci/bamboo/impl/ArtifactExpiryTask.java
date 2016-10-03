package de.sagr.ci.bamboo.impl;

import com.atlassian.bamboo.artifact.Artifact;
import com.atlassian.bamboo.build.artifact.ArtifactHandler;
import com.atlassian.bamboo.build.artifact.ArtifactHandlingUtils;
import com.atlassian.bamboo.build.artifact.ArtifactManager;
import com.atlassian.bamboo.plan.artifact.ArtifactContext;
import com.atlassian.bamboo.spring.ComponentAccessor;
import com.atlassian.bamboo.task.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by grebe on 27.08.2016.
 */
public class ArtifactExpiryTask implements TaskType {

    @Override
    public TaskResult execute(TaskContext taskContext) throws TaskException {
        final ArtifactContext artifactContext = taskContext.getBuildContext().getArtifactContext();
        // get configured artifacts
        final String artifactsToExpire = taskContext.getConfigurationMap().get(ArtifactExpiryTaskConfigurator.ARTIFACT_EXPIRY_PARAM_NAME);
        final String[] artifactsToExpireList = StringUtils.split(artifactsToExpire, ";");
        for (String nextArtifactName : artifactsToExpireList) {
            for (Artifact nextArtifact : artifactContext.getSharedArtifactsFromPreviousStages().get(nextArtifactName)) {
                removeArtifact(nextArtifact);
            }
        }

        // TODO remove artifacts from build context
        ComponentAccessor.newOsgiServiceProxy(ArtifactManager.class).get().removeOrphanedArtifacts();

        return TaskResultBuilder
                .newBuilder(taskContext)
                .build();
    }

    private void removeArtifact(final Artifact artifact) {
        final ArtifactHandler artifactHandler = ArtifactHandlingUtils.getArtifactHandlerForLink(ComponentAccessor.PLUGIN_ACCESSOR.get(), artifact.getLinkType());
        artifactHandler.removeArtifactFromStorage(artifact, null);
    }
}
