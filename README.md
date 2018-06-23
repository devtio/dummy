# Dummy Application #

The purpose of this application is to help test various routing scenarios in Kubernetes.

## How Tos ##

### Build Docker Image ###

Build the JAR

```
./gradlew clean buildImage  
```

Setup Minikube env vars to push to it's docker repo

```
eval $(minikube docker-env)
```

Then build the image
```
docker build -t devtio/dummy:1.0.1 build/docker
```

### Deploy to Kube ###

A set of scripts exist inside this 'dummy' project that can setup the following:
- namespace
- deployments
- services

You can configure what you want to setup in a 'scenario' file. Eg. scenario1.yml

At the moment, to run a scenario, you have to go to the "ScenarioRunnerTest" and uncomment the test there, then run it.
This will use your currently logged in Kube credentials to execute and setup the desired scenario.

