package view;

import java.util.concurrent.Semaphore;

import controller.ThreadF1;

public class Main {

	public static void main(String[] args) {

		int teams = 7;
		int teams_size = 2;
		
		Semaphore sem_pit = new Semaphore ( 5 );
		Semaphore sem_team;
		
		for ( int i = 0; i < teams; i++ ) {
			
			sem_team = new Semaphore ( 1 );
			for ( int i2 = 0; i2 < teams_size; i2++ ) {
				
				new ThreadF1 ( sem_pit, sem_team ).start ( );
			}
		}
	}
}
