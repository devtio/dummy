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


## How to call it ##

### Get the Deployed Host/Port ###

#### If you're using NodePort to expose your service ####

You can setup to use `NodePort` in your scenario.yml file by setting `externalService: true`

```yaml

namespace: dummy
apps:
  -
    name: a
    version: v1
    routing:
      externalService: true
```

Then see the value of the `NodePort` by viewing the services

```
kubectl get services -n dummy
```

Then call the dummy service

```
http://192.168.99.100:30290/hops?path=a-v1,b-v1,a-v2
```

