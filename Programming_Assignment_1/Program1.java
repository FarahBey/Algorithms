/*
 * Name: <your name>
 * EID: <your EID>
 */

 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.LinkedList;

 /**
  * Your solution goes in this class.
  *
  * Please do not modify the other files we have provided for you, as we will use
  * our own versions of those files when grading your project. You are
  * responsible for ensuring that your solution works with the original version
  * of all the other files we have provided for you.
  *
  * That said, please feel free to add additional files and classes to your
  * solution, as you see fit. We will use ALL of your additional files when
  * grading your solution. However, do not add extra import statements to this file.
  */
 public class Program1 extends AbstractProgram1 {
 
     /**
      * Determines whether a candidate Matching represents a solution to the stable matching problem.
      * Study the description of a Matching in the project documentation to help you with this.
      */
     @Override
     public boolean isStableMatching(Matching problem) {
         /* TODO implement this function */

         //inverse student preference list
         ArrayList<ArrayList<Integer>> studPref = problem.getStudentPreference();
         ArrayList<ArrayList<Integer>> uniPref = problem.getUniversityPreference();
         ArrayList<ArrayList<Integer>> invstudPref = new ArrayList<ArrayList<Integer>>();
         ArrayList<ArrayList<Integer>> invuniPref = new ArrayList<ArrayList<Integer>>();

         for(int i = 0; i < studPref.size();i++){
             invstudPref.add(new ArrayList<>());
         }
         for(int i = 0; i < studPref.size(); i++){
             for(int j = 0; j < studPref.get(i).size(); j++){
                 invstudPref.get(i).add(studPref.get(i).indexOf(j));
             }
         }
        //inverse university preference list
         for(int i = 0; i < uniPref.size();i++){
             invuniPref.add(new ArrayList<>());
         }
         for(int i = 0; i < uniPref.size(); i++){
             for(int j = 0; j < uniPref.get(i).size(); j++ ){
                 invuniPref.get(i).add(uniPref.get(i).indexOf(j));
             }
         }

         ArrayList<Integer> matchResults =  problem.getStudentMatching();
         for(int i = 0; i < problem.getStudentCount(); i++){
             for(int j = 0; j < problem.getStudentCount(); j++){
                 int c = matchResults.get(j);//university_we_are_currently_on, universe student j is with
                 if(c < invuniPref.size() && c >= 0 && i != j) {
                     if (matchResults.get(i) == -1 ) {
                         if (invuniPref.get(c).get(i) < invuniPref.get(c).get(j)) {
                             return false;
                         }
                     }

                     if (matchResults.get(i) != -1 && matchResults.get(i) != c) {
                         if (invuniPref.get(c).get(i) < invuniPref.get(c).get(j)) {
                             if (invstudPref.get(i).get(c) < invstudPref.get(j).get(c)) {
                                 return false;
                             }
                         }
                     }
                 }
             }
         }
         return true;
     }
 
     /**
      * Determines a solution to the stable matching problem from the given input set. Study the
      * project description to understand the variables which represent the input to your solution.
      *
      * @return A stable Matching.
      */
     @Override
     public Matching stableMatchingGaleShapley_universityoptimal(Matching problem) {
         /* TODO implement this function */

         ArrayList<ArrayList<Integer>> studPref = problem.getStudentPreference();
         ArrayList<ArrayList<Integer>> uniPref = problem.getUniversityPreference();
         int totalPositions = problem.totalUniversityPositions();

         // inverse student preference list
         ArrayList<ArrayList<Integer>> invstudPref = new ArrayList<ArrayList<Integer>>();
         for(int i = 0; i < studPref.size();i++){
            invstudPref.add(new ArrayList<>());
         }
         for(int i = 0; i < studPref.size(); i++){
             for(int j = 0; j < studPref.get(i).size(); j++ ){
                 invstudPref.get(i).add(studPref.get(i).indexOf(j));
             }
             //System.out.println(invstudPref.get(i));
         }

         //set all student matches to zero;
         ArrayList<Integer> match = new ArrayList<>();
         for(int i = 0; i < problem.getStudentCount(); i++){
             match.add( -1 );
         }
         // check if university has been assigned
         ArrayList<Integer> queue = new ArrayList<>();
         for(int i = 0; i < uniPref.size(); i++){
             queue.add(i);
         }

         //Gayle-Shapley
         ArrayList<Integer> eachUniPos = problem.getUniversityPositions();
         while(totalPositions != 0){ //m
             int university = queue.get(queue.size() -1);// which university
             queue.remove(queue.size() - 1);
             int student = 0;// which student in match
             while(eachUniPos.get(university) != 0){
                 int studentTheyWant = uniPref.get(university).get(student);
                 if(match.get(studentTheyWant) == -1){
                     match.set(studentTheyWant, university);
                     totalPositions--;
                     student++;
                     eachUniPos.set(university, eachUniPos.get(university)- 1);
                 }

                 else {
                     int universityTheyHave = match.get(student);
                     if (invstudPref.get(studentTheyWant).get(university) < invstudPref.get(studentTheyWant).get(universityTheyHave)) {
                         eachUniPos.set(university, eachUniPos.get(university) - 1);
                         eachUniPos.set(universityTheyHave, eachUniPos.get(university) + 1);
                         queue.add(universityTheyHave);
                         match.set(student, university);
                         student++;
                     } else {
                         student++;
                     }
                 }
             }
         }
         problem.setStudentMatching(match);
         return problem;
     }
 
     /**
      * Determines a solution to the stable matching problem from the given input set. Study the
      * project description to understand the variables which represent the input to your solution.
      *
      * @return A stable Matching.
      */
     @Override
     public Matching stableMatchingGaleShapley_studentoptimal(Matching problem) {
         /* TODO implement this function */
         ArrayList<ArrayList<Integer>> studPref = problem.getStudentPreference();
         ArrayList<ArrayList<Integer>> uniPref = problem.getUniversityPreference();
         int totalPositions = problem.totalUniversityPositions();

         // inverse student preference list
         ArrayList<ArrayList<Integer>> invstudPref = new ArrayList<ArrayList<Integer>>();
         for(int i = 0; i < studPref.size();i++){
             invstudPref.add(new ArrayList<>());
         }
         for(int i = 0; i < studPref.size(); i++){
             for(int j = 0; j < studPref.get(i).size(); j++ ){
                 invstudPref.get(i).add(studPref.get(i).indexOf(j));
             }
             //System.out.println(invstudPref.get(i));
         }

         //set all student matches to zero;
         ArrayList<Integer> match = new ArrayList<>();
         for(int i = 0; i < problem.getStudentCount(); i++){
             match.add( -1 );
         }
         // check if university has been assigned
         ArrayList<Integer> queue = new ArrayList<>();
         for(int i = 0; i < uniPref.size(); i++){
             queue.add(i);
         }

         //Gayle-Shapley
         ArrayList<Integer> eachUniPos = problem.getUniversityPositions();
         while(totalPositions != 0){ //m
             int university = queue.get(queue.size() -1);// which university
             queue.remove(queue.size() - 1);
             int student = 0;// which student in match
             while(eachUniPos.get(university) != 0){
                 int studentTheyWant = uniPref.get(university).get(student);
                 if(match.get(studentTheyWant) == -1){
                     match.set(studentTheyWant, university);
                     totalPositions--;
                     student++;
                     eachUniPos.set(university, eachUniPos.get(university)- 1);
                 }

                 else {
                     int universityTheyHave = match.get(student);
                     if (invstudPref.get(studentTheyWant).get(university) < invstudPref.get(studentTheyWant).get(universityTheyHave)) {
                         eachUniPos.set(university, eachUniPos.get(university) - 1);
                         eachUniPos.set(universityTheyHave, eachUniPos.get(university) + 1);
                         queue.add(universityTheyHave);
                         match.set(student, university);
                         student++;
                     } else {
                         student++;
                     }
                 }
             }
         }
         problem.setStudentMatching(match);


         return problem;
     }
 }