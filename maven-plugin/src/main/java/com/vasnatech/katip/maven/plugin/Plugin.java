package com.vasnatech.katip.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "katip", defaultPhase = LifecyclePhase.COMPILE)
public class Plugin extends AbstractMojo {


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("------------katip---------------");
    }
}
