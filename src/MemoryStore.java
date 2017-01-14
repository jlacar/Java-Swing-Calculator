/*
 * Author: Alex Matthews
 * Ireland
 */	
public class MemoryStore {

		private double storedVal = 0.0;

		public void store(double num) {
			storedVal = num;
		}

		public double getStoredValue() {
			return storedVal;
		}

		public void resetStoredValue() {
			storedVal = 0.0;
		}

		public void plusStoredValue(double num) {
			
			storedVal += num;
		}

		public void minusStoredValue(double num) {
			
			storedVal -= num;
		}
		
	}