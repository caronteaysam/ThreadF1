package controller;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ThreadF1 extends Thread implements Comparable<ThreadF1> {
	
	public static ArrayList <ThreadF1> list = new ArrayList<ThreadF1> ( );
	public static int finished = 0;
	
	private Semaphore sem_pit;
	private Semaphore sem_team;
	private long min_lap = Long.MAX_VALUE;
	
	public ThreadF1 ( Semaphore sem_pit, Semaphore sem_team ) {
		
		this.sem_pit = sem_pit;
		this.sem_team = sem_team;
		ThreadF1.list.add ( this );
	}
	
	@Override
	public int compareTo ( ThreadF1 o ) {
		return (int)(this.min_lap - o.min_lap);
	}
	
	@Override
	public void run() {
		
		leavePit ( );
		roll ( );
	}
	
	private void leavePit ( ) {
		
		try {
			
			long exit_min = 5000;
			long exit_delta = 3000;
			
			this.sem_team.acquire ( );
			System.out.printf ( "O carro n.%d está aguardando para sair do pit\n", getId ( ));

			this.sem_pit.acquire ( );
			System.out.printf ( "O carro n.%d está saindo do pit\n", getId ( ));
			sleep ((long)( exit_min + Math.random ( ) * exit_delta ));
			System.out.printf ( "O carro n.%d saiu do pit\n", getId ( ));
			
		} catch ( InterruptedException e ) { e.printStackTrace();
		} finally {
			
			this.sem_pit.release ( );
		}
	}
	
	private void roll ( ) {
		
		try {
			
			long lap_min = 10000;
			long lap_delta = 3000;
			long current_lap = 0;
			long max_laps = 3;
			
			long rand;
			while ( current_lap < max_laps ) {
				
				current_lap++;
				rand = (long)( lap_min + Math.random ( ) * lap_delta );
				sleep ( rand );
				System.out.printf ( "O carro n.%d passou a volta %d em %d\n", getId( ), current_lap, rand );
				
				if ( rand < this.min_lap ) this.min_lap = rand;
			}
			System.out.printf ( "O carro n.%d tem a menor volta em %d\n", getId ( ), min_lap );
			finished++;
			
			if ( finished < list.size ( )) return;

			ThreadF1.list.sort ( null );
			System.out.println ( ThreadF1.list );
		} catch ( InterruptedException e ) { e.printStackTrace();
		} finally {
			
			this.sem_team.release ( );
		}
	}
	
	@Override
	public String toString ( ) {
		return "Car " + getId ( ) + " min_lap: " + min_lap;
	}
}