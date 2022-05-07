# JARVIS Assignment 3 test

## Napkin Diagram

![Napkin Diagram](https://user-images.githubusercontent.com/22557048/152892846-99e400bf-0cf2-44ba-a407-ce3cb34d833b.png)


## Design Diagram

![Design Diagram](https://user-images.githubusercontent.com/89654540/167268136-b6cdc99c-d768-465d-8523-66ba9e5efffe.png)


## User Flow Diagram

![image](https://user-images.githubusercontent.com/89654540/167268386-bdc0c0e5-935c-4249-999c-bd0639b10c7d.png)

## NexRad Parameter plot 
![User flow diagram](https://user-images.githubusercontent.com/22557048/152893438-234bd3c7-8ea7-4c45-9b13-2c7cca853d22.png)

## Merra data plot 
- Ozone level
![image](https://user-images.githubusercontent.com/89654540/167268396-ec2ad534-1151-4d67-94e9-f6833b450290.png)
- COPD CO chemical production level
![image](https://user-images.githubusercontent.com/89654540/167268402-0969e5c2-f709-4720-9f08-9eac738c37ed.png)

## Requirements
- Hard requirement: Linux system
- If there is any error in 'RUN apt install -y mongodb-org' during make, go to a1-authserver/Dockerfile. line 10. if [arch=amd64] change it to [arch=arm64] else if [arch=arm64], change it to [arch=amd64].
- Docker needs to be installed in the host system
- The entire project runs on a single machine
- requires ``make`` to build

## Build
Just run make
> make local

Head to a web browser and go to http://localhost to access the application

To cleanup
> make cleanup_local
## Functionality
- Open Web browser and launch http://localhost
- Click on Sign in button, you'll be navigated to login page to enter your credentials
- First time user should register by clicking on + icon, enter username, password and click on next button, you'll be logged in and navigated to home page.
- If you are already registered you can enter your credentials and login by clicking on go button.
- Once you are on home page select location and date time and click on find. You'll have to wait for a few seconds for response.
- A map with reflexivity plot is generated.
- On homepage you can view user history by clicking on history button which navigates you to history page.
- On homepage you can logout by clicking on logout button which navigates you to index page.
