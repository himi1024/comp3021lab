import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class QuarantineSystem {
    public static class DashBoard {
        List<Person> People;
        List<Integer> patientNums;
        List<Integer> infectNums;
        List<Double> infectAvgNums;
        List<Integer> vacNums;
        List<Integer> vacInfectNums;

        public DashBoard(List<Person> p_People) {
            this.People = p_People;
            this.patientNums = new ArrayList<>(8);
            this.infectNums = new ArrayList<>(8);
            this.infectAvgNums = new ArrayList<>(8);
            this.vacNums = new ArrayList<>(8);
            this.vacInfectNums = new ArrayList<>(8);
        }
        
        public void runDashBoard() {
        
            for (int i = 0; i < 8; i++) {	
                this.patientNums.add(0);
                this.infectNums.add(0);
                this.vacNums.add(0);
                this.vacInfectNums.add(0);
            }
            /*
             * TODO: Collect the statistics based on People
             *  Add the data in the lists, such as patientNums, infectNums, etc.
             */ 
            int patient = 0;
            Integer[] infectCount = new Integer[8];
            Arrays.fill(infectCount, 0);
            for(Person p : People) { 
            	//System.out.println("Patient:" + p);
            	int i = p.getAge()/10;
            	if(i > 7)
            		i = 7;
            	if(p.getInfectCnt() >= 1) {	
            		patientNums.set(i, patientNums.get(i)+1);
            		infectNums.set(i, infectNums.get(i)+1);
            	}
            	if(p.getIsVac()==true) {
            		vacNums.set(i,  vacNums.get(i)+1);
            	}
            	if(p.getInfectCnt() >= 1 & p.getIsVac()==true) {
            		vacInfectNums.set(i, vacInfectNums.get(i)+1);
            	}
            	infectCount[i] += p.getInfectCnt(); 
            	}
                    
            for(int i = 0; i < 8;i++) {
            	double avg = 0;
            	if(infectCount[i] != 0) {
            		avg = (double)(infectCount[i])/patientNums.get(i);    
            	}
            	infectAvgNums.add(avg);
            }     
            //System.out.println(patient);
        }
          
        
        
    }


    private List<Person> People;
    private List<Patient> Patients;

    private List<Record> Records;
    private List<Hospital> Hospitals;
    private int newHospitalNum;

    private DashBoard dashBoard;

    public QuarantineSystem() throws IOException {
        importPeople();
        importHospital();
        importRecords();
        dashBoard = null;
        Patients = new ArrayList<>();
    }

    public void startQuarantine() throws IOException { //Done
        /*
         * Task 1: Saving Patients
         */
        System.out.println("Task 1: Saving Patients");              
        /*
         * TODO: Process each record
         */        
        for(Record r : Records) { //loop whole List<Record> Records
        	switch(r.getStatus()) {
        		case Confirmed:
        			saveSinglePatient(r);
        			break;
        		case Recovered:
        			releaseSinglePatient(r);
        		default:
        			break;
        	}    	
        }     
        exportRecordTreatment();
        /*
         * Task 2: Displaying Statistics
         */
        

        System.out.println("Task 2: Displaying Statistics");
        dashBoard = new DashBoard(this.People);
        
        dashBoard.runDashBoard();
        exportDashBoard();

    }

    /*
     * Save a single patient when the status of the record is Confirmed
     */
    public void saveSinglePatient(Record record) { //Done
    	//TODO
    	Patient newP = null;
    	for(Person person : People) {
    		if(person.getIDCardNo().compareTo(record.getIDCardNo()) == 0) { //Find the Person
    			newP = new Patient(person, record.getSymptomLevel()); 
    			person.setInfectCnt(person.getInfectCnt()+1);
    			break;
    		}
    	}  	 
    	
    	
    	if( newP != null) {
    		Hospital nearH = null;
    		for(Hospital h: Hospitals) { //Selection Nearest Hospital		
    			if(nearH == null || (float)h.getLoc().getDisSquare(newP.getLoc()) < 
						(float)nearH.getLoc().getDisSquare(newP.getLoc())){
    				if(h.getCapacity().getSingleCapacity(newP.getSymptomLevel()) > 0)
    					nearH = h;
    			}			
    		}  	
    		if(nearH == null) { //No Capacity for all existing Hospital
    			newHospitalNum++;
    			nearH = new Hospital("H-New-"+ newHospitalNum, newP.getLoc());
    			record.setHospitalID(nearH.HospitalID);
    			newP.setHospitalID(nearH.HospitalID);
    			Patients.add(newP);
    			nearH.getCapacity().decreaseCapacity(newP.getSymptomLevel());
    			Hospitals.add(nearH);  	
    		}
    		else { //Enough Capacity
    			nearH.addPatient(newP);
    			//System.out.println(nearH.HospitalID + " " + nearH.getCapacity().CriticalCapacity);
    			record.setHospitalID(nearH.HospitalID);
    			newP.setHospitalID(nearH.HospitalID);
    			Patients.add(newP);
    			nearH.getCapacity().decreaseCapacity(newP.getSymptomLevel());   		
    		}
    	}
    	//System.out.println(Patients); //Self-checking
    	
    	
    }

    /*
     * Release a single patient when the status of the record is Recovered
     */
    public void releaseSinglePatient(Record record) { //Done
        //TODO    	
    	for(Patient p : Patients) {
    		if(p.getIDCardNo().compareTo(record.getIDCardNo()) == 0) { //Find the Person
    			record.setHospitalID(p.getHospitalID());
    			//System.out.println(record.getHospitalID());
    			for(Hospital h : Hospitals){
    				if(h.HospitalID.compareTo(record.getHospitalID()) == 0) {
    					h.releasePatient(p);   	
    					h.getCapacity().increaseCapacity(p.getSymptomLevel());
    					Patients.remove(p);
    					break;    				
    				}
    			}
    			break;
    		}
    	}    	    	    	
    }

    /*
     * Import the information of the people in the area from Person.txt
     * The data is finally stored in the attribute People
     * You do not need to change the method.
     */
    public void importPeople() throws IOException {
        this.People = new ArrayList<>();
        File filename = new File("data/Person.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert (records.length == 6);
                String pIDCardNo = records[0];
                System.out.println(pIDCardNo);
                int XLoc = Integer.parseInt(records[1]);
                int YLoc = Integer.parseInt(records[2]);
                Location pLoc = new Location(XLoc, YLoc);
                assert (records[3].equals("Male") || records[3].equals("Female"));
                String pGender = records[3];
                int pAge = Integer.parseInt(records[4]);
                assert (records[5].equals("Yes") || records[5].equals("No"));
                boolean pIsVac = (records[5].equals("Yes"));
                Person p = new Person(pIDCardNo, pLoc, pGender, pAge, pIsVac);
                this.People.add(p);
            }
            line = br.readLine();
        }
    }

    /*
     * Import the information of the records
     * The data is finally stored in the attribute Records
     * You do not need to change the method.
     */
    public void importRecords() throws IOException {
        this.Records = new ArrayList<>();

        File filename = new File("data/Record.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert(records.length == 3);
                String pIDCardNo = records[0];
                System.out.println(pIDCardNo);
                assert(records[1].equals("Critical") || records[1].equals("Moderate") || records[1].equals("Mild"));
                assert(records[2].equals("Confirmed") || records[2].equals("Recovered"));
                Record r = new Record(pIDCardNo, records[1], records[2]);
                Records.add(r);
            }
            line = br.readLine();
        }
    }

    /*
     * Import the information of the hospitals
     * The data is finally stored in the attribute Hospitals
     * You do not need to change the method.
     */
    public void importHospital() throws IOException {
        this.Hospitals = new ArrayList<>();
        this.newHospitalNum = 0;

        File filename = new File("data/Hospital.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert(records.length == 6);
                String pHospitalID = records[0];
                System.out.println(pHospitalID);
                int XLoc = Integer.parseInt(records[1]);
                int YLoc = Integer.parseInt(records[2]);
                Location pLoc = new Location(XLoc, YLoc);
                int pCritialCapacity = Integer.parseInt(records[3]);
                int pModerateCapacity = Integer.parseInt(records[4]);
                int pMildCapacity = Integer.parseInt(records[5]);
                Capacity cap = new Capacity(pCritialCapacity, pModerateCapacity, pMildCapacity);
                Hospital hospital = new Hospital(pHospitalID, pLoc, cap);
                this.Hospitals.add(hospital);
            }
            line = br.readLine();
        }
    }

    /*
     * Export the information of the records
     * The data is finally dumped into RecordTreatment.txt
     * DO NOT change the functionality of the method
     * Otherwise, you may generate wrong results in Task 1
     */
    public void exportRecordTreatment() throws IOException {
        File filename = new File("output/RecordTreatment.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write("IDCardNo        SymptomLevel        Status        HospitalID\n");
        for (Record record : Records) {
            //Invoke the toString method of Record.
            bw.write(record.toString() + "\n");
        }
        bw.close();
    }

    /*
     * Export the information of the dashboard
     * The data is finally dumped into Statistics.txt
     * DO NOT change the functionality of the method
     * Otherwise, you may generate wrong results in Task 2
     */
    public void exportDashBoard() throws IOException {
        File filename = new File("output/Statistics.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);

        bw.write("AgeRange        patientNums        infectAvgNums        vacNums        vacInfectNums\n");

        for (int i = 0; i < 8; i++) {
            String ageRageStr = "";
            switch (i) {
                case 0:
                    ageRageStr = "(0, 10)";
                    break;
                case 7:
                    ageRageStr = "[70, infinite)";
                    break;
                default:
                    ageRageStr = "[" + String.valueOf(i) + "0, " + String.valueOf(i + 1) + "0)";
                    break;
            }
            String patientNumStr = String.valueOf(dashBoard.patientNums.get(i));
            String infectAvgNumsStr = String.valueOf(dashBoard.infectAvgNums.get(i));
            String vacNumsStr = String.valueOf(dashBoard.vacNums.get(i));
            String vacInfectNumsStr = String.valueOf(dashBoard.vacInfectNums.get(i));

            bw.write(ageRageStr + "        " + patientNumStr + "        " + infectAvgNumsStr + "        " + vacNumsStr + "        " + vacInfectNumsStr + "\n");
        }

        bw.close();
    }

    /* The entry of the project */
    public static void main(String[] args) throws IOException {
        QuarantineSystem system = new QuarantineSystem();
        System.out.println("Start Quarantine System");
        system.startQuarantine();
        System.out.println("Quarantine Finished");
    }
}
