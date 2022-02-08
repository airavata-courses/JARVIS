# JARVIS Assignment 1

## Napkin Diagram

![Napkin Diagram](https://user-images.githubusercontent.com/22557048/152892846-99e400bf-0cf2-44ba-a407-ce3cb34d833b.png)


## Design Diagram

![Design Diagram](https://user-images.githubusercontent.com/22557048/152893155-2e558d2c-d486-4070-89b9-abf9153e8221.png)


## User Flow Diagram

![User flow diagram](https://user-images.githubusercontent.com/22557048/152893438-234bd3c7-8ea7-4c45-9b13-2c7cca853d22.png)



## Requirements
- Hard requirement: Linux system
- Docker needs to be installed in the host system
- The entire project runs on a single machine
- requires ``make`` to build

## Build
Just run make
> make

Head to a web browser and go to http://localhost to access the application

To cleanup
> make cleanup

## Functionality
- Open Web browser and launch http://localhost
- Click on Sign in button, you'll be navigated to login page to enter your credentials
- First time user should register by clicking on + icon, enter username, password and click on next button, you'll be logged in and navigated to home page.
- If you are already registered you can enter your credentials and login by clicking on go button.
- Once you are on home page select location and date time and click on find. You'll have to wait for a few seconds for response.
- A map with reflexivity plot is generated.
- On homepage you can view user history by clicking on history button which navigates you to history page.
- On homepage you can logout by clicking on logout button which navigates you to index page.
