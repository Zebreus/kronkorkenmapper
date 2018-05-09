
public enum Output {
	/*
	 * NONE : No output
	 * ERROR : Only Errors
	 * NORMAL : Only necessary output
	 * HIGH : Log exactly what the program is doing
	 * DEBUG : Log everything + details
	 * 
	 */
	NONE,ERROR,NORMAL,HIGH,DEBUG;

	public static Output level = Output.NORMAL;
	
	public void log(String string) {
		if(this.compareTo(level)<=0){
			System.out.print(string);
		}
	}
	
	public void logln(String string) {
		this.log(string+'\n');
	}
	
	public static void logException(Exception e,String simplemessage) {
		if(Output.ERROR.compareTo(level)<=0){
			System.err.println(simplemessage);
		}
		if(Output.HIGH.compareTo(level)<=0){
			System.err.println("Errormessage: "+e.getMessage());
			if(Output.DEBUG.compareTo(level)>0){
				System.err.println("Set outputlevel to DEBUG for more information.");
			}
		}
		if(Output.DEBUG.compareTo(level)<=0){
			e.printStackTrace();
		}
	}
}
