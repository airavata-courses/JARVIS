### This branch is used to test CI/CD
### CICD Setup on XSEDE Jetstream
# Jarvis-cd Steps to install jenkins:
* https://railskey.wordpress.com/2016/04/21/install-jenkins-on-ubuntu/
* Installed Java
* Installed Jenkins
* Access on jarvis-cd:8080 port
### Setting up Jenkins for Github repo.
* Slave node: https://www.mirantis.com/blog/setting-external-openstack-testing-system-part-2/
* Deployed build on jenkins vm (jarvis-cd) and images are placed in docker repo in (jarvis-master)vm.
### Jenkins Stages:
* Cleaning up the previous build images.
* Building docker images with new build number on jenkins vm.
* Pushing images to jarvis-master and deploy kubernetes.
* We are triggering the jobs jarvis_jenkins (master node) which will trigger the sub jobs jarvis-cd-new (slave node).
### Plugin used in Jenkins:
* Parameterized Trigger Plugin
* Git Integration Plugin
### Architecture:
![image](https://user-images.githubusercontent.com/89654540/162112653-1a55191a-a83b-4555-b8a4-50f3a65927bb.png)
