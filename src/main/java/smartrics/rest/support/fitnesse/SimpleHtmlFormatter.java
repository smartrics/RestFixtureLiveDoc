package smartrics.rest.support.fitnesse;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import util.TimeMeasurement;
import fitnesse.responders.run.CompositeExecutionLog;
import fitnesse.responders.run.TestSummary;
import fitnesse.responders.run.TestSystem;
import fitnesse.responders.run.formatters.BaseFormatter;
import fitnesse.wiki.WikiPage;

/**
 * Formats the test result in markdown format compatible with github.
 * 
 * @author smartrics
 */
public class SimpleHtmlFormatter extends BaseFormatter {

	private static final HtmlSimplifier MD_TRANSLATOR = new HtmlSimplifier();

	private static class TranslatedPage {
		public String fqn;
		public String simplifiedHtml;

		public TranslatedPage(String name, String fqn, String html) {
			this.fqn = fqn;
			this.simplifiedHtml = MD_TRANSLATOR.simplify(name, html);
		}

	}

	private List<TranslatedPage> testResults = new ArrayList<TranslatedPage>();
	private StringBuffer currentChunkBuffer;
	private File output;

	/**
	 * Creates a formatter.
	 * 
	 * @param output
	 *            the path to the output file.
	 */
	public SimpleHtmlFormatter(String output) {
		this.output = new File(output);
		System.out.println(this.output.getAbsolutePath());
		if (this.output.exists()) {
			this.output.delete();
		}
	}

	@Override
	public void setExecutionLogAndTrackingId(String stopResponderId,
			CompositeExecutionLog log) throws Exception {
		System.out.println("setExecutionLogAndTrackingId " + stopResponderId);
	}

	@Override
	public void testSystemStarted(TestSystem testSystem, String testSystemName,
			String testRunner) throws Exception {
	}

	@Override
	public void newTestStarted(WikiPage test, TimeMeasurement timeMeasurement)
			throws Exception {
		currentChunkBuffer = new StringBuffer();
	}

	@Override
	public void testOutputChunk(String output) throws Exception {
		currentChunkBuffer.append(output);
	}

	@Override
	public void writeHead(String pageType) throws Exception {

	}

	@Override
	public void allTestingComplete(TimeMeasurement totalTimeMeasurement)
			throws Exception {
		super.allTestingComplete(totalTimeMeasurement);
		FileOutputStream fos = new FileOutputStream(output);
		for (TranslatedPage page : testResults) {
			fos.write(page.simplifiedHtml.getBytes());
		}
		fos.close();
	}

	@Override
	public void announceNumberTestsToRun(int testsToRun) {
		super.announceNumberTestsToRun(testsToRun);
	}

	@Override
	public void testComplete(WikiPage test, TestSummary summary,
			TimeMeasurement timeMeasurement) throws Exception {
		super.testComplete(test, summary, timeMeasurement);
		String fqn = getPageFullyQualifiedName(test);
		String buff = currentChunkBuffer.toString();
		currentChunkBuffer = null;
		testResults.add(new TranslatedPage(test.getName(), fqn, buff));
	}

	private String getPageFullyQualifiedName(WikiPage test) throws Exception {
		String pageName = test.getName();
		WikiPage parent = test.getParent();
		while (parent != null && parent != parent.getParent()) {
			pageName = parent.getName() + "." + pageName;
			parent = parent.getParent();
		}
		return pageName;
	}

}
