package type.common.work;

public class TypeLogger {
	public void info(String msg) {
		log("Info", msg);
	}
	public void info(String work, String msg) {
		log("Info", work, msg);
	}
	public void warning(String msg) {
		log("Warning", msg);
	}
	public void warning(String work, String msg) {
		log("Warning", work, msg);
	}
	public void severe(String msg) {
		log("Severe", msg);
	}
	public void severe(String work, String msg) {
		log("Severe", work, msg);
	}
	public void error(String msg) {
		log("Error", msg);
	}
	public void fine(String msg) {
		log("Fine", msg);
	}
	public void fine(String work, String msg) {
		log("Fine", work, msg);
	}
	public void error(String work, String msg) {
		log("Error", msg, work);
	}
	public void log(String head, String msg) {
		log(head, "", msg);
	}
	public void log(String head, String work, String msg) {
		if(work.isBlank()) {
			System.out.println("[" + head + "] " + msg);
		}else {
			System.out.println("[" + head + "][" + work + "] " + msg);
		}
	}
}
