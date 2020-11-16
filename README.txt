For detailed instruction, please read the section 6 in the final report.

(1). Front-end Setup

1. Change the directory to "frontend".

2. Set up the npm environment.
   
There is a txt file in our frontend folder named "node_modules_link". If the user's npm version is too old, they can download the node modules via the link in this file.

Once you download the node modules zip file to the frontend folder. Double click the zip file and extract the "node_modules" folder to the current path. And after the extraction, the folder content shows up. 

3. Run the command in the terminal (console) in this folder:
	npm run serve

4. Now you can visit our website on: "http://localhost:8080/".


(2). Back-end Setup

1. Building System 

We have already provided the packed server propertysale-0.0.1-SNAPSHOT.jar in the 
root directory. Alternatively, you can build the server by yourself following
steps below:
	a. Click to enter the propertysale folder where is source file of the 
        	system, where you can find pom.xml.
	b.  Run the Maven command to pack the server in the console:
		mvn install -f pom.xml 
	c. Now, you can find the packer jar file propertysale-0.0.1-SNAPSHOT.jar in the 		target directory.
2. Running System 

	a. Change directory to the target directory.
	b. Run Java command under the directory which has propertysale-0.0.1-SNAPSHOT.jar:
			java -jar propertysale-0.0.1-SNAPSHOT.jar
	Now, you can find the system is running on Port 8060.

(3). Amazon Web Server Recovery
We have already started a cloud service on Amazon Web Services where we are running 
database, Redis and other important components in our system. However, the AWS is very unstable that it may crash. If the server cannot connect Redis or RabbitMQ, which means it need to be restarted.

So that, you should follow the steps below to recovery  the cloud server:

1. Login

Go to the home page https://aws.amazon.com and sign in with:
Username: alphago9900@gmail.com
Password: Alphago9900,.

2. Connecting to Service Instance
	a. Click EC2 then select Instance on the side bar.
	b. Click the instance with ID i-0a8e7d752018bf9ff.
	c. Click the button in the upper right corner. 
	d. Click the orange button connect to connect the cloud server.
	Now, we can use console to restart all services.

3. Restarting all services 
Attention!! The following steps must under the \ or /home/ubuntu. 
Do not move to other paths!

a. Restart Rabbit MQ:
	sudo docker restart rabbit
b. Restart Redis:
	sudo /usr/redis/bin/redis-server /usr/redis/bin/6379.conf
c. Restart Image Server:
	nohup java -jar /home/ubuntu/ImageServer/security.jar > log.file 2>&1 & 

If the restart is not successful or there are other problems, please contact to Xiaohan Zhu via z5187021@ad.unsw.edu.au.

		 
