
//class for the postal worker
public class PostalWorker implements Runnable{

	//variables
	private int workerID;
	int worker;
	
	//constructor
	PostalWorker(int num) {
		
		this.workerID = num;
		
		//worker thread created
		workerCreated(workerID);
		
	}
	//function runs all the worker tasks
	@Override
	public void run() {
		
		try {
			
			while(true) {
				
				//wait for customer
				Project2.custReady.acquire();
				
				//add worker to queue
				Project2.mutex3.acquire();
				addWorker(workerID);
				Project2.mutex3.release();
				
				//remove customer from queue
				Project2.mutex2.acquire();
				int customer = removeCustomer();
				Project2.mutex2.release();
				
				//help customer if a worker is available
				Project2.workerAvail.acquire();
				helpCustomer(workerID, customer);
				Project2.workerAvail.release();
				
				//signal worker is ready to help a customer
				Project2.workReady.release();
				
				//wait for customer to get task
				Project2.gotTask.acquire();
				
				//finish helping the customer
				Project2.useScale.acquire();
				finishCustomer(workerID, customer);
				Project2.useScale.release();
				Project2.finished[customer].release();
			}
		
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	//function to get worker ID
	public int getWorkerID() {
		
		return workerID;
	}
	//function to display creation of worker thread
	public void workerCreated(int workerID) {
		
		//display
		System.out.println("Postal worker " + workerID + " created");
		
	}
	//function to display which customer the worker is helping
	public void helpCustomer(int worker, int customer) {
		
		//display
		System.out.println("Postal worker " + worker + " serving customer " + customer);
	}
	//function to display the worker is done helping the customer
	public void finishCustomer(int worker, int customer) throws InterruptedException {
		
		int task = Project2.nextTask.remove();
		
		//if the task is mail a package, use a scale
		if(task == 3) {
			
			//display that the scale is being used
			System.out.println("Scales in use by postal worker " + worker);
			
			//have the thread sleep based on task table
			Thread.sleep(2000);
			
			//display
			System.out.println("Postal worker " + worker + " finished serving customer " + customer);
			
		} else if (task == 2) {
			
			//have the thread sleep based on task table
			Thread.sleep(1500);
			
			//display
			System.out.println("Postal worker " + worker + " finished serving customer " + customer);
			
		} else if (task == 1) {
			
			//have the thread sleep based on task table
			Thread.sleep(1000);
			
			//display
			System.out.println("Postal worker " + worker + " finished serving customer " + customer);
		}	
	}
	//function to remove customer to queue
	public int removeCustomer() {
			
		//remove from queue
		return Project2.nextCustomer.remove();
		
	}
	//function to add customer to queue
	public void addWorker(int worker) {
			
		//add to queue
		Project2.nextWorker.add(worker);
	}
		
}
