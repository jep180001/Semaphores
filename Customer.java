import java.util.Random;

//class for the customer
public class Customer implements Runnable{

	//variables
	private int customer;
	
	//constructor
	Customer() {
		
		
	}
	@Override
	//implement all the customer tasks
	public void run() {
		
		try {
			
			//display created customer thread
			Project2.mutex2.acquire();
			customer = Project2.count;
			Project2.count = Project2.count + 1;
			createCustomer(customer);
			Project2.mutex2.release();
			
			//see if customer can enter post office
			Project2.maxCap.acquire();
			
			//display the customer entered the post office
			Project2.mutex2.acquire();
			enterOffice(customer);
			Project2.mutex2.release();
			
			//signal that the customer is ready in line
			Project2.custReady.release();
			
			//wait for worker to help customer
			Project2.workReady.acquire();
			
			//remove worker from queue that is going to help the customer
			Project2.mutex3.acquire();
			int worker = removeWorker();
			Project2.mutex3.release();
			
			//display the task the customer wants
			Project2.mutex2.acquire();
			int task = getTask(customer, worker);
			Project2.mutex2.release();
			
			//add the task to a queue for the worker
			Project2.mutex1.acquire();
			Project2.nextTask.add(task);
			Project2.mutex1.release();
			
			//signal that the customer is ready
			Project2.gotTask.release();
			
			//wait for the worker to finish helping
			Project2.finished[customer].acquire();
			
			//display that the customer has finished and leaves the office
			finishTask(customer, task);
			leaveOffice(customer);
			
			//signal that the customer left and next customer can enter
			Project2.maxCap.release();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	      
	    
	}

	//function randomly assigns a task to customer and displays the task
	//three options:
	//1. buy stamps
	//2. mail a letter
	//3. mail a package
	public int getTask(int customer, int worker) throws InterruptedException {
		
		//get one of the three options randomly
		Random rand = new Random();
		int task = rand.nextInt(3) + 1;
		
		if(task == 1) {
		
			//display the assigned task
			System.out.println("Customer " +  customer + " asks postal worker " + worker+ " to buy stamps");
			return task;
			
		
		//task 2: mail a letter
		} else if(task == 2) {
			
			//display the assigned task
			System.out.println("Customer " +  customer + " asks postal worker " + worker+ " to mail a letter");
			return task;
			
			
		//task 3: mail a package
		} else {
			
			//display the assigned task
			System.out.println("Customer " +  customer + " asks postal worker " + worker +" to mail a package");
			return task;
			
		}
		
	}
	//function to enter post office
	public void enterOffice(int customer) {
		
		//add to queue
		addCustomer(customer);
		
		//display
		System.out.println("Customer " + customer + " enters post office");
	}
	//function to display customer thread created
	public void createCustomer(int customer) {
		
		//display
		System.out.println("Customer " + customer + " created");
		
	}
	//function to display the customer has finished their task
	public void finishTask(int customer, int task) {
		
		//task 1: buy stamps
		if(task == 1) {
		
			//display the finished task
			System.out.println("Customer " + customer + " finished " + "buying stamps");
		
		//task 2: mail a letter
		} else if(task == 2) {
			
			//display the finished task
			System.out.println("Customer " + customer +  " finished " + "mailing a letter");
			
		//task 3: mail a package
		} else {
			
			//display the finished task
			System.out.println("Customer " + customer +  " finished " + "mailing a package");
			
		}
		
	}
	//function to display the customer has left the post office
	public void leaveOffice(int customer) {
			
		//display
		System.out.println("Customer " + customer + " leaves post office");
	}
	
	//function to add customer to queue
	public void addCustomer(int customer) {
		
		//add to queue
		Project2.nextCustomer.add(customer);
		
	}
	//function to remove customer from queue
	public int removeWorker() {
			
		//remove from queue
		return Project2.nextWorker.remove();
		
	}
	//function to return the customer thread
	public int returnCustomer() {
		
		return customer;
	}
	
}
