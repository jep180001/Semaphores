import java.util.*;
import java.util.concurrent.Semaphore;

public class Project2 {

	//variables
	final static int CUSTTHREADS = 50;
	final static int WORKTHREADS = 3;
	static int count = 0;
	
	//semaphores
	protected static Semaphore maxCap = new Semaphore(10, true);
	protected static Semaphore custReady = new Semaphore(0, true);
	protected static Semaphore workReady = new Semaphore(0, true);
	protected static Semaphore workerAvail = new Semaphore(3, true);
	protected static Semaphore mutex1 = new Semaphore(1, true);
	protected static Semaphore mutex2 = new Semaphore(1, true);
	protected static Semaphore mutex3 = new Semaphore(1, true);
	protected static Semaphore useScale = new Semaphore(1, true);
	protected static Semaphore gotTask = new Semaphore(0, true);
	protected static Semaphore finished[] = new Semaphore[50];
	static {
	    for(int i = 0; i < 50; i++) {
	        finished[i] = new Semaphore(0, true);
	    }   
	}
	//queues to implement FIFO
	protected static Queue<Integer> nextCustomer = new LinkedList<>();
	protected static Queue<Integer> nextWorker = new LinkedList<>();
	protected static Queue<Integer> nextTask = new LinkedList<>();
	
	//main function
	public static void main(String[] args) {
		
		//arrays used to create the threads
		Thread customerThreads[] = new Thread[CUSTTHREADS];
		Customer customerArray[] = new Customer[CUSTTHREADS];
		Thread workerThreads[] = new Thread[WORKTHREADS];	
	    PostalWorker workerArray[] = new PostalWorker[WORKTHREADS];
	    
	    //create postal worker threads - 3 workers
	    for(int j = 0; j < WORKTHREADS; ++j) {
	    	  
	    	  workerArray[j] = new PostalWorker(j);
	    	  workerThreads[j] = new Thread(workerArray[j]);
	    	  workerThreads[j].start();
	    }
	    
	    //create customer threads - 50 customers
	    for(int i = 0; i < CUSTTHREADS; ++i) {
	    	
	    	customerArray[i] = new Customer();
	    	customerThreads[i] = new Thread(customerArray[i]);
	    	customerThreads[i].start();
	  
	    }
		//join the customer threads
	    for(int k = 0; k < CUSTTHREADS; ++k) {
	    	
	    	try {
			
	    		customerThreads[k].join();
	    		
	    		System.out.println("Customer " + k + " has joined");
	    		
	    	} catch (InterruptedException e) {
			 
	    	}
	    }
		
	    //the program has finished, terminate
		System.exit(0);
	}

}
