import java.io.*;
import java.util.*;

class Job{
	int number;
	int time;
	int jobSize;
	boolean used;
	boolean finish;
}
class Memory extends Job{
	int number;
	int blockSize;
	boolean free;
	Job j;
}
public class lab3{
	public static ArrayList <Job> jobs = new ArrayList <Job>();
	public static ArrayList <Job> jobs2 = new ArrayList <Job>();
	public static ArrayList <Job> jobs3 = new ArrayList <Job>();
	public static ArrayList <Memory> memory = new ArrayList <Memory>();
	public static ArrayList <Float> tp = new ArrayList <Float>();
	public static int ans = 0;

	public static void main(String[] args){
		readFile("job list.txt", 1);	
		readFile("memory list.txt", 2);
		int  clear = 0;

		while(ans != 5){
			Scanner sc = new Scanner(System.in);
			System.out.print("\n\nCHOOSE OPERATION\n\n[1] First-fit\n[2] Best-fit\n[3] Worst-fit\n[4] Summary\n[5] Exit \nchoice: ");
     		ans = sc.nextInt();
     		cls();
     		if(ans == 1){
				memAlgo(jobs, 1);
     		}
     		else if(ans == 2){
				memAlgo(jobs2, 2);
     		}
     		else if(ans == 3){
     			memAlgo(jobs3, 3);
     		}
     		else if(ans == 4){
     			summary();
     		}
 			if(ans != 5 && ans != 4){
 				System.out.print("\n\n\t\t\t\tclear? [1] YES [2] NO: ");
	     		clear = sc.nextInt();
	     		if(clear == 1){
	     			cls();     			
	     		}
 			}
 				
		}
	}
	public static void readFile(String str, int arrayNum){
		String[] array;
		int i = 0;
		try{
            FileInputStream fstream = new FileInputStream(str);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = "";
            while ((strLine = br.readLine()) != null) {
            	if(arrayNum == 1){
	            	Job job = new Job();
	            	Job job2 = new Job();
	            	Job job3 = new Job();

	            	if( i > 0){
			            array = strLine.split("\\s+");
		            	job.number = Integer.parseInt(array[0]);
		            	job.time = Integer.parseInt(array[1]);
		            	job.jobSize = Integer.parseInt(array[2]);
		            	job.used = false;
		            	job.finish = false;
		            	jobs.add(job);

		            	job2.number = Integer.parseInt(array[0]);
		            	job2.time = Integer.parseInt(array[1]);
		            	job2.jobSize = Integer.parseInt(array[2]);
		            	job2.used = false;
		            	job2.finish = false;
		            	jobs2.add(job2);

		            	job3.number = Integer.parseInt(array[0]);
		            	job3.time = Integer.parseInt(array[1]);
		            	job3.jobSize = Integer.parseInt(array[2]);
		            	job3.used = false;
		            	job3.finish = false;
		            	jobs3.add(job3);

			        }
			    }
			    else{
			    	Memory mem = new Memory();
	            	if( i > 0){
			            array = strLine.split("\\s+");
		            	mem.number = Integer.parseInt(array[0]);
		            	mem.blockSize = Integer.parseInt(array[1]);
		            	mem.free = true;
		            	memory.add(mem);
			        }
			    }
		        i++;
            }
        in.close();
        }catch (Exception e){
                System.err.println("Error: " + e.getMessage());
        }
	}

	public static void memAlgo(ArrayList<Job> jobs, int n){
		float count = 0, throughput;
		if(n == 2){
			Collections.sort(memory,(mem1, mem2) -> mem1.blockSize - mem2.blockSize);
		}
		else if (n == 3){
			Collections.sort(memory,(mem1, mem2) -> mem1.blockSize + mem2.blockSize);
		}

		while(!finishAllJobs(n) && canAccept(n)){
			if(memoryFree()){
				for(int i = 0; i < jobs.size(); i++){
					for(int j = 0; j < memory.size(); j++){
						if(jobs.get(i).jobSize <= memory.get(j).blockSize && jobs.get(i).time > 0 && memory.get(j).free && (!jobs.get(i).used)){
							jobs.get(i).used = true;
							memory.get(j).j = jobs.get(i);
							memory.get(j).free = false;
						}
					}
				}
			}
			for(int i = 0; i < memory.size(); i++){
				if(memory.get(i).j!=null){
					if(memory.get(i).j.time > 0){
						memory.get(i).j.time--;
					}

					else if(memory.get(i).j.time == 0){
						memory.get(i).j.finish = true;
						memory.get(i).free = true;
						memory.get(i).j = null;
					}
				}
				
			}
			throughput = printElements(2, n, count);
			count++;

			try {
	            // thread to sleep for 1000 milliseconds
	            Thread.sleep(1000);
	         } catch (Exception e) {
	            System.out.println(e);
	         }
	        if(canAccept(n)){
	        	cls();
	        }
	        else{
	        	tp.add(throughput);
	        }
		}
	}

	public static boolean finishAllJobs(int n){
		if(n == 1){
			for (int i = 0; i< jobs.size(); i++){
				if(!jobs.get(i).finish){
					return false;
				}
			}
		}
		else if(n == 2){
			for (int i = 0; i< jobs2.size(); i++){
				if(!jobs2.get(i).finish){
					return false;
				}
			}
		}
		else if (n == 3){
			for (int i = 0; i< jobs3.size(); i++){
				if(!jobs3.get(i).finish){
					return false;
				}
			}
		}
		
		return true;
	}
	public static boolean memoryFree(){
		for (int i = 0; i< memory.size(); i++){
			if(memory.get(i).free){
				return true;
			}
		}
		return false;
	}
	public static boolean canAccept(int n){
		ArrayList <Job> unfinishedJobs = new ArrayList<Job>();

		if(n == 1){
			for(int i = 0; i < jobs.size(); i++){
				if(!jobs.get(i).finish){
					unfinishedJobs.add(jobs.get(i));
				}
			}
		}
		else if(n == 2){
			for(int i = 0; i < jobs2.size(); i++){
				if(!jobs2.get(i).finish){
					unfinishedJobs.add(jobs2.get(i));
				}
			}
		}
		else if(n == 3){
			for(int i = 0; i < jobs3.size(); i++){
				if(!jobs3.get(i).finish){
					unfinishedJobs.add(jobs3.get(i));
				}
			}
		}
		
		if(unfinishedJobs.size()==0){
			return true;
		}
		for(int i = 0; i < unfinishedJobs.size(); i++){
			for(int j = 0; j < memory.size(); j++){
				if(unfinishedJobs.get(i).jobSize <= memory.get(j).blockSize){
					return true;
				}
			}
		}
		return false;
	}
	public static void cls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();           
        }
        catch (Exception e){
            System.err.println(e);           
        }
    }
	public static float printElements(int arrayNum, int n, float time){
		String s = "";
		float count = 0;
		switch(n){
			case (1):{
				s = "FIRST FIT";
				break;
			} 
			case (2):{
				s = "BEST FIT";
				break;
			}
			case (3):{
				s = "WORST FIT";
				break;
			}
		}
		switch(n){
			case (1):{
				for (int i = 0; i < jobs.size(); i++){
					if(jobs.get(i).finish){
						count++;
					}
				}
				break;
			} 
			case (2):{
				for (int i = 0; i < jobs2.size(); i++){
					if(jobs2.get(i).finish){
						count++;
					}
				}
				break;
			}
			case (3):{
				for (int i = 0; i < jobs3.size(); i++){
					if(jobs3.get(i).finish){
						count++;
					}
				}
				break;
			}
		}
		
		float tp = count/time;
		System.out.println("\t\t\t\t" + s + " time: " + count + " ms    THROUGHPUT: " + tp + " (jobs/millisecond)");
		String format1 = " \t\t\t\t| %-7s  ||  %-9s  | %-11s | %-11s |  %-11s |  %-11s |%n";
		System.out.println("\t\t\t\t-----------------------------------------------------------------------------------------------");
		System.out.format(format1, " MEMORY BLOCK  ", "SIZE   ", "JOB LIST   ","JOB SIZE", "FRAGMENTATION", "PERCENTAGE");
		System.out.println("\t\t\t\t-----------------------------------------------------------------------------------------------");
		String format2 = "\t\t\t\t|      %-12s||  %-9s  | %-11s | %-11s |  %-11s   |  %-11s |%n";
		
		if(arrayNum == 1){
			for (int i = 0; i < jobs.size(); i++){
				System.out.println(jobs.get(i).number + "[" + jobs.get(i).time + "]");
			}
		}
		else{
			for (int i = 0; i < memory.size(); i++){
				if(memory.get(i).j != null){
					System.out.format(format2, memory.get(i).number, memory.get(i).blockSize, memory.get(i).j.number + " [" + memory.get(i).j.time + "]", memory.get(i).j.jobSize, memory.get(i).blockSize - memory.get(i).j.jobSize, ((float)memory.get(i).j.jobSize/(float)memory.get(i).blockSize)*100 + "%");
				}
			}
		}
		System.out.println("\t\t\t\t-----------------------------------------------------------------------------------------------");
		
		System.out.println("\n\t\t\t\tJOBS IN QUEUE");
		System.out.println("\t\t\t\t-------------------------------------------");
		String format3 = " \t\t\t\t|   %-7s  ||%-9s  |   %-11s |%n";
		System.out.format(format3,  "JOB ", " TIME ", " JOBSIZE");
		System.out.println("\t\t\t\t-------------------------------------------");
		
		switch(n){
			case (1):{
				for(int i = 0; i < jobs.size(); i++){
					if(!jobs.get(i).used)
						System.out.format(format3,jobs.get(i).number,"\t[" + jobs.get(i).time + "]", jobs.get(i).jobSize);
				}
				break;
			} 
			case (2):{
				for(int i = 0; i < jobs2.size(); i++){
					if(!jobs2.get(i).used)
						System.out.format(format3,jobs2.get(i).number,"\t[" + jobs2.get(i).time + "]", jobs2.get(i).jobSize);
				}
				break;
			}
			case (3):{
				for(int i = 0; i < jobs3.size(); i++){
					if(!jobs3.get(i).used)
						System.out.format(format3,jobs3.get(i).number,"\t[" + jobs3.get(i).time + "]", jobs3.get(i).jobSize);
				}
				break;
			}
		}
		System.out.println("\t\t\t\t-------------------------------------------");

		return tp;
	}
	public static void summary(){
		float min = tp.get(0);
		System.out.println("\t\t\t\t\n");
		System.out.println("\t\t\t\tFIRSTtSUMMARY FIT: " + tp.get(0) + "jobs/millisecond");
		System.out.println("\t\t\t\tBEST FIT: " + tp.get(1) + "jobs/millisecond");
		System.out.println("\t\t\t\tWORST FIT: " + tp.get(2) + "jobs/millisecond");
		for(int i = 1; i < tp.size(); i++){
			if(tp.get(i) < min)
				min = tp.get(i);
		}
	}

}