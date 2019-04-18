public interface Function {

	public static class ErrorOne extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public static class ErrorTwo extends Exception {
		private static final long serialVersionUID = 1L;
	}

	void
	// START SNIPPET: throwsError
	function(Object[] paramaeters) throws ErrorOne, ErrorTwo;
	// END SNIPPET: throwsError
}